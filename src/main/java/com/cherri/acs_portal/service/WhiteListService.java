package com.cherri.acs_portal.service;

import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.input.WhiteListPanCreateDTO;
import com.cherri.acs_portal.dto.blackList.input.WhiteListPanQueryDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.dto.whitelist.WhiteListQueryResult;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.service.validator.CardImportValidator;
import com.cherri.acs_portal.util.ExcelBuildUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.HmacException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.commons.utils.HmacUtils;
import ocean.acs.commons.utils.MaskUtils;
import ocean.acs.models.dao.BinRangeDAO;
import ocean.acs.models.dao.PanInfoDAO;
import ocean.acs.models.dao.WhiteListPanDAO;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.entity.WhiteListPanDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.WhiteListPanCreateDO;
import ocean.acs.models.data_object.portal.WhiteListPanQueryDO;
import ocean.acs.models.data_object.portal.WhiteListQueryResultDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class WhiteListService {

    private final PanInfoDAO panInfoDAO;
    private final WhiteListPanDAO whiteListPanDAO;
    private final BinRangeDAO binRangeDao;

    private final AcsIntegratorManager acsIntegratorManager;
    private final HsmPlugin hsmPlugin;
    private final HttpServletResponse httpServletResponse;

    private final CardImportValidator cardImportValidator;

    private List<String> getWhitelistPanCsvColumnNames() {
      return Arrays.asList(
          MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_ADDED_TIME),
          MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_PAN));
    }
    @Autowired
    public WhiteListService(
      PanInfoDAO panInfoDAO,
      WhiteListPanDAO whiteListPanDAO,
      BinRangeDAO binRangeDao,
      AcsIntegratorManager acsIntegratorManager,
      HsmPlugin hsmPlugin,
      HttpServletResponse httpServletResponse,
      CardImportValidator cardImportValidator) {
        this.panInfoDAO = panInfoDAO;
        this.whiteListPanDAO = whiteListPanDAO;
        this.binRangeDao = binRangeDao;
        this.acsIntegratorManager = acsIntegratorManager;
        this.hsmPlugin = hsmPlugin;
        this.httpServletResponse = httpServletResponse;
        this.cardImportValidator = cardImportValidator;
    }

    public PagingResultDTO<WhiteListQueryResult> query(WhiteListPanQueryDTO queryDto) {
        String cardNumberHash;
        try {
            cardNumberHash =
              HmacUtils.encrypt(
                queryDto.getRealCardNumber(), EnvironmentConstants.hmacHashKey);
        } catch (HmacException e) {
            throw new OceanException(e.getMessage(), e);
        }

        PagingResultDO<WhiteListQueryResultDO> queryResultPage =
          whiteListPanDAO.query(
            WhiteListPanQueryDO.newInstance(
              queryDto.getStartTime(),
              queryDto.getEndTime(),
              queryDto.getCardBrand(),
              cardNumberHash,
              queryDto.getIssuerBankId()));

        PagingResultDTO<WhiteListQueryResult> resDto = new PagingResultDTO<>();
        resDto.setTotal(queryResultPage.getTotal());
        resDto.setTotalPages(queryResultPage.getTotalPages());
        resDto.setCurrentPage(queryResultPage.getCurrentPage());

        List<WhiteListQueryResult> data =
          queryResultPage.getData().stream()
            .map(WhiteListQueryResult::valueOf)
            .collect(Collectors.toList());

        resDto.setData(data);

        return resDto;
    }

    public PagingResultDTO<WhiteListQueryResult> queryByIds(IdsQueryDTO queryDto) {
        Pageable pageRequest = PageRequest.of(1, EnvironmentConstants.PAGINATION_MAX_ROWS);
        PagingResultDO<WhiteListQueryResultDO> queryResultPage =
          whiteListPanDAO.findByIds(queryDto.getIds(), pageRequest);

        List<WhiteListQueryResult> whiteListQueryResultList = queryResultPage.getData().stream()
          .map(
            e ->
              WhiteListQueryResult.valueOf(e)).collect(Collectors.toList());

        PagingResultDTO<WhiteListQueryResult> response = new PagingResultDTO<>();
        response.setData(whiteListQueryResultList);
        response.setTotalPages(queryResultPage.getTotalPages());
        response.setTotal(queryResultPage.getTotal());
        response.setCurrentPage(queryResultPage.getCurrentPage());
        return response;
    }

  /** 新增白名單 */
  public WhiteListPanCreateDTO addWhiteListPan(WhiteListPanCreateDTO createDto)
      throws OceanException {
    // 檢查是否有BinRange資料
    if (!cardImportValidator.isBinRangeExists(createDto.getIssuerBankId())) {
      throw new OceanException(ResultStatus.NO_SELF_BANK_BIN_RANGE, "No Bin-Range data");
    }
    // 檢查卡號是否符合自行的BinRange範圍之中
    String notInBinRangeCard =
        cardImportValidator.extractNotExistsInBinRangeCard(
            createDto.getIssuerBankId(), createDto.getRealCardNumber());
    if (StringUtils.isNotEmpty(notInBinRangeCard)) {
      throw new OceanException(
          ResultStatus.NO_SELF_BANK_BIN_RANGE, "Does not exists in Bin-Range:" + notInBinRangeCard);
    }
    // 卡號是否已存在白名單
    if (cardImportValidator.isCardNumberExistedInWhiteList(
        createDto.getIssuerBankId(), createDto.getRealCardNumber())) {
      log.warn(
          "[addWhiteListPan] Duplicate cardNumber={}",
          StringUtils.normalizeSpace(MaskUtils.acctNumberMask(createDto.getRealCardNumber())));
      throw new OceanException(
          ResultStatus.DUPLICATE_DATA_ELEMENT, "The card number exists in the whitelist.");
    }
      // 卡號是否已存在於其他間銀行
      if (cardImportValidator.isCardNumberExistedInOtherBank(
        createDto.getIssuerBankId(), createDto.getRealCardNumber())) {
          log.warn(
            "[addWhiteListPan] card number exists in other banks, cardNumber={}",
            StringUtils.normalizeSpace(MaskUtils.acctNumberMask(createDto.getRealCardNumber())));
          throw new OceanException(
            ResultStatus.DUPLICATE_DATA_ELEMENT, "The card number exists in other banks.");
      }

      String cardBrand =
        binRangeDao.findCardBrandByIssuerBankIdAndCardNumberWithoutPadding(
          createDto.getIssuerBankId(),
          createDto.getRealCardNumber());

      String cardNumberHash;
      try {
          cardNumberHash = HmacUtils
            .encrypt(createDto.getRealCardNumber(), EnvironmentConstants.hmacHashKey);
      } catch (HmacException e) {
          log.error("[addWhiteListPan] hmac error.", e);
          throw new OceanException(ResultStatus.HMAC_ERROR, e.getMessage());
      }

      String cardNumberMask = MaskUtils.acctNumberMask(createDto.getRealCardNumber());
      String cardNumberEn =
        hsmPlugin
          .encryptWithIssuerBankId(createDto.getRealCardNumber(), createDto.getIssuerBankId())
          .getBase64();

      PanInfoDO panInfo =
        panInfoDAO.findOrCreatePanInfo(
          createDto.getIssuerBankId(),
          cardBrand,
          cardNumberMask,
          cardNumberEn,
          cardNumberHash,
          createDto.getUser());

      WhiteListPanDO whiteListPan = whiteListPanDAO.add(
        WhiteListPanCreateDO.newInstance(
          createDto.getId(),
          createDto.getIssuerBankId(),
          createDto.getRealCardNumber(),
          createDto.getFileContent(),
          createDto.getFileName(),
          createDto.getUser(),
          createDto.getVersion()
          , createDto.getAuditStatus()
        ), panInfo.getId());
      if (whiteListPan != null) {
          createDto.setId(whiteListPan.getId());
      }
      return createDto;
  }

  /** 刪除白名單 by id */
  public DeleteDataDTO deleteById(DeleteDataDTO deleteDataDTO) {

    return whiteListPanDAO
      .delete(DeleteDataDO.valueOf(deleteDataDTO.getId(),
        deleteDataDTO.getIssuerBankId(),
        deleteDataDTO.getFileContent(),
        deleteDataDTO.getFileName(),
        deleteDataDTO.getUser(),
        deleteDataDTO.getVersion()
        , deleteDataDTO.getAuditStatus()
      ))
        .map(
            e -> {
              deleteDataDTO.setId(e.getId());
              deleteDataDTO.setAuditStatus(AuditStatus.getStatusBySymbol(e.getAuditStatus()));
              return deleteDataDTO;
            })
        .orElse(deleteDataDTO);
  }

  public void exportWhiteListPanCsvByQuery(List<WhiteListQueryResult> dataList, String timeZone) throws IOException {
    exportWhiteListPanCSV(dataList, timeZone);
  }

  private void exportWhiteListPanCSV(List<WhiteListQueryResult> dataList, String timeZone) throws IOException {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet();
    List<String> columnNames = getWhitelistPanCsvColumnNames();
    ExcelBuildUtils.createHeader(workbook, sheet, columnNames);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of(timeZone)));
    // Create data body
    for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
      Row row = sheet.createRow(rowNum);
      WhiteListQueryResult panDto = dataList.get(rowNum - 1);
      Date date = new Date(panDto.getCreateMillis());
      String createDateTime = sdf.format(date);
      row.createCell(0).setCellValue(createDateTime);
      row.createCell(1).setCellValue(panDto.getCardNumber());
    }
    ExcelBuildUtils.resizeColums(sheet, columnNames.size());
    String fileName = ExcelBuildUtils.getFileNameFormat(MessageConstants.get(MessageConstants.EXCEL_WHITE_LIST_PAN));
    ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
  }

  /** 取得新增白名單資料 by id */
  public Optional<WhiteListPanCreateDTO> getWhiteListPanDtoById(Long whiteListPanId) {
      Optional<WhiteListPanDO> result = whiteListPanDAO.findById(whiteListPanId);
      return result.map(WhiteListPanCreateDTO::valueOfWhiteListPanDO);
  }

  /** 修改白名單 */
  public WhiteListPanCreateDTO updateWhiteListPan(WhiteListPanCreateDTO whiteListPanCreateDto) {
    return whiteListPanDAO
      .update(WhiteListPanCreateDO.newInstance(
        whiteListPanCreateDto.getId(),
        whiteListPanCreateDto.getIssuerBankId(),
        whiteListPanCreateDto.getRealCardNumber()
        , whiteListPanCreateDto.getFileContent(),
        whiteListPanCreateDto.getFileName()
        , whiteListPanCreateDto.getUser(),
        whiteListPanCreateDto.getVersion(),
        whiteListPanCreateDto.getAuditStatus()
      ))
        .map(
            e -> {
              whiteListPanCreateDto.setId(e.getId());
              return whiteListPanCreateDto;
            })
        .orElse(whiteListPanCreateDto);
  }
}
