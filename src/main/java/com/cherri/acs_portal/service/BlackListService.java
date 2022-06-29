package com.cherri.acs_portal.service;

import com.cherri.acs_portal.constant.MessageConstants;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.controller.request.BlackListDeviceIdQueryDTO;
import com.cherri.acs_portal.controller.request.BlackListDeviceOperationReqDto;
import com.cherri.acs_portal.controller.request.BlackListIpQueryDTO;
import com.cherri.acs_portal.controller.request.BlackListPanQueryDTO;
import com.cherri.acs_portal.controller.response.BlackListMerchantUrlResDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.input.BlackListMerchantUrlQueryDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListAuthStatusDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListDeviceInfoDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListIpGroupDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListMerchantUrlDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListPanBatchDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListPanDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListQueryResultDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.manager.BlackListIpManager;
import com.cherri.acs_portal.service.validator.CardImportValidator;
import com.cherri.acs_portal.util.AcsPortalUtil;
import com.cherri.acs_portal.util.ExcelBuildUtils;
import com.cherri.acs_portal.util.IpUtils;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.BlackListAuthStatusCategory;
import ocean.acs.commons.enumerator.DeviceChannel;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.HmacException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.commons.utils.HmacUtils;
import ocean.acs.commons.utils.MaskUtils;
import ocean.acs.models.dao.BatchImportDAO;
import ocean.acs.models.dao.BinRangeDAO;
import ocean.acs.models.dao.BlackListAuthStatusDAO;
import ocean.acs.models.dao.BlackListDeviceInfoDAO;
import ocean.acs.models.dao.BlackListIpGroupDAO;
import ocean.acs.models.dao.BlackListMerchantUrlDAO;
import ocean.acs.models.dao.BlackListPanBatchDAO;
import ocean.acs.models.dao.BlackListPanDAO;
import ocean.acs.models.dao.PanInfoDAO;
import ocean.acs.models.dao.TransactionLogDAO;
import ocean.acs.models.data_object.entity.BatchImportDO;
import ocean.acs.models.data_object.entity.BlackListAuthStatusDO;
import ocean.acs.models.data_object.entity.BlackListDeviceInfoDO;
import ocean.acs.models.data_object.entity.BlackListIpGroupDO;
import ocean.acs.models.data_object.entity.BlackListMerchantUrlDO;
import ocean.acs.models.data_object.entity.BlackListPanDO;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.portal.BlackListDeviceIdQueryDO;
import ocean.acs.models.data_object.portal.BlackListMerchantUrlQueryDO;
import ocean.acs.models.data_object.portal.BlackListPanQueryDO;
import ocean.acs.models.data_object.portal.BlackListQueryResultDO;
import ocean.acs.models.data_object.portal.ComplexBlackListDeviceDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.IdsQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.PortalBlackListIpGroupDO;
import ocean.acs.models.data_object.portal.PortalBlackListMerchantUrlDO;

@Log4j2
@Service
public class BlackListService {

    private final HttpServletResponse httpServletResponse;
    private final BlackListAuthStatusDAO authStatusDao;

    private final AcsIntegratorManager acsIntegratorManager;

    private final BlackListIpGroupDAO ipGroupDao;
    private final BlackListIpManager ipManager;
    private final BlackListDeviceInfoDAO blackListDeviceInfoDao;

    private final BlackListMerchantUrlDAO merchantUrlDao;

    private final BlackListPanBatchDAO panBatchDao;
    private final BatchImportDAO batchImportDao;
    private final PanInfoDAO panInfoDAO;
    private final BlackListPanDAO blackListPanDAO;
    private final TransactionLogDAO txLogDao;
    private final BinRangeDAO binRangeDao;
    private final HsmPlugin hsmPlugin;

    private final CardImportValidator cardImportValidator;

    @Autowired
    public BlackListService(
      HttpServletResponse httpServletResponse,
      AcsIntegratorManager acsIntegratorManager,
      BlackListAuthStatusDAO authStatusDao,
      BlackListIpGroupDAO ipGroupDao1,
      BlackListIpManager ipManager,
      BlackListDeviceInfoDAO blackListDeviceInfoDao,
      BlackListMerchantUrlDAO merchantUrlDao,
      BlackListPanBatchDAO panBatchDao,
      BatchImportDAO batchImportDao, PanInfoDAO panInfoDAO,
      BlackListPanDAO blackListPanDAO,
      TransactionLogDAO txLogDao, BinRangeDAO binRangeDao,
      HsmPlugin hsmPlugin,
      CardImportValidator cardImportValidator) {
        this.httpServletResponse = httpServletResponse;
        this.acsIntegratorManager = acsIntegratorManager;
        this.authStatusDao = authStatusDao;
        this.ipGroupDao = ipGroupDao1;
        this.ipManager = ipManager;
        this.blackListDeviceInfoDao = blackListDeviceInfoDao;
        this.merchantUrlDao = merchantUrlDao;
        this.panBatchDao = panBatchDao;
        this.batchImportDao = batchImportDao;
        this.panInfoDAO = panInfoDAO;
        this.blackListPanDAO = blackListPanDAO;
        this.txLogDao = txLogDao;
        this.binRangeDao = binRangeDao;
        this.hsmPlugin = hsmPlugin;
        this.cardImportValidator = cardImportValidator;
    }

