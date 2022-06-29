package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.controller.request.BlackListDeviceIdQueryDTO;
import com.cherri.acs_portal.controller.request.BlackListDeviceIdQueryExportDTO;
import com.cherri.acs_portal.controller.request.BlackListDeviceOperationReqDto;
import com.cherri.acs_portal.controller.request.BlackListIpQueryDTO;
import com.cherri.acs_portal.controller.request.BlackListIpQueryExportDTO;
import com.cherri.acs_portal.controller.request.BlackListPanQueryDTO;
import com.cherri.acs_portal.controller.response.BatchResultDTO;
import com.cherri.acs_portal.controller.response.BlackListIpGroupResDTO;
import com.cherri.acs_portal.controller.response.BlackListMerchantUrlResDTO;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.input.BatchImportDTO;
import com.cherri.acs_portal.dto.blackList.input.BatchQueryDTO;
import com.cherri.acs_portal.dto.blackList.input.BlackListMerchantUrlQueryDTO;
import com.cherri.acs_portal.dto.blackList.input.BlackListMerchantUrlQueryExportDTO;
import com.cherri.acs_portal.dto.blackList.input.WhiteListPanCreateDTO;
import com.cherri.acs_portal.dto.blackList.input.WhiteListPanQueryDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListAuthStatusDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListDeviceInfoDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListIpGroupDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListMerchantUrlDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListPanBatchDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListPanDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListQueryResultDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.dto.whitelist.WhiteListQueryResult;
import com.cherri.acs_portal.service.AuditService;
import com.cherri.acs_portal.service.BatchImportService;
import com.cherri.acs_portal.service.BlackListService;
import com.cherri.acs_portal.service.WhiteListService;
import com.cherri.acs_portal.service.validator.CardImportValidator;
import com.cherri.acs_portal.util.FileUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.commons.utils.MaskUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RiskFacade {

    private final BlackListService blackListService;
    private final WhiteListService whiteListService;
    private final BatchImportService batchImportService;
    private final AuditService auditService;

    private final CardImportValidator cardImportValidator;

    @Autowired
    public RiskFacade(
      BlackListService blackListService,
      WhiteListService whiteListService,
      BatchImportService batchImportService,
      AuditService auditService,
      CardImportValidator cardImportValidator) {
        this.blackListService = blackListService;
        this.whiteListService = whiteListService;
        this.batchImportService = batchImportService;
        this.auditService = auditService;
        this.cardImportValidator = cardImportValidator;
    }

    public List<BlackListAuthStatusDTO> getAuthStatusList(Long issuerBankId) throws OceanException {
        return blackListService.findAuthStatusAll(issuerBankId);
    }

    public DataEditResultDTO updateAuthStatus(BlackListAuthStatusDTO updateDto) {
        try {
            boolean isDemandAudit =
              auditService.isAuditingOnDemand(AuditFunctionType.BLACK_LIST_AUTH_STATUS);
            if (isDemandAudit) {
                return auditService.requestAudit(
                  AuditFunctionType.BLACK_LIST_AUTH_STATUS, AuditActionType.UPDATE, updateDto);
            }
            updateDto.setAuditStatus(AuditStatus.PUBLISHED);
            updateDto = blackListService.updateAuthStatus(updateDto);
            return new DataEditResultDTO(updateDto);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 新增黑名單卡號
     */
    public DataEditResultDTO addBlackListPan(BlackListPanDTO manualPanCreateDTO) {
        try {
            // 檢查是否有BinRange資料
            if (!cardImportValidator.isBinRangeExists(manualPanCreateDTO.getIssuerBankId())) {
                throw new OceanException(ResultStatus.NO_SELF_BANK_BIN_RANGE, "No Bin-Range data");
            }
            // 檢查卡號是否符合自行的BinRange範圍之中
            String notInBinRangeCard =
              cardImportValidator.extractNotExistsInBinRangeCard(
                manualPanCreateDTO.getIssuerBankId(), manualPanCreateDTO.getCardNumber());
            if (StringUtils.isNotEmpty(notInBinRangeCard)) {
                throw new OceanException(
                  ResultStatus.NO_SELF_BANK_BIN_RANGE,
                  "Does not exists in Bin-Range:" + notInBinRangeCard);
            }
            // 卡號是否已存在黑名單
            final String cardNumber = manualPanCreateDTO.getCardNumber();
            boolean existsInBlacklist =
              cardImportValidator.isCardNumberExistedInBlackList(
                manualPanCreateDTO.getIssuerBankId(), cardNumber);
            if (existsInBlacklist) {
                log.warn("[addBlackListPan] Duplicate cardNumber={}",
                    StringUtils.normalizeSpace(MaskUtils.acctNumberMask(cardNumber)));
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
                  "Duplicate cardNumber.");
            }
            // 卡號是否存在於其他銀行
            boolean existsInOtherBank =
              cardImportValidator.isCardNumberExistedInOtherBank(
                manualPanCreateDTO.getIssuerBankId(), cardNumber);
            if (existsInOtherBank) {
                throw new OceanException(
                  ResultStatus.DUPLICATE_DATA_ELEMENT, "The card number exists in other banks.");
            }

            // 資料進入待審核
            boolean isDemandAudit = auditService
              .isAuditingOnDemand(AuditFunctionType.BLACK_LIST_PAN);
            if (isDemandAudit) {
                return auditService.requestAudit(
                  AuditFunctionType.BLACK_LIST_PAN, AuditActionType.ADD, manualPanCreateDTO);
            }
            // 不走審核流程
            manualPanCreateDTO.setAuditStatus(AuditStatus.PUBLISHED);
            BlackListPanDTO result = blackListService.addBlackListPanByManual(manualPanCreateDTO);
            return new DataEditResultDTO(result);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 刪除黑名單卡號
     */
    public DataEditResultDTO deleteBlackListPan(DeleteDataDTO deleteDto) {
        try {
            boolean isDemandAudit = auditService
              .isAuditingOnDemand(AuditFunctionType.BLACK_LIST_PAN);
            if (isDemandAudit) {
                return auditService.requestAudit(
                  AuditFunctionType.BLACK_LIST_PAN, AuditActionType.DELETE, deleteDto);
            }
            deleteDto.setAuditStatus(AuditStatus.PUBLISHED);
            DeleteDataDTO result = blackListService.deletePanById(deleteDto);
            return new DataEditResultDTO(result);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 新增黑名單 by IP
     */
    public DataEditResultDTO addBlackListIp(BlackListIpGroupDTO createDto) {
        try {
            // 是否已存在重複的IP
            String checkResult =
              blackListService.isDuplicateIpRange(
                createDto.getIssuerBankId(), createDto.getIp(), createDto.getCidr());
            if (!checkResult.isEmpty()) {
                String errMsg =
                  String.format(
                    "The IP/CIDR:%s/%d, in range %s",
                    createDto.getIp(), createDto.getCidr(), checkResult);
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, errMsg);
            }

            boolean isDemandAudit = auditService
              .isAuditingOnDemand(AuditFunctionType.BLACK_LIST_IP);
            if (isDemandAudit) {
                return auditService.requestAudit(
                  AuditFunctionType.BLACK_LIST_IP, AuditActionType.ADD, createDto);
            }
            createDto.setAuditStatus(AuditStatus.PUBLISHED);
            BlackListIpGroupDTO result = blackListService.addBlackListIp(createDto);
            return new DataEditResultDTO(result);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 刪除黑名單 by IP
     */
    public DataEditResultDTO deleteBlackListIp(DeleteDataDTO deleteDto) {
        try {
            boolean isDemandAudit = auditService
              .isAuditingOnDemand(AuditFunctionType.BLACK_LIST_IP);
            if (isDemandAudit) {
                return auditService.requestAudit(
                  AuditFunctionType.BLACK_LIST_IP, AuditActionType.DELETE, deleteDto);
            }
            deleteDto.setAuditStatus(AuditStatus.PUBLISHED);
            DeleteDataDTO result = blackListService.deleteIpByIpGroupIds(deleteDto);
            return new DataEditResultDTO(result);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 新增黑名單 by Merchant URL
     */
    public DataEditResultDTO addBlackListMerchantUrl(BlackListMerchantUrlDTO createDto) {
        try {
            if (blackListService.isMerchantUrlExist(
              createDto.getIssuerBankId(), createDto.getMerchantUrl())) {
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, "Duplicate url");
            }

            boolean isDemandAudit =
              auditService.isAuditingOnDemand(AuditFunctionType.BLACK_LIST_MERCHANT_URL);
            if (isDemandAudit) {
                return auditService.requestAudit(
                  AuditFunctionType.BLACK_LIST_MERCHANT_URL, AuditActionType.ADD, createDto);
            }
            createDto.setAuditStatus(AuditStatus.PUBLISHED);
            BlackListMerchantUrlDTO result = blackListService.addBlackListMerchantUrl(createDto);
            return new DataEditResultDTO(result);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 刪除黑名單 by Merchant URL
     */
    public DataEditResultDTO deleteBlackListMerchantUrl(DeleteDataDTO deleteDto) {
        try {
            boolean isDemandAudit =
              auditService.isAuditingOnDemand(AuditFunctionType.BLACK_LIST_MERCHANT_URL);
            if (isDemandAudit) {
                return auditService.requestAudit(
                  AuditFunctionType.BLACK_LIST_MERCHANT_URL, AuditActionType.DELETE, deleteDto);
            }
            deleteDto.setAuditStatus(AuditStatus.PUBLISHED);
            DeleteDataDTO result = blackListService.deleteMerchantUrlById(deleteDto);
            return new DataEditResultDTO(result);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 刪除白名單
     */
    public DataEditResultDTO deleteWhiteListById(DeleteDataDTO deleteDataDTO) {
        try {
            boolean isDemandAudit = auditService.isAuditingOnDemand(AuditFunctionType.WHITE_LIST);
            if (isDemandAudit) {
                return auditService.requestAudit(
                  AuditFunctionType.WHITE_LIST, AuditActionType.DELETE, deleteDataDTO);
            } else {
                deleteDataDTO.setAuditStatus(AuditStatus.PUBLISHED);
                DeleteDataDTO result = whiteListService.deleteById(deleteDataDTO);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 刪除白名單 - 多筆
     */
    public List<DataEditResultDTO> deleteWhiteListByIds(List<DeleteDataDTO> deleteDataDTOList) {
        return deleteDataDTOList.stream()
          .map(whiteListService::deleteById)
          .collect(Collectors.toList())
          .stream()
          .map(DataEditResultDTO::new)
          .collect(Collectors.toList());
    }

    public void exportWhiteListCSV(List<WhiteListQueryResult> resultList, String timeZone) {
        try {
            whiteListService.exportWhiteListPanCsvByQuery(resultList, timeZone);
        } catch (IOException ioe) {
            log.error("[exportWhiteListCSV] Failed in export whitelist csv.", ioe);
        }
    }

    public void exportWhiteListCSV(IdsQueryDTO queryDto) {
        PagingResultDTO<WhiteListQueryResult> resultPage = whiteListService.queryByIds(queryDto);
        try {
            List<WhiteListQueryResult> dataList = resultPage.getData();
            dataList = dataList == null ? Collections.emptyList() : dataList;
            whiteListService.exportWhiteListPanCsvByQuery(dataList, queryDto.getTimeZone());
        } catch (IOException ioe) {
            log.error("[exportWhiteListCSV] Failed in export whitelist csv.", ioe);
        }
    }

    public List<BlackListPanBatchDTO> getBatchNameList(Long issuerBankId) throws OceanException {
        return blackListService.findBatchNameList(issuerBankId);
    }

    public void exportBlackListPanCSVByQuery(List<BlackListQueryResultDTO> resultList, String timeZone) {
        try {
            blackListService.exportBlackListPanCsvByQuery(resultList, timeZone);
        } catch (IOException ioe) {
            log.error("[exportBlackListPanCSVByQuery] Failed in export blacklist csv.", ioe);
        }
    }

    public void exportBlacklistPanCsvByIds(IdsQueryDTO queryDto)
      throws IOException {
        List<Long> ids = queryDto.getIds();
        List<BlackListQueryResultDTO> authStatusPans = blackListService.findByIds(ids);
        blackListService.exportBlackListPanCsvByIds(authStatusPans, queryDto.getTimeZone());
    }

    public PagingResultDTO<BlackListIpGroupResDTO> queryIpGroup(BlackListIpQueryDTO queryDto) {
        PagingResultDTO<BlackListIpGroupDTO> queryResult = blackListService.queryIp(queryDto);
        List<BlackListIpGroupResDTO> data = new ArrayList<>();
        if (queryResult.getData() != null && !queryResult.getData().isEmpty()) {
            data =
              queryResult.getData().stream()
                .map(BlackListIpGroupResDTO::valueOf)
                .collect(Collectors.toList());
        }
        PagingResultDTO<BlackListIpGroupResDTO> response = new PagingResultDTO<>();
        response.setTotalPages(queryResult.getTotalPages());
        response.setTotal(queryResult.getTotal());
        response.setCurrentPage(queryResult.getCurrentPage());
        response.setData(data);
        return response;
    }

    public void exportBlacklistIpCsvByQuery(BlackListIpQueryExportDTO queryDto) {
        try {
            PagingResultDTO<BlackListIpGroupDTO> result = blackListService.queryIp(queryDto);
            if (result != null && result.getData() != null) {
                blackListService.exportBlackListIpCsv(result.getData(), queryDto.getTimeZone());
            }

        } catch (IOException ioe) {
            log.error("[exportBlacklistIpCsvByQuery] Failed in export blacklist csv.", ioe);
        }
    }

    public void exportBlacklistIpCsvByIds(IdsQueryDTO queryDto) {
        try {
            List<BlackListIpGroupDTO> result = blackListService.queryIpGroupByIds(queryDto);
            if (result != null) {
                blackListService.exportBlackListIpCsv(result, queryDto.getTimeZone());
            }

        } catch (IOException ioe) {
            log.error("[exportBlacklistIpCsvByIds] Failed in export blacklist csv.", ioe);
        }
    }

    public PagingResultDTO<BlackListMerchantUrlResDTO> queryMerchantUrl(
      BlackListMerchantUrlQueryDTO queryDto) {
        return blackListService.queryMerchantUrl(queryDto);
    }

    /**
     * 輸出Merchant URL 黑名單csv檔 by 查詢條件
     */
    public void exportBlacklistMerchantUrlCsvByQuery(BlackListMerchantUrlQueryExportDTO queryDto) {
        try {
            queryDto.setPage(1);
            queryDto.setPageSize(EnvironmentConstants.PAGINATION_MAX_ROWS);
            PagingResultDTO<BlackListMerchantUrlResDTO> result =
              blackListService.queryMerchantUrl(queryDto);
            if (result != null && result.getData() != null) {
                blackListService.exportBlackListMerchantUrlCsv(result.getData(), queryDto.getTimeZone());
            }

        } catch (IOException ioe) {
            log
              .error("[exportBlacklistMerchantUrlCsvByQuery] Failed in export blacklist csv.", ioe);
        }
    }

    /**
     * 輸出Merchant URL 黑名單csv檔 by id
     */
    public void exportBlacklistMerchantUrlCsvByIds(IdsQueryDTO queryDto) {
        try {
            List<BlackListMerchantUrlResDTO> result = blackListService.queryMerchantUrl(queryDto);
            if (result != null) {
                blackListService.exportBlackListMerchantUrlCsv(result, queryDto.getTimeZone());
            }

        } catch (IOException ioe) {
            log.error("[exportBlacklistMerchantUrlCsvByIds] Failed in export blacklist csv.", ioe);
        }
    }

    public PagingResultDTO<WhiteListQueryResult> queryWhiteList(WhiteListPanQueryDTO queryDto) {
        return whiteListService.query(queryDto);
    }

    /**
     * 查詢黑名單
     */
    public List<BlackListQueryResultDTO> queryBlackListPan(BlackListPanQueryDTO queryDto) {
        return blackListService.queryBlackListPan(queryDto);
    }

    /**
     * 查詢黑名單 分頁
     */
    public PagingResultDTO<BlackListQueryResultDTO> queryPaginationBlackList(
      BlackListPanQueryDTO queryDto) throws OceanException {
        return blackListService.queryPaginationBlackListPan(queryDto);
    }

    /**
     * 新增白名單
     */
    public DataEditResultDTO addWhiteListPan(WhiteListPanCreateDTO createDto) {
        try {
            final String cardNumber = createDto.getRealCardNumber();
            // 檢查是否有BinRange資料
            if (!cardImportValidator.isBinRangeExists(createDto.getIssuerBankId())) {
                throw new OceanException(ResultStatus.NO_SELF_BANK_BIN_RANGE, "No Bin-Range data");
            }
            // 檢查卡號是否符合自行的BinRange範圍之中
            String notInBinRangeCard =
              cardImportValidator
                .extractNotExistsInBinRangeCard(createDto.getIssuerBankId(), cardNumber);
            if (StringUtils.isNotEmpty(notInBinRangeCard)) {
                throw new OceanException(
                  ResultStatus.NO_SELF_BANK_BIN_RANGE,
                  "Does not exists in Bin-Range:" + notInBinRangeCard);
            }
            // 卡號是否已存在於白名單
            boolean existsInBlacklist =
              cardImportValidator.isCardNumberExistedInWhiteList(
                createDto.getIssuerBankId(), cardNumber);
            if (existsInBlacklist) {
                log.warn("[addWhiteListPan] Duplicate cardNumber={}",
                  StringUtils.normalizeSpace(MaskUtils.acctNumberMask(cardNumber)));
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
                  "Duplicate cardNumber.");
            }
            // 卡號是否存在於其他銀行
            boolean existsInOtherBank =
              cardImportValidator.isCardNumberExistedInOtherBank(
                createDto.getIssuerBankId(), cardNumber);
            if (existsInOtherBank) {
                log.warn(
                  "[addWhiteListPan] Card number exists in other banks, cardNumber={}",
                  StringUtils.normalizeSpace(MaskUtils.acctNumberMask(createDto.getRealCardNumber())));
                throw new OceanException(
                  ResultStatus.DUPLICATE_DATA_ELEMENT, "The card number exists in other banks.");
            }

            // 資料進入待審核
            boolean isDemandAudit = auditService.isAuditingOnDemand(AuditFunctionType.WHITE_LIST);
            if (isDemandAudit) {
                return auditService.requestAudit(
                  AuditFunctionType.WHITE_LIST, AuditActionType.ADD, createDto);
            } else {
                createDto.setAuditStatus(AuditStatus.PUBLISHED);
                WhiteListPanCreateDTO result = whiteListService.addWhiteListPan(createDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public BatchResultDTO importPanBlackList(
      byte[] batchImportFile,
      Long issuerBankId,
      String user) {
        BatchImportDTO importDTO =
            new BatchImportDTO(issuerBankId, null, null, null,
                batchImportFile);
        importDTO.setUser(user);

        importDTO.setAuditStatus(AuditStatus.PUBLISHED);
        long addedCount = batchImportService.addBlackListByBatch(importDTO);
        BatchResultDTO result = new BatchResultDTO();
        result.setAddedCount(addedCount);
        return result;
    }

    public BatchResultDTO deletePanBlackList(
        byte[] batchImportFile,
        Long issuerBankId,
        String user) {
        BatchImportDTO importDTO =
            new BatchImportDTO(issuerBankId, null, null, null,
                batchImportFile);
        importDTO.setUser(user);

        importDTO.setAuditStatus(AuditStatus.PUBLISHED);
        long deletedCount = batchImportService.deleteBlackListByBatch(importDTO);
        BatchResultDTO result = new BatchResultDTO();
        result.setDeletedCount(deletedCount);
        return result;
    }

    public PagingResultDTO<BatchImportDTO> queryPanBlackListBatch(BatchQueryDTO queryDTO) {
        return batchImportService.queryPanBlackListBatch(queryDTO);
    }

    public DataEditResultDTO updatePanBlackListBatch(BatchImportDTO importDTO) {
        try {
            boolean demandAuditing =
              auditService.isAuditingOnDemand(AuditFunctionType.BLACK_LIST_BATCH_IMPORT);

            if (demandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.BLACK_LIST_BATCH_IMPORT, AuditActionType.UPDATE, importDTO);
            } else {
                importDTO.setAuditStatus(AuditStatus.PUBLISHED);
                BatchImportDTO batchImport = batchImportService.updatePanBlackListBatch(importDTO);
                return new DataEditResultDTO(batchImport);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO deleteMultiPanBlackListBatch(DeleteDataDTO deleteDto) {
        try {
            boolean demandAuditing =
              auditService.isAuditingOnDemand(AuditFunctionType.BLACK_LIST_BATCH_IMPORT);

            if (demandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.BLACK_LIST_BATCH_IMPORT, AuditActionType.DELETE, deleteDto);
            }
            deleteDto.setAuditStatus(AuditStatus.PUBLISHED);
            return new DataEditResultDTO(batchImportService.deletePanBlackListBatch(deleteDto));
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO createBlackListDevice(
      BlackListDeviceOperationReqDto blackListDeviceOperationDto) {
        try {
            boolean demandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BLACK_LIST_DEVICE);
            if (demandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.BLACK_LIST_DEVICE, AuditActionType.ADD,
                    blackListDeviceOperationDto);
            } else {
                blackListDeviceOperationDto.setAuditStatus(AuditStatus.PUBLISHED);
                return new DataEditResultDTO(
                  blackListService.createBlackListDevice(blackListDeviceOperationDto));
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public PagingResultDTO<BlackListDeviceInfoDTO> getBlackListDeviceInfo(
      BlackListDeviceIdQueryDTO queryDto) {
        return blackListService.getBlackListDeviceInfo(queryDto);
    }

    public void exportBlacklistDeviceIdCsvByQuery(BlackListDeviceIdQueryExportDTO queryDto) {
        try {
            queryDto.setPage(1);
            queryDto.setPageSize(1000);
            PagingResultDTO<BlackListDeviceInfoDTO> result = blackListService
              .getBlackListDeviceInfo(queryDto);
            if (result != null && result.getData() != null) {
                blackListService.exportBlackListDeviceIdCsv(result.getData(), queryDto.getTimeZone());
            }

        } catch (IOException ioe) {
            log.error("Failed in export blacklist csv.", ioe);
        }
    }

    public void exportBlacklistDeviceIdCsvByIds(IdsQueryDTO queryDto) {
        try {
            List<BlackListDeviceInfoDTO> result = blackListService.queryDeviceInfoByIds(queryDto);
            if (result != null) {
                blackListService.exportBlackListDeviceIdCsv(result, queryDto.getTimeZone());
            }

        } catch (IOException ioe) {
            log.error("Failed in export blacklist csv.", ioe);
        }
    }

    public DataEditResultDTO deleteBlackListDevice(DeleteDataDTO deleteDto) {
        try {
            boolean demandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BLACK_LIST_DEVICE);
            if (demandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.BLACK_LIST_DEVICE, AuditActionType.DELETE,
                    deleteDto);
            } else {
                deleteDto.setAuditStatus(AuditStatus.PUBLISHED);
                return new DataEditResultDTO(blackListService.deleteBlackListDevice(deleteDto));
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

}
