package com.cherri.acs_portal.service;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.attempt.GrantedLogDTO;
import com.cherri.acs_portal.dto.attempt.GrantedLogQueryDTO;
import com.cherri.acs_portal.dto.attempt.GrantedTransactionLogDTO;
import com.cherri.acs_portal.dto.whitelist.AttemptGrantedDTO;
import com.cherri.acs_portal.dto.whitelist.AttemptSettingLimitationDTO;
import com.cherri.acs_portal.util.ExcelBuildUtils;
import com.neovisionaries.i18n.CurrencyCode;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.models.dao.CurrencyDAO;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.dao.PanInfoDAO;
import ocean.acs.models.dao.WhiteListAttemptSettingDAO;
import ocean.acs.models.data_object.entity.CurrencyDO;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.entity.WhiteListAttemptSettingDO;
import ocean.acs.models.data_object.portal.GrantedLogQueryDO;
import ocean.acs.models.data_object.portal.GrantedTransactionLogDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class WhiteListAttemptService {

    @Autowired
    private PanInfoDAO panInfoDao;
    @Autowired
    private WhiteListAttemptSettingDAO settingDAO;
    @Autowired
    private CurrencyDAO currencyDao;

    public Optional<AttemptGrantedDTO> findOneWhiteListAttemptSettingByPanId(Long panId) {
        return settingDAO.findOneByPanId(panId).map(AttemptGrantedDTO::valueOf);
    }

    public PagingResultDTO<GrantedLogDTO> getAttemptGrantedLog(GrantedLogQueryDTO queryDto)
      throws DatabaseException {
        PagingResultDTO<AttemptGrantedDTO> attemptSettingPage =
            getWhiteListAttemptSetting(queryDto);
        if (attemptSettingPage.isEmpty()) {
            return PagingResultDTO.empty();
        }

        List<GrantedLogDTO> dataList =
            attemptSettingPage.getData().stream()
                .map(
                    attemptSetting -> {
                        List<GrantedTransactionLogDO> transactionLogList =
                            settingDAO.getGrantedTransactionLog(
                                attemptSetting.getId());
                        log.debug(
                            "[get3ds2AttemptGrantedLog] transactionLogDTOList size={}",
                            transactionLogList.size());
                        return GrantedLogDTO.valueOf(
                            attemptSetting, transactionLogList);
                    })
                .collect(Collectors.toList());

        PagingResultDTO<GrantedLogDTO> resultPage = new PagingResultDTO<>();
        resultPage.setCurrentPage(attemptSettingPage.getCurrentPage());
        resultPage.setTotal(attemptSettingPage.getTotal());
        resultPage.setTotalPages(attemptSettingPage.getTotalPages());
        resultPage.setData(dataList);
        return resultPage;
    }

    private PagingResultDTO<AttemptGrantedDTO> getWhiteListAttemptSetting(
      GrantedLogQueryDTO logQueryDTO) {
        PageRequest page =
          PageRequest.of(
            logQueryDTO.getPage() - 1,
            logQueryDTO.getPageSize(),
            Sort.Direction.DESC,
            "s.createMillis");
        Page<WhiteListAttemptSettingDO> attemptSettingPage =
          settingDAO.findAttemptSettingByPanId(
            GrantedLogQueryDO.newInstance(
              logQueryDTO.getPanId(),
              logQueryDTO.getIssuerBankId()),
            page);
        if (attemptSettingPage == null || attemptSettingPage.isEmpty()) {
            return PagingResultDTO.empty();
        }

        List<AttemptGrantedDTO> settingDTOList =
          attemptSettingPage.getContent().stream()
            .map(AttemptGrantedDTO::valueOf)
            .collect(Collectors.toList());

        PagingResultDTO<AttemptGrantedDTO> pagingResult = new PagingResultDTO<>();
        pagingResult.setData(settingDTOList);
        pagingResult.setTotal(attemptSettingPage.getTotalElements());
        pagingResult.setCurrentPage(attemptSettingPage.getNumber() + 1);
        pagingResult.setTotalPages(attemptSettingPage.getTotalPages());
        return pagingResult;
    }

    public AttemptSettingLimitationDTO getAttemptAuthorizeSetting(Long issuerBankId) {
        AttemptSettingLimitationDTO settingDTO = new AttemptSettingLimitationDTO();
        settingDTO.setExpireTime(EnvironmentConstants.WHITELIST_ATTEMPT_AVAILABLE_DURATION);
        settingDTO.setTriesPermittedLimit(
          EnvironmentConstants.WHITELIST_ATTEMPT_AVAILABLE_TRIES_LIMIT);
        settingDTO.setCurrencyList(getCurrencyList());
        return settingDTO;
    }

    private List<AttemptSettingLimitationDTO.Currency> getCurrencyList() {
        List<AttemptSettingLimitationDTO.Currency> currencyList = new ArrayList<>();

        currencyDao.findByUseExchangeCodeNotNull().stream()
          .sorted(Comparator.comparing(CurrencyDO::getAlpha))
          .forEach(
            currencyDataObj -> {
                AttemptSettingLimitationDTO.Currency currency =
                  new AttemptSettingLimitationDTO.Currency();
                currency.setCode(currencyDataObj.getCode());
                currency.setName(currencyDataObj.getAlpha());
                currencyList.add(currency);
            });
        return currencyList;
    }

    /** 人工彈性授權設定 */
    public AttemptGrantedDTO addAttemptGrantedSetting(AttemptGrantedDTO grantedDTO)
      throws DatabaseException {
        return addWhiteListAttemptSetting(grantedDTO);
    }

    /** ACS 2.0 人工彈性授權設定 */
    private AttemptGrantedDTO addWhiteListAttemptSetting(AttemptGrantedDTO grantDTO) {
        boolean existsPanInfo = panInfoDao.countsByIdAndIssuerBankId(
          grantDTO.getPanId(), grantDTO.getIssuerBankId()) > 0;
        if (!existsPanInfo) {
            throw new NoSuchDataException("The PanInfo ID=" + grantDTO.getPanId()
              + " and issuerBanId=" + grantDTO.getIssuerBankId() + " not found.");
        }

        // 當前時間 + (有效時間(30分鐘)*秒(60)*毫秒(1000))
        long now = System.currentTimeMillis();
        long expiredTime =
          now + (EnvironmentConstants.WHITELIST_ATTEMPT_AVAILABLE_DURATION * 60 * 1000);

        WhiteListAttemptSettingDO whiteListAttemptSetting =
          WhiteListAttemptSettingDO.builder()
            .currency(grantDTO.getCurrency())
            .amount(grantDTO.getMaxMoney())
            .attemptExpiredTime(expiredTime)
            .auditStatus(grantDTO.getAuditStatus().getSymbol())
            .id(grantDTO.getId())
            .panId(grantDTO.getPanId())
            .permittedTimes(grantDTO.getTriesPermitted().intValue())
            .permittedQuota(grantDTO.getTriesPermitted().intValue())
            .createMillis(System.currentTimeMillis())
            .creator(grantDTO.getCreator())
            .build();

        WhiteListAttemptSettingDO addResult = settingDAO.saveOrUpdate(whiteListAttemptSetting);
        grantDTO.setId(addResult.getId());
        return grantDTO;
    }

    public PanInfoDO getPanInfoById(Long panInfoId) throws DatabaseException {
        return panInfoDao
          .findById(panInfoId)
          .orElseThrow(
            () -> new NoSuchDataException("Pan info not found by panId=" + panInfoId));
    }

    private static final List<String> ATTEMPT_LOG_CSV_COLUMN_NAMES =
      Arrays.asList("設定時間", "使用次數上限", "金額上限", "設定人員", "客戶交易紀錄");
    private static final String EXECUTE_LOG_FORMAT = "%s ,%s, %s";

    public void exportAttemptLogCSV(List<GrantedLogDTO> dataList, HttpServletResponse rps)
      throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);

        ExcelBuildUtils.createHeader(workbook, sheet, ATTEMPT_LOG_CSV_COLUMN_NAMES);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(EnvironmentConstants.ACS_TIMEZONE);
        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            GrantedLogDTO logDto = dataList.get(rowNum - 1);
            Date date = new Date(logDto.getAuthMillis());
            String createDateTime = sdf.format(date);
            row.createCell(0).setCellValue(createDateTime);
            row.createCell(1).setCellValue(logDto.getTriesPermitted());
            String currencyName = "";
            if (StringUtils.isNumeric(logDto.getCurrency())) {
                CurrencyCode currencyCode =
                  CurrencyCode.getByCode(Integer.parseInt(logDto.getCurrency()));
                currencyName = currencyCode == null ? "unknown" : currencyCode.name();
            }
            row.createCell(2).setCellValue(logDto.getMaxMoney() + " " + currencyName);
            row.createCell(3).setCellValue(logDto.getApproved());

            StringBuilder executeLog = new StringBuilder();
            String seperator = "";
            for (GrantedTransactionLogDTO transLog : logDto.getTransactionLogList()) {
                String executeDateTime =
                  transLog.getExecuteMillis() > 0
                    ? sdf.format(new Date(transLog.getExecuteMillis()))
                    : "";
                executeLog
                  .append(seperator)
                  .append(
                    String.format(
                      EXECUTE_LOG_FORMAT,
                      executeDateTime,
                      transLog.getAmount(),
                      currencyName));
                seperator = "\n";
            }
            row.createCell(4).setCellValue(executeLog.toString());
        }
        ExcelBuildUtils.resizeColums(sheet, ATTEMPT_LOG_CSV_COLUMN_NAMES.size());
        String fileName = ExcelBuildUtils.getFileNameFormat("Attempt_Log");
        ExcelBuildUtils.doExport(rps, fileName, workbook);
    }

    public void updateWhiteListAttemptSetting(AttemptGrantedDTO updateDto) {
        WhiteListAttemptSettingDO entity =
          settingDAO
            .findOneByPanId(updateDto.getId())
            .orElseThrow(
              () ->
                new NoSuchDataException(
                  "id=" + updateDto.getId() + " not found."));
        entity.setAuditStatus(updateDto.getAuditStatus().getSymbol());
        entity.setUpdater(updateDto.getCreator());
        entity.setUpdateMillis(System.currentTimeMillis());
        settingDAO.saveOrUpdate(entity);
    }
}