    public List<String> getBlacklistPanCsvColumnNames() {
        return Arrays.asList(
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_ADDED_TIME),
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_PAN),
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_BLOCK_TIMES));
    }

    public List<String> getBlacklistDeviceIdCsvColumnNames() {
        return Arrays.asList(
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_DEVICE_ID),
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_ADDED_TIME),
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_DEVICE_ID_IP_ADDRESS),
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_DEVICE_ID_DEVICE_CHANNEL),
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_DEVICE_ID_BROWSER_USER_AGENT),
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_BLOCK_TIMES));
    }

    public List<String> getBlacklistIpCsvColumnNames() {
        return Arrays.asList(
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_ADDED_TIME),
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_IP_IP_ADDRESS),
            MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_BLOCK_TIMES));
    }

    public List<String> getBlacklistMerchantCsvColumnNames() {
       return Arrays.asList(
           MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_ADDED_TIME),
           MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_MERCHANT_UR_MERCHANT_URL_KEYWORDS));
    }

    public TransStatus getTransStatus(Long issuerBankId, BlackListAuthStatusCategory category)
      throws OceanException {
        try {
            return authStatusDao.getAuthStatus(issuerBankId, category);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        } catch (NoSuchDataException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public List<BlackListAuthStatusDTO> findAuthStatusAll(Long issuerBankId) throws OceanException {
        List<BlackListAuthStatusDO> list;
        try {
            list = authStatusDao.findByIssuerBank(issuerBankId);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        return list.stream().map(BlackListAuthStatusDTO::valueOf).collect(Collectors.toList());
    }

    public Optional<BlackListAuthStatusDTO> findOneAuthStatus(Long blackListAuthStatusId)
      throws OceanException {
        try {
            return authStatusDao
              .findOneAuthStatus(blackListAuthStatusId)
              .map(BlackListAuthStatusDTO::valueOf);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public BlackListAuthStatusDTO updateAuthStatus(BlackListAuthStatusDTO updateDto)
      throws OceanException {
        TransStatus transStatus = TransStatus.codeOf(updateDto.getAuthStatus());
        if (TransStatus.UNKNOWN.equals(transStatus)) {
            throw new IllegalArgumentException(
              "The transStatus=" + updateDto.getAuthStatus() + " is unknown.");
        }
        try {
            BlackListAuthStatusDO blackListAuthStatus = authStatusDao
              .findOneAuthStatus(updateDto.getId())
              .map(
                entity -> {
                    entity.setTransStatus(transStatus.getCode());
                    entity.setAuditStatus(updateDto.getAuditStatus().getSymbol());
                    entity.setUpdater(updateDto.getUser());
                    entity.setUpdateMillis(System.currentTimeMillis());
                    return entity;
                })
              .orElseThrow(
                () -> new NoSuchDataException("Id=" + updateDto.getId() + " not found."));

            authStatusDao.saveOrUpdateAuthStatus(blackListAuthStatus);
            if (AuditStatus.PUBLISHED.equals(updateDto.getAuditStatus())) {
                batchUpdateByBlacklistCategory(blackListAuthStatus.getCategory(), transStatus,
                  updateDto);
            }
            return updateDto;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void batchUpdateByBlacklistCategory(
      Integer categoryCode, TransStatus transStatus, BlackListAuthStatusDTO updateDto)
      throws IllegalArgumentException, DatabaseException {
        BlackListAuthStatusCategory category = BlackListAuthStatusCategory.getByCode(categoryCode);
        switch (category) {
            case BLACK_LIST_PAN:
                // 更新卡號批次黑名單的"單筆新增"的transStatus
                if (AuditStatus.PUBLISHED.equals(updateDto.getAuditStatus())) {
                    panBatchDao.updateTransStatusByIssuerBankIdAndBatchName(
                      updateDto.getIssuerBankId(),
                      EnvironmentConstants.DEFAULT_BLACK_LIST_PAN_BATCH_NAME,
                      transStatus);
                }
                // 單筆新增的卡號因為在kernel那邊是用join的方式組出卡號黑名單中的驗證狀態，所以這裡不批次更新
                break;
            case BLACK_LIST_DEVICE_ID:
                blackListDeviceInfoDao
                  .updateTransStatusAndUpdaterByIssuerBankId(updateDto.getIssuerBankId(),
                    transStatus, updateDto.getUser());
            case BLACK_LIST_IP:
                ipGroupDao.updateTransStatusByIssuerBankId(
                  updateDto.getIssuerBankId(), transStatus, updateDto.getUser());
                break;
            case BLACK_LIST_MERCHANT_URL:
                merchantUrlDao.updateTransStatusByIssuerBankId(
                  updateDto.getIssuerBankId(), transStatus, updateDto.getUser());
                break;
            default:
                throw new IllegalArgumentException("The BlackListAuthStatusCategory is unknown.");
        }
    }

    public Optional<BlackListPanDTO> findOneBlackListPan(Long issuerBankId, Long blackListPanId)
      throws DatabaseException {
        Optional<BlackListPanDO> panOpt;
        try {
            panOpt = blackListPanDAO.findById(blackListPanId);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }

        if (!panOpt.isPresent()) {
            return Optional.empty();
        }
        BlackListPanDO blackListPan = panOpt.get();
        return panInfoDAO.findById(blackListPan.getPanId())
          .map(panInfo -> BlackListPanDTO.valueOf(panInfo, blackListPan));
    }

    /**
     * 若有重複則回傳已重複的IP+CIDR
     *
     * @param issuerBankId
     * @param ip
     * @param cidr
     * @return
     */
    public String isDuplicateIpRange(Long issuerBankId, String ip, Integer cidr) {
        List<BlackListIpGroupDO> allIp = ipGroupDao.findByIssuerBankId(issuerBankId);
        if (allIp == null || allIp.isEmpty()) {
            return "";
        }
        // 轉ipv6
        String ipv6;
        if (IpUtils.isIPv4(ip)) {
            ipv6 = IpUtils.ip4To6(combineIpAndCidr(ip, cidr));
        } else {
            ipv6 = combineIpAndCidr(ip, cidr);
        }

        // 判斷範圍
        return allIp.stream()
          .filter(
            blackListIpGroup ->
              IpUtils.inRange(
                ipv6, combineIpAndCidr(blackListIpGroup.getIp(), blackListIpGroup.getCidr())))
          .map(
            blackListIpGroup -> {
                String result =
                  combineIpAndCidr(blackListIpGroup.getIp(), blackListIpGroup.getCidr());
                // 原始資料為IPv4
                if (4 == blackListIpGroup.getOriginVersion()) {
                    result = IpUtils.ip6To4(result);
                }
                // 若原始資料沒有CIDR則去除
                if (null == blackListIpGroup.getCidr()) {
                    result = result.split("/")[0];
                }

                log.warn(
                    "[isDuplicateIpRange] The IP/CIDR exists in blackListIpGroup.id={}, IP={}, CIDR={}, ip/cidr={}",
                    blackListIpGroup.getId(),
                    StringUtils.normalizeSpace(ip),
                    cidr,
                    StringUtils.normalizeSpace(result)
                );
                return result;
            })
          .findFirst()
          .orElse("");
    }

    private String combineIpAndCidr(String ip, Integer cidr) {
        // cidr若為空則預設ipv6的cidr最大值128
        cidr = cidr == null ? 128 : cidr;
        return ip + "/" + cidr;
    }

    public Optional<BlackListIpGroupDTO> findOneBlackListIpGroup(
      Long issuerBankId, Long blackListIpGroupId) {
        return ipGroupDao
          .findById(blackListIpGroupId)
          .map(
            entity -> {
                BlackListIpGroupDTO dto = new BlackListIpGroupDTO();
                dto.setId(entity.getId());
                dto.setIssuerBankId(entity.getIssuerBankId());
                dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
                dto.setCreator(entity.getCreator());
                dto.setUpdater(entity.getUpdater());
                return dto;
            });
    }

    public Optional<BlackListMerchantUrlDTO> findOneBlackListMerchantUrl(
      Long issuerBankId, Long blackListMerchantUrlId) {
        return merchantUrlDao
          .findById(blackListMerchantUrlId)
          .map(
            entity -> {
                BlackListMerchantUrlDTO dto = new BlackListMerchantUrlDTO();
                dto.setId(entity.getId());
                dto.setIssuerBankId(entity.getIssuerBankId());
                dto.setMerchantUrl(entity.getUrl());
                dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
                dto.setCreator(entity.getCreator());
                dto.setUpdater(entity.getUpdater());
                return dto;
            });
    }

    /**
     * 新增黑名單卡號 - 手動輸入卡號
     */
    public BlackListPanDTO addBlackListPanByManual(BlackListPanDTO reqDto) {
        TransStatus transStatus =
          getTransStatus(reqDto.getIssuerBankId(), BlackListAuthStatusCategory.BLACK_LIST_PAN);
        Long issuerBankId = reqDto.getIssuerBankId();
        String cardNumber = reqDto.getCardNumber();
        String user = reqDto.getCreator();
        try {
            // 檢查是否有BinRange資料
            if (!cardImportValidator.isBinRangeExists(reqDto.getIssuerBankId())) {
                throw new OceanException(ResultStatus.NO_SELF_BANK_BIN_RANGE, "No Bin-Range data");
            }
            // 檢查卡號是否符合自行的BinRange範圍之中
            String notInBinRangeCard =
              cardImportValidator.extractNotExistsInBinRangeCard(
                reqDto.getIssuerBankId(), cardNumber);
            if (StringUtils.isNotEmpty(notInBinRangeCard)) {
                throw new OceanException(
                  ResultStatus.NO_SELF_BANK_BIN_RANGE,
                  "Does not exists in Bin-Range:" + notInBinRangeCard);
            }
            Long singleAddedBatchId = panBatchDao.getSingleAddedBatchId(issuerBankId);
            // 卡號是否已存在於黑名單中
            if (cardImportValidator.isCardNumberExistedInBlackList(
              issuerBankId, cardNumber, singleAddedBatchId)) {
                log.warn("[addBlackListPanByManual] Duplicate cardNumber={}",
                  StringUtils.normalizeSpace(MaskUtils.acctNumberMask(cardNumber)));
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
                  "Duplicate cardNumber.");
            }
            // 卡號是否已存在於其他間銀行
            if (cardImportValidator.isCardNumberExistedInOtherBank(issuerBankId, cardNumber)) {
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
                  "The card number exists in other banks.");
            }

            String cardBrand =
              binRangeDao.findCardBrandByIssuerBankIdAndCardNumberWithoutPadding(
                issuerBankId, cardNumber);

            String maskCardNumber = MaskUtils.acctNumberMask(cardNumber);
            String encryptCardNumber =
              hsmPlugin.encryptWithIssuerBankId(cardNumber, issuerBankId).getBase64();
            String hashCardNumber = HmacUtils.encrypt(cardNumber, EnvironmentConstants.hmacHashKey);

            PanInfoDO panInfo =
              panInfoDAO.findOrCreatePanInfo(
                issuerBankId,
                cardBrand,
                maskCardNumber,
                encryptCardNumber,
                hashCardNumber,
                user);

            return blackListPanDAO
              .add(panInfo, singleAddedBatchId, user, transStatus)
              .map(blackPan -> BlackListPanDTO.valueOf(panInfo, blackPan))
              .orElse(reqDto);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        } catch (HmacException e) {
            throw new OceanException(ResultStatus.HMAC_ERROR, e.getMessage());
        }
    }

    public BlackListPanDTO updateBlackListPan(BlackListPanDTO reqDto) {
        try {
            BlackListPanDO entity =
              blackListPanDAO
                .findById(reqDto.getId())
                .orElseThrow(() -> new NoSuchDataException("Id=" + reqDto.getId() + " not found."));
            entity.setAuditStatus(reqDto.getAuditStatus().getSymbol());
            entity.setUpdater(reqDto.getUpdater());
            entity.setUpdateMillis(System.currentTimeMillis());
            entity = blackListPanDAO.saveOrUpdate(entity);
            reqDto.setId(entity.getId());
            return reqDto;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public List<BlackListQueryResultDTO> findByIds(List<Long> ids)
      throws OceanException {
        try {
            List<BlackListQueryResultDTO> dataList = blackListPanDAO.queryByBlackListId(ids).
              stream().map(BlackListQueryResultDTO::valueOf).collect(Collectors.toList());
            appendExtraData(dataList);
            return dataList;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DeleteDataDTO deletePanById(DeleteDataDTO deleteDto) throws OceanException {
        try {
            BlackListPanDO entity =
              blackListPanDAO.findById(deleteDto.getId()).orElseThrow(
                () -> new NoSuchDataException("Id=" + deleteDto.getId() + " not found."));
            entity.setDeleteFlag(true);
            entity.setDeleter(deleteDto.getUser());
            entity.setDeleteMillis(System.currentTimeMillis());
            entity = blackListPanDAO.saveOrUpdate(entity);
            deleteDto.setId(entity.getId());
            return deleteDto;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public void exportBlackListPanCsvByQuery(List<BlackListQueryResultDTO> dataList, String timeZone)
      throws IOException {
        exportBlackListPanCSV(dataList, timeZone);
    }

    public void exportBlackListPanCsvByIds(List<BlackListQueryResultDTO> dataList, String timeZone)
      throws IOException {
        exportBlackListPanCSV(dataList, timeZone);
    }

    private void exportBlackListPanCSV(List<BlackListQueryResultDTO> dataList, String timeZone) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> columnNames = getBlacklistPanCsvColumnNames();
        ExcelBuildUtils.createHeader(workbook, sheet, columnNames);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of(timeZone)));

        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            BlackListQueryResultDTO panDto = dataList.get(rowNum - 1);
            Date date = new Date(panDto.getCreateMillis());
            String createDateTime = sdf.format(date);

            row.createCell(0).setCellValue(createDateTime);
            row.createCell(1).setCellValue(panDto.getCardNumber());
            row.createCell(2).setCellValue(panDto.getBlockTimes());
        }
        ExcelBuildUtils.resizeColums(sheet, columnNames.size());
        String fileName =
            ExcelBuildUtils.getFileNameFormat(
                MessageConstants.get(
                    MessageConstants.EXCEL_BLACK_LIST,
                    MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_PAN)));
        ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
    }

    public DeleteDataDTO deleteIpByIpGroupIds(DeleteDataDTO deleteDto) {
        DeleteDataDO deleteDataDO = DeleteDataDO.builder()
          .id(deleteDto.getId()).issuerBankId(deleteDto.getIssuerBankId())
          .fileName(deleteDto.getFileName()).fileContent(deleteDto.getFileContent())
          .auditStatus(deleteDto.getAuditStatus()).version(deleteDto.getVersion())
          .user(deleteDto.getUser()).build();
        ipGroupDao.deleteByIds(deleteDataDO);
        return deleteDto;
    }

    public PagingResultDTO<BlackListIpGroupDTO> queryIp(BlackListIpQueryDTO queryDto) {
        PagingResultDTO<BlackListIpGroupDTO> queryResult = ipManager.query(queryDto);
        List<BlackListIpGroupDTO> data = queryResult.getData().stream()
          .peek(this::convertIp6To4)
          .collect(Collectors.toList());
        queryResult.setData(data);
        return queryResult;
    }

    public List<BlackListIpGroupDTO> queryIpGroupByIds(IdsQueryDTO queryDto) {
        return ipManager.findByIds(queryDto).stream()
            .peek(this::convertIp6To4)
            .collect(Collectors.toList());
    }

    private void convertIp6To4(BlackListIpGroupDTO dto) {
        boolean isOriginVersionIpv4 = 4 == dto.getOriginVersion();
        if (isOriginVersionIpv4 && null == dto.getCidr()) {
            dto.setIp(dto.getIp());
        } else if (isOriginVersionIpv4) {
            String ipv6Cidr =
                IpUtils.ip6To4(String.format("%s/%d", dto.getIp(), dto.getCidr()));
            String[] ipv6Array = ipv6Cidr.split("/");
            dto.setIp(ipv6Array[0]);
            dto.setCidr(Integer.parseInt(ipv6Array[1]));
        }
    }

    /**
     * 新增黑名單 by IP
     */
    public BlackListIpGroupDTO addBlackListIp(BlackListIpGroupDTO createDto) {
        // 是否已存在重複的IP
        String checkResult =
          isDuplicateIpRange(createDto.getIssuerBankId(), createDto.getIp(), createDto.getCidr());
        if (!checkResult.isEmpty()) {
            String errMsg =
              String.format(
                "The IP/CIDR:%s/%d, in range %s",
                createDto.getIp(), createDto.getCidr(), checkResult);
            throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, errMsg);
        }

        TransStatus transStatus =
          getTransStatus(createDto.getIssuerBankId(), BlackListAuthStatusCategory.BLACK_LIST_IP);

        PortalBlackListIpGroupDO blackListIpGroupDO = PortalBlackListIpGroupDO.builder()
          .id(createDto.getId())
          .issuerBankId(createDto.getIssuerBankId())
          .originVersion(createDto.getOriginVersion())
          .ip(createDto.getIp())
          .cidr(createDto.getCidr())
          .blockTimes(createDto.getBlockTimes())
          .auditStatus(createDto.getAuditStatus())
          .creator(createDto.getCreator())
          .createMillis(createDto.getCreateMillis())
          .updater(createDto.getUpdater())
          .build();
        Long ipGroupId = ipGroupDao.create(blackListIpGroupDO, transStatus, createDto.getCreator())
          .getId();
        createDto.setId(ipGroupId);
        return createDto;
    }

    public BlackListIpGroupDTO updateBlackListIp(BlackListIpGroupDTO reqDto) {
        BlackListIpGroupDO entity = ipGroupDao.findById(reqDto.getId())
          .orElseThrow(() -> new NoSuchDataException("Id=" + reqDto.getId() + " not found."));
        entity.setAuditStatus(reqDto.getAuditStatus().getSymbol());
        entity.setUpdater(reqDto.getUpdater());
        entity.setUpdateMillis(System.currentTimeMillis());
        entity = ipGroupDao.update(entity);
        reqDto.setId(entity.getId());
        return reqDto;
    }

    public BlackListMerchantUrlDTO updateBlackListMerchantUrl(BlackListMerchantUrlDTO reqDto) {
        BlackListMerchantUrlDO entity = merchantUrlDao.findById(reqDto.getId())
          .orElseThrow(() -> new NoSuchDataException("Id=" + reqDto.getId() + " not found."));
        entity.setAuditStatus(reqDto.getAuditStatus().getSymbol());
        entity.setUpdater(reqDto.getUpdater());
        entity.setUpdateMillis(System.currentTimeMillis());
        entity = merchantUrlDao.update(entity);
        reqDto.setId(entity.getId());
        return reqDto;
    }

    public void exportBlackListIpCsv(List<BlackListIpGroupDTO> dataList, String timeZone) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> columnNames = getBlacklistIpCsvColumnNames();
        ExcelBuildUtils.createHeader(workbook, sheet, columnNames);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of(timeZone)));

        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            BlackListIpGroupDTO ipGroupDto = dataList.get(rowNum - 1);
            Date date = new Date(ipGroupDto.getCreateMillis());
            String createDateTime = sdf.format(date);
            String name = String.format("%s/%d", ipGroupDto.getIp(), ipGroupDto.getCidr());
            row.createCell(0).setCellValue(createDateTime);
            row.createCell(1).setCellValue(name);
            row.createCell(2).setCellValue(ipGroupDto.getBlockTimes());
        }
        ExcelBuildUtils.resizeColums(sheet, columnNames.size());
        String fileName =
            ExcelBuildUtils.getFileNameFormat(
                MessageConstants.get(
                    MessageConstants.EXCEL_BLACK_LIST,
                    "IP"));
        ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
    }

    public DeleteDataDTO deleteMerchantUrlById(DeleteDataDTO deleteDto) {
        merchantUrlDao.deleteById(DeleteDataDO.builder()
          .user(deleteDto.getUser())
          .version(deleteDto.getVersion())
          .auditStatus(deleteDto.getAuditStatus())
          .fileContent(deleteDto.getFileContent())
          .fileName(deleteDto.getFileName())
          .issuerBankId(deleteDto.getIssuerBankId())
          .id(deleteDto.getId())
          .build());
        return deleteDto;
    }

    public PagingResultDTO<BlackListMerchantUrlResDTO> queryMerchantUrl(
      BlackListMerchantUrlQueryDTO queryDto) {
        PagingResultDO<BlackListMerchantUrlDO> queryResult = merchantUrlDao
          .findByUrlLike(BlackListMerchantUrlQueryDO.builder()
            .issuerBankId(queryDto.getIssuerBankId())
            .startTime(queryDto.getStartTime()).endTime(queryDto.getEndTime())
            .merchantUrl(queryDto.getMerchantUrl())
            .page(queryDto.getPage()).pageSize(queryDto.getPageSize()).build());

        List<BlackListMerchantUrlResDTO> newData =
          queryResult.getData().stream()
            .map(BlackListMerchantUrlResDTO::valueOf)
            .collect(Collectors.toList());

        PagingResultDTO<BlackListMerchantUrlResDTO> response = new PagingResultDTO<>();
        response.setData(newData);
        response.setTotalPages(queryResult.getTotalPages());
        response.setTotal(queryResult.getTotal());
        response.setCurrentPage(queryResult.getCurrentPage());
        return response;
    }

    public List<BlackListMerchantUrlResDTO> queryMerchantUrl(IdsQueryDTO queryDto) {
        PageRequest page = PageRequest.of(0, 1000, Sort.Direction.DESC, "createMillis");
        List<BlackListMerchantUrlDO> queryResult = merchantUrlDao.findByIds(IdsQueryDO.builder()
          .issuerBankId(queryDto.getIssuerBankId()).ids(queryDto.getIds()).build(), page);

        List<BlackListMerchantUrlResDTO> dtoList =
          queryResult.stream().map(BlackListMerchantUrlResDTO::valueOf)
            .collect(Collectors.toList());
        return dtoList;
    }

    /**
     * 檢查merchantUrl是否存在
     */
    public boolean isMerchantUrlExist(Long issuerBankId, String merchantUrl) {
        return merchantUrlDao.isUrlExist(issuerBankId, merchantUrl);
    }

    /**
     * 新增黑名單 by Merchant URL
     */
    public BlackListMerchantUrlDTO addBlackListMerchantUrl(BlackListMerchantUrlDTO createDto) {
        if (isMerchantUrlExist(createDto.getIssuerBankId(), createDto.getMerchantUrl())) {
            throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, "Duplicate url");
        }

        TransStatus transStatus = getTransStatus(
          createDto.getIssuerBankId(), BlackListAuthStatusCategory.BLACK_LIST_MERCHANT_URL);
        BlackListMerchantUrlDO blackListMerchantUrlDO =
          merchantUrlDao.create(PortalBlackListMerchantUrlDO.builder()
            .id(createDto.getId()).issuerBankId(createDto.getIssuerBankId())
            .createMillis(createDto.getCreateMillis()).creator(createDto.getCreator())
            .updater(createDto.getUpdater())
            .auditStatus(createDto.getAuditStatus())
            .fileName(createDto.getFileName())
            .fileContent(createDto.getFileContent())
            .merchantUrl(createDto.getMerchantUrl())
            .version(createDto.getVersion()).build(), transStatus);
        return BlackListMerchantUrlDTO.valueOf(blackListMerchantUrlDO);
    }

    public void exportBlackListMerchantUrlCsv(List<BlackListMerchantUrlResDTO> dataList, String timeZone)
      throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> columnNames = getBlacklistMerchantCsvColumnNames();
        ExcelBuildUtils.createHeader(workbook, sheet, columnNames);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of(timeZone)));

        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            BlackListMerchantUrlResDTO merchantUrl = dataList.get(rowNum - 1);
            Date date = new Date(merchantUrl.getCreateMillis());
            String createDateTime = sdf.format(date);
            row.createCell(0).setCellValue(createDateTime);
            row.createCell(1).setCellValue(merchantUrl.getMerchantUrl());
        }
        ExcelBuildUtils.resizeColums(sheet, columnNames.size());
        String fileName =
            ExcelBuildUtils.getFileNameFormat(
                MessageConstants.get(
                    MessageConstants.EXCEL_BLACK_LIST,
                    MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_MERCHANT_URL)));
        ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
    }

    public List<BlackListPanBatchDTO> findBatchNameList(Long issuerBankId) throws OceanException {
        List<BatchImportDO> list;
        try {
            list = panBatchDao.findBatchNameList(issuerBankId);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(BlackListPanBatchDTO::valueOf).collect(Collectors.toList());
    }

    /**
     * 加上黑名單資訊
     */
    private void appendExtraData(List<BlackListQueryResultDTO> resultDtoList) {
        if (resultDtoList == null) {
            return;
        }
        addBlockTimesByPan(resultDtoList);
    }

    /**
     * 阻擋次數
     */
    private List<BlackListQueryResultDTO> addBlockTimesByPan(
      final List<BlackListQueryResultDTO> allBlackList) {
        final List<Integer> aresReasonList =
          Arrays.asList(ResultStatus.BLACK_PAN_C.getCode(), ResultStatus.BLACK_PAN_N.getCode());
        // 去除重複卡號
        final List<BlackListQueryResultDTO> noDuplicationList =
          allBlackList.stream()
            .filter(AcsPortalUtil.distinctByKey(BlackListQueryResultDTO::getCardNumberHash))
            .collect(Collectors.toList());

        // 暫存驗證狀態
        Map<String, Integer> tmpTransStatusMap = new HashMap<>();
        noDuplicationList.forEach(
          dto -> {
              Integer blockTimes =
                txLogDao.countByAcctNumberHashAndAresResultReasonCodeIn(
                  dto.getCardNumberHash(), aresReasonList);
              tmpTransStatusMap.put(dto.getCardNumberHash(), blockTimes);
          });
        // 使用cardNumberHash對應出阻擋次數
        allBlackList.forEach(
          bank -> bank.setBlockTimes(tmpTransStatusMap.getOrDefault(bank.getCardNumberHash(), 0)));
        return noDuplicationList;
    }

    public List<BlackListQueryResultDTO> queryBlackListPan(BlackListPanQueryDTO queryDto)
      throws OceanException {
        try {
            BlackListPanQueryDO blackListPanQueryDO = BlackListPanQueryDO.builder()
              .issuerBankId(queryDto.getIssuerBankId())
              .cardBrand(queryDto.getCardBrand())
              .cardNumberHash(hashValue(queryDto.getPan()))
              .startTime(queryDto.getStartTime())
              .endTime(queryDto.getEndTime())
              .build();
            List<BlackListQueryResultDTO> list = blackListPanDAO
              .queryBlackListPan(blackListPanQueryDO)
              .stream().map(BlackListQueryResultDTO::valueOf).collect(Collectors.toList());
            appendExtraData(list);
            return list;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    private String hashValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return HmacUtils.encrypt(value, EnvironmentConstants.hmacHashKey);
        } catch (HmacException e) {
            throw new OceanException(e.getMessage(), e);
        }
    }

    public PagingResultDTO<BlackListQueryResultDTO> queryPaginationBlackListPan(
      BlackListPanQueryDTO queryDto) throws OceanException {
        try {
            PagingResultDO<BlackListQueryResultDO> result =
              blackListPanDAO.queryPaginationBlackListPan(BlackListPanQueryDO.builder()
                .issuerBankId(queryDto.getIssuerBankId())
                .cardBrand(queryDto.getCardBrand())
                .cardNumberHash(hashValue(queryDto.getPan()))
                .page(queryDto.getPage())
                .pageSize(queryDto.getPageSize())
                .startTime(queryDto.getStartTime())
                .endTime(queryDto.getEndTime())
                .build());

            if (result.isEmpty())
                return PagingResultDTO.empty();

            List<BlackListQueryResultDTO> dataList = result.getData()
              .stream().map(BlackListQueryResultDTO::valueOf).collect(Collectors.toList());
            appendExtraData(dataList);

            PagingResultDTO<BlackListQueryResultDTO> pagingResultDTO = new PagingResultDTO<>();
            pagingResultDTO.setData(dataList);
            pagingResultDTO.setTotal(result.getTotal());
            pagingResultDTO.setTotalPages(result.getTotalPages());
            pagingResultDTO.setCurrentPage(result.getCurrentPage());

            return pagingResultDTO;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 以black_list_ip.id 回找 BlackListIpGroupCreateDTO
     */
    public BlackListIpGroupDTO getBlackListIpGroupCreateDTO(Long blackListIpGroupId)
      throws NoSuchDataException {
        BlackListIpGroupDO ipGroup = getBlackListIpGroupByBlackListIpGroupId(blackListIpGroupId)
          .orElseThrow(() -> new NoSuchDataException(
            "[BlackListIpGroup] ID=" + blackListIpGroupId + " not found."));
        return BlackListIpGroupDTO.valueOf(ipGroup);
    }

    /**
     * 以black_list_ip_group.id 查black_list_ip_group
     *
     * @return
     */
    private Optional<BlackListIpGroupDO> getBlackListIpGroupByBlackListIpGroupId(Long groupId) {
        return ipGroupDao.findById(groupId);
    }

    public BlackListDeviceOperationReqDto createBlackListDevice(
      BlackListDeviceOperationReqDto blackListDeviceOperationDto) {
        try {
            TransStatus transStatus = authStatusDao
              .getAuthStatus(blackListDeviceOperationDto.getIssuerBankId(),
                BlackListAuthStatusCategory.BLACK_LIST_DEVICE_ID);

            List<BlackListDeviceInfoDTO> blackListDevices =
              blackListDeviceInfoDao
                .findByKernelTransactionLogIds(blackListDeviceOperationDto.getIds())
                .stream().map(BlackListDeviceInfoDTO::valueOf).collect(Collectors.toList());

            if (!blackListDevices.isEmpty()) {
                log.debug("[addBlackListDeviceSettings] Create Black List Device");

                List<String> deviceIds = new ArrayList<String>();
                List<BlackListDeviceInfoDO> entities = new ArrayList<BlackListDeviceInfoDO>();
                for (BlackListDeviceInfoDTO dto : blackListDevices) {
                    if (null == dto.getDeviceId() || dto.getDeviceId().trim().length() == 0) {
                        log.debug(
                          "[addBlackListDeviceSettings] Transaction(tx_log_id:{}) doesn't have device-ID",
                          dto.getId());
                        continue;
                    }

                    if (dto.getIssuerBankId().longValue() != blackListDeviceOperationDto
                            .getIssuerBankId().longValue()) {
                        throw new IllegalArgumentException("invalid issuer_bank_id");
                    }
                    
                    if (deviceIds.contains(dto.getDeviceId())) {
                        continue;
                    } else {
                        boolean isExist = blackListDeviceInfoDao.existsByIssuerBankIdAndDeviceIDAndNotDelete(
                            dto.getIssuerBankId(), dto.getDeviceId());
                        if (isExist) {
                            log.debug(
                              "[addBlackListDeviceSettings] Transaction(tx_log_id:{})'s device-ID has been added to setting table",
                              dto.getId());
                            continue;
                        } else {
                            deviceIds.add(dto.getDeviceId());
                        }
                    }
                    
                    BlackListDeviceInfoDO deviceEntity = new BlackListDeviceInfoDO();
                    deviceEntity.setIssuerBankId(blackListDeviceOperationDto.getIssuerBankId());
                    deviceEntity.setDeviceID(dto.getDeviceId());
                    deviceEntity.setDeviceChannel(dto.getDeviceChannel());
                    deviceEntity.setBrowserUserAgent(dto.getBrowserUserAgent());
                    deviceEntity.setIp(dto.getIp());
                    deviceEntity.setTransStatus(transStatus.getCode());
                    deviceEntity.setCreator(blackListDeviceOperationDto.getUser());
                    deviceEntity.setCreateMillis(System.currentTimeMillis());
                    deviceEntity
                      .setAuditStatus(blackListDeviceOperationDto.getAuditStatus().getSymbol());
                    entities.add(deviceEntity);
                }

                if (!entities.isEmpty()) {
                    blackListDeviceInfoDao.saveAll(entities);
                }
            }

            return blackListDeviceOperationDto;

        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public PagingResultDTO<BlackListDeviceInfoDTO> getBlackListDeviceInfo(
      BlackListDeviceIdQueryDTO queryDto) {

        PagingResultDO<ComplexBlackListDeviceDO> resultDO =
          blackListDeviceInfoDao.query(BlackListDeviceIdQueryDO.builder()
            .deviceChannels(queryDto.getDeviceChannels()).issuerBankId(queryDto.getIssuerBankId())
            .startTime(queryDto.getStartTime()).endTime(queryDto.getEndTime())
            .page(queryDto.getPage()).pageSize(queryDto.getPageSize())
            .build());
        List<BlackListDeviceInfoDTO> list = resultDO.getData().stream()
          .map(BlackListDeviceInfoDTO::valueOf).collect(Collectors.toList());
        PagingResultDTO<BlackListDeviceInfoDTO> pagingResultDTO = new PagingResultDTO<>();
        pagingResultDTO.setData(list);
        pagingResultDTO.setTotal(resultDO.getTotal());
        pagingResultDTO.setTotalPages(resultDO.getTotalPages());
        pagingResultDTO.setCurrentPage(resultDO.getCurrentPage());
        return pagingResultDTO;
    }

    public List<BlackListDeviceInfoDTO> queryDeviceInfoByIds(IdsQueryDTO queryDto) {
        return blackListDeviceInfoDao.findByIds(
          IdsQueryDO.builder()
            .ids(queryDto.getIds())
            .issuerBankId(queryDto.getIssuerBankId())
            .build()
        ).stream().map(BlackListDeviceInfoDTO::valueOf).collect(Collectors.toList());
    }

    public void exportBlackListDeviceIdCsv(List<BlackListDeviceInfoDTO> dataList, String timeZone)
      throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> columnNames = getBlacklistDeviceIdCsvColumnNames();
        ExcelBuildUtils.createHeader(workbook, sheet, columnNames);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of(timeZone)));

        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            BlackListDeviceInfoDTO deviceIdDto = dataList.get(rowNum - 1);
            Date date = new Date(deviceIdDto.getCreateMillis());
            String createDateTime = sdf.format(date);
            String deviceChannel = DeviceChannel.codeOf(deviceIdDto.getDeviceChannel()).name();
            row.createCell(0).setCellValue(deviceIdDto.getDeviceId());
            row.createCell(1).setCellValue(createDateTime);
            row.createCell(2).setCellValue(deviceIdDto.getIp());
            row.createCell(3).setCellValue(deviceChannel);
            row.createCell(4).setCellValue(deviceIdDto.getBrowserUserAgent());
            row.createCell(5).setCellValue(deviceIdDto.getBlockTimes());
        }

        ExcelBuildUtils.resizeColums(sheet, columnNames.size());
        String fileName =
            ExcelBuildUtils.getFileNameFormat(
                MessageConstants.get(
                    MessageConstants.EXCEL_BLACK_LIST,
                    MessageConstants.get(MessageConstants.EXCEL_BLACK_LIST_DEVICE_ID)));
        ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
    }

    public DeleteDataDTO deleteBlackListDevice(DeleteDataDTO deleteDto) {
        blackListDeviceInfoDao.deleteByIds(deleteDto.getId(), deleteDto.getUser());
        return deleteDto;
    }

}
