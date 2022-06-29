package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.controller.request.BlackListDeviceIdQueryDTO;
import com.cherri.acs_portal.controller.request.BlackListDeviceIdQueryExportDTO;
import com.cherri.acs_portal.controller.request.BlackListDeviceOperationReqDto;
import com.cherri.acs_portal.controller.request.BlackListIpGroupOperationReqDTO;
import com.cherri.acs_portal.controller.request.BlackListIpQueryDTO;
import com.cherri.acs_portal.controller.request.BlackListIpQueryExportDTO;
import com.cherri.acs_portal.controller.request.BlackListMerchantUrlOperationReqDTO;
import com.cherri.acs_portal.controller.request.BlackListPanOperationReqDTO;
import com.cherri.acs_portal.controller.request.BlackListPanQueryDTO;
import com.cherri.acs_portal.controller.request.BlackListPanQueryExportDTO;
import com.cherri.acs_portal.controller.request.ClassicRbaReportReqDto;
import com.cherri.acs_portal.controller.request.ClassicRbaSettingUpdateReqDto;
import com.cherri.acs_portal.controller.response.BlackListIpGroupResDTO;
import com.cherri.acs_portal.controller.response.BlackListMerchantUrlResDTO;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.controller.validator.BatchImportFileValidator;
import com.cherri.acs_portal.dto.ApiPageResponse;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.input.BatchImportDTO;
import com.cherri.acs_portal.dto.blackList.input.BatchQueryDTO;
import com.cherri.acs_portal.dto.blackList.input.BlackListAuthStatusUpdateDTO;
import com.cherri.acs_portal.dto.blackList.input.BlackListMerchantUrlQueryDTO;
import com.cherri.acs_portal.dto.blackList.input.BlackListMerchantUrlQueryExportDTO;
import com.cherri.acs_portal.dto.blackList.input.WhiteListPanCreateDTO;
import com.cherri.acs_portal.dto.blackList.input.WhiteListPanQueryDTO;
import com.cherri.acs_portal.dto.blackList.input.WhiteListPanQueryExportDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListAuthStatusDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListIpGroupDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListMerchantUrlDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListPanBatchDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListPanDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListQueryResultDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.dto.whitelist.WhiteListQueryResult;
import com.cherri.acs_portal.facade.ClassicRbaFacade;
import com.cherri.acs_portal.facade.RiskFacade;
import com.cherri.acs_portal.util.FileUtils;
import com.cherri.acs_portal.util.IpUtils;
import com.cherri.acs_portal.util.StringCustomizedUtils;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequestMapping("/auth/risk")
public class RiskController extends ContextProvider {

    private final RiskFacade riskFacade;
    private final ClassicRbaFacade classicRbaFacade;

    @Autowired
    public RiskController(RiskFacade riskFacade, ClassicRbaFacade classicRbaFacade) {
        this.riskFacade = riskFacade;
        this.classicRbaFacade = classicRbaFacade;
    }

    /**
     * 取得黑名單驗證狀態列表
     */
    @GetMapping("/blacklist/auth-status-list/{issuerBankId}")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_AUTH_STATUS_LIST)
    public ApiResponse authStatusList(@PathVariable Long issuerBankId) {
        List<BlackListAuthStatusDTO> result = riskFacade.getAuthStatusList(issuerBankId);
        return new ApiResponse<>(result);
    }

    /**
     * 更新黑名單驗證狀態
     */
    @PostMapping("/blacklist/auth-status/update")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#updateDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_AUTH_STATUS_UPDATE)
    public ApiResponse authStatusList(@Valid @RequestBody BlackListAuthStatusUpdateDTO updateDto) {
        BlackListAuthStatusDTO serviceDto = new BlackListAuthStatusDTO(updateDto, getUserAccount());
        DataEditResultDTO result = riskFacade.updateAuthStatus(serviceDto);
        return new ApiResponse<>(result);
    }

    /**
     * 查詢黑名單 - 卡號 - 取得 黑名單新增方式 下拉選單資料
     */
    @GetMapping("/blacklist/pan/batch-name-list/{issuerBankId}")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_PAN_LIST)
    public ApiResponse getBatchNames(@PathVariable Long issuerBankId) {
        List<BlackListPanBatchDTO> result = riskFacade.getBatchNameList(issuerBankId);
        return new ApiResponse<>(result);
    }

    /**
     * 查詢黑名單 - 卡號
     */
    @PostMapping("/blacklist/pan/query")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_PAN_LIST)
    public ApiPageResponse<BlackListQueryResultDTO> queryBlackListPanList(
      @Valid @RequestBody BlackListPanQueryDTO queryDto) {
        if (queryDto.getStartTime() == null || queryDto.getStartTime() <= 0) {
            queryDto.setStartTime(0L);
        }
        if (queryDto.getEndTime() == null || queryDto.getEndTime() <= 0) {
            queryDto.setEndTime(Long.MAX_VALUE);
        }
        if (queryDto.getPage() == null || queryDto.getPage() <= 0) {
            queryDto.setPage(1);
        }
        if (queryDto.getPageSize() == null || queryDto.getPageSize() <= 0) {
            queryDto.setPageSize(20);
        }
        return new ApiPageResponse<>(riskFacade.queryPaginationBlackList(queryDto));
    }

    /**
     * 新增黑名單 - 卡號 - 手動輸入卡號
     */
    @PostMapping("/blacklist/card-number")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#createDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_PAN_CREATE_BY_MANUAL)
    public ApiResponse addBlackListByManual(
      @Valid @RequestBody BlackListPanOperationReqDTO createDto) {
        BlackListPanDTO serviceDto = new BlackListPanDTO(createDto);
        serviceDto.setCreator(getUserAccount());
        return new ApiResponse<>(riskFacade.addBlackListPan(serviceDto));
    }

    /**
     * 匯出xls - 卡號 - 查詢條件（依查詢詢條件全部匯出）
     */
    @PostMapping("/blacklist/pan/query/export/xls")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDTO.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_PAN_EXPORT)
    public void exportBlackListPanCsvByQuery(@Valid @RequestBody BlackListPanQueryExportDTO queryDTO) {
        if (queryDTO.getStartTime() == null || queryDTO.getStartTime() <= 0) {
            queryDTO.setStartTime(0L);
        }
        if (queryDTO.getEndTime() == null || queryDTO.getEndTime() <= 0) {
            queryDTO.setEndTime(Long.MAX_VALUE);
        }

        List<BlackListQueryResultDTO> dataList =
          riskFacade.queryBlackListPan(queryDTO);
        riskFacade.exportBlackListPanCSVByQuery(dataList, queryDTO.getTimeZone());
    }

    /**
     * 匯出xls - 卡號IDs （匯出勾選的黑名單卡號)
     */
    @PostMapping("/blacklist/pan/id/export/xls")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_PAN_EXPORT)
    public void exportPanCsvByID(@Valid @RequestBody IdsQueryDTO queryDto) throws IOException {
        riskFacade.exportBlacklistPanCsvByIds(queryDto);
    }

    /**
     * 刪除黑名單 - 卡號
     */
    @PostMapping("/blacklist/pan/delete")
    @Secured("ROLE_BLACK_LIST_MODIFY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_DELETE)
    public ApiResponse deleteBackListPan(@Valid @RequestBody DeleteDataDTO deleteDto) {
        deleteDto.setUser(getUserAccount());
        return new ApiResponse<>(riskFacade.deleteBlackListPan(deleteDto));
    }

    /**
     * 查詢黑名單 - IP
     */
    @PostMapping("/blacklist/ip/query")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_IP_LIST)
    public ApiPageResponse queryIP(@Valid @RequestBody BlackListIpQueryDTO queryDto) {
        PagingResultDTO<BlackListIpGroupResDTO> result = riskFacade.queryIpGroup(queryDto);
        return new ApiPageResponse<>(result);
    }

    /**
     * 新增黑名單 - IP
     */
    @PostMapping("/blacklist/ip")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_IP_CREATE)
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#createDto.issuerBankId)")
    public ApiResponse addBlackListIp(
      @Valid @RequestBody BlackListIpGroupOperationReqDTO createDto) {
        BlackListIpGroupDTO serviceDto = new BlackListIpGroupDTO(createDto, getUserAccount());
        boolean isIpv4 = IpUtils.isIPv4(serviceDto.getIp());
        boolean isIpv6 = IpUtils.isIPv6(serviceDto.getIp());
        if (!isIpv4 && !isIpv6) {
            throw new OceanException(ResultStatus.INVALID_FORMAT,
              "Invalid IP:" + serviceDto.getIp());
        }
        serviceDto.setOriginVersion(isIpv4 ? 4 : 6);
        return new ApiResponse<>(riskFacade.addBlackListIp(serviceDto));
    }

    /**
     * 匯出xls - IP - 查詢條件
     */
    @PostMapping("/blacklist/ip/query/export/xls")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_IP_EXPORT)
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    public void exportBlackListIpCsvByQuery(@Valid @RequestBody BlackListIpQueryExportDTO queryDto) {
        if (queryDto.getStartTime() == null || queryDto.getStartTime() <= 0) {
            queryDto.setStartTime(0L);
        }
        if (queryDto.getEndTime() == null || queryDto.getEndTime() <= 0) {
            queryDto.setEndTime(Long.MAX_VALUE);
        }
        riskFacade.exportBlacklistIpCsvByQuery(queryDto);
    }

    /**
     * 匯出xls - IP - Ids
     */
    @PostMapping("/blacklist/ip/id/export/xls")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_IP_EXPORT)
    public void exportBlackListIpCsvByQuery(@Valid @RequestBody IdsQueryDTO queryDto) {
        riskFacade.exportBlacklistIpCsvByIds(queryDto);
    }

    /**
     * 刪除黑名單 - IP
     */
    @PostMapping("/blacklist/ip/delete")
    @Secured("ROLE_BLACK_LIST_MODIFY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_DELETE)
    public ApiResponse deleteBackListIp(@Valid @RequestBody DeleteDataDTO deleteDto) {
        deleteDto.setUser(getUserAccount());
        return new ApiResponse<>(riskFacade.deleteBlackListIp(deleteDto));
    }

    /**
     * 查詢黑名單 - Merchant URL
     */
    @PostMapping("/blacklist/merchant-url/query")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_MERCHANT_URL_LIST)
    public ApiPageResponse queryMerchantUrl(
      @Valid @RequestBody BlackListMerchantUrlQueryDTO queryDto) {
        PagingResultDTO<BlackListMerchantUrlResDTO> result =
          riskFacade.queryMerchantUrl(queryDto);
        return new ApiPageResponse<>(result);
    }

    /**
     * 新增黑名單 - Merchant URL
     */
    @PostMapping("/blacklist/merchant-url")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#createDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_MERCHANT_URL_CREATE)
    public ApiResponse addBlackListMerchantUrl(
      @Valid @RequestBody BlackListMerchantUrlOperationReqDTO createDto) {
        BlackListMerchantUrlDTO serviceDto = new BlackListMerchantUrlDTO(createDto,
          getUserAccount());
        return new ApiResponse<>(riskFacade.addBlackListMerchantUrl(serviceDto));
    }

    /**
     * 匯出xls - Merchant URL - 查詢條件
     */
    @PostMapping("/blacklist/merchant-url/query/export/xls")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_MERCHANT_URL_EXPORT)
    public void exportBlackListMerchantUrlCsvByQuery(
      @Valid @RequestBody BlackListMerchantUrlQueryExportDTO queryDto) {
        if (queryDto.getStartTime() == null || queryDto.getStartTime() <= 0) {
            queryDto.setStartTime(0L);
        }
        if (queryDto.getEndTime() == null || queryDto.getEndTime() <= 0) {
            queryDto.setEndTime(Long.MAX_VALUE);
        }
        riskFacade.exportBlacklistMerchantUrlCsvByQuery(queryDto);
    }

    /**
     * 匯出xls - Merchant URL - Ids
     */
    @PostMapping("/blacklist/merchant-url/id/export/xls")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_MERCHANT_URL_EXPORT)
    public void exportBlackListMerchantUrlCsvById(@Valid @RequestBody IdsQueryDTO queryDto) {
        riskFacade.exportBlacklistMerchantUrlCsvByIds(queryDto);
    }

    /**
     * 刪除黑名單 - Merchant URL
     */
    @PostMapping("/blacklist/merchant-url/delete")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#deleteDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_MERCHANT_URL_CREATE)
    public ApiResponse addBlackListMerchantUrl(@Valid @RequestBody DeleteDataDTO deleteDto) {
        deleteDto.setUser(getUserAccount());
        return new ApiResponse<>(riskFacade.deleteBlackListMerchantUrl(deleteDto));
    }

    /**
     * 查詢白名單 - 卡號
     */
    @PostMapping("/whitelist/pan/query")
    @PreAuthorize(
      "hasRole('ROLE_WHITE_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.WHITE_LIST_PAN_LIST)
    public ApiPageResponse queryWhiteListPans(@Valid @RequestBody WhiteListPanQueryDTO queryDto) {
        if (queryDto.getStartTime() == null || queryDto.getStartTime() <= 0) {
            queryDto.setStartTime(0L);
        }
        if (queryDto.getEndTime() == null || queryDto.getEndTime() <= 0) {
            queryDto.setEndTime(Long.MAX_VALUE);
        }
        PagingResultDTO<WhiteListQueryResult> whitePanResult =
          riskFacade.queryWhiteList(queryDto);
        return new ApiPageResponse<>(whitePanResult);
    }

    /**
     * 匯出xls - 卡號白名單 - 查詢條件
     */
    @PostMapping("/whitelist/pan/query/export/xls")
    @PreAuthorize(
      "hasRole('ROLE_WHITE_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.WHITE_LIST_PAN_EXPORT)
    public void exportWhiteListPanCsvByQuery(@Valid @RequestBody WhiteListPanQueryExportDTO queryDto) {
        if (queryDto.getStartTime() == null || queryDto.getStartTime() <= 0) {
            queryDto.setStartTime(0L);
        }
        if (queryDto.getEndTime() == null || queryDto.getEndTime() <= 0) {
            queryDto.setEndTime(Long.MAX_VALUE);
        }
        PagingResultDTO<WhiteListQueryResult> whitePanResult =
          riskFacade.queryWhiteList(queryDto);
        riskFacade.exportWhiteListCSV(whitePanResult.getData(), queryDto.getTimeZone());
    }

    /**
     * 匯出xls - 卡號白名單 - ID
     */
    @PostMapping("/whitelist/pan/id/export/xls")
    @PreAuthorize(
      "hasRole('ROLE_WHITE_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.WHITE_LIST_PAN_EXPORT)
    public void exportWhiteListPanCsvById(@Valid @RequestBody IdsQueryDTO queryDto) {
        riskFacade.exportWhiteListCSV(queryDto);
    }

    /**
     * 新增白名單 - 卡號
     */
    @PostMapping("/whitelist/pan")
    @PreAuthorize(
      "hasRole('ROLE_WHITE_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#createDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.WHITE_LIST_PAN_CREATE)
    public ApiResponse addWhiteListPan(@Valid @RequestBody WhiteListPanCreateDTO createDto) {
        if (createDto == null
          || StringUtils.isEmpty(createDto.getRealCardNumber())
          || createDto.getIssuerBankId() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "Incorrect arguments");
        }

        createDto.setUser(getUserAccount());
        return new ApiResponse<>(riskFacade.addWhiteListPan(createDto));
    }

    /**
     * 刪除白名單
     */
    @PostMapping("/whitelist/delete")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.WHITE_LIST_PAN_DELETE)
    @PreAuthorize(
      "hasRole('ROLE_WHITE_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#deleteDto.issuerBankId)")
    public ApiResponse deleteWhiteList(@Valid @RequestBody DeleteDataDTO deleteDto) {
        deleteDto.setUser(getUserAccount());
        return new ApiResponse<>(riskFacade.deleteWhiteListById(deleteDto));
    }

    /**
     * 新增黑名單批次 - 卡號
     */
    @PostMapping("/blacklist/batch/pan")
    @PreAuthorize(
      "hasRole('ROLE_BLACK_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_PAN_IMPORT_CREATE)
    public ApiResponse addPanBlackListBatch(
      @RequestParam("panList") MultipartFile batchImportFile,
      @RequestParam Long issuerBankId) {

        byte[] batchFileContent;
        try {
            batchFileContent = IOUtils.toByteArray(batchImportFile.getInputStream());
        } catch (IOException e) {
            log.warn(
              "[addPanBlackListBatch] Failed in read attachment when import blacklist by card number.",
              e);
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "Missing attachment.");
        }
        // Verify batch file content
        BatchImportFileValidator.valid(batchFileContent);

        return new ApiResponse<>(
          riskFacade.importPanBlackList(
            batchFileContent,
            issuerBankId,
            getUserAccount()));
    }

    /**
     * 新增黑名單批次 - 卡號
     */
    @PostMapping("/blacklist/batch/pan/delete")
    @PreAuthorize(
        "hasRole('ROLE_BLACK_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_PAN_IMPORT_DELETE)
    public ApiResponse deletePanBlackListBatch(
        @RequestParam("panList") MultipartFile batchImportFile,
        @RequestParam Long issuerBankId) {

        byte[] batchFileContent;
        try {
            batchFileContent = IOUtils.toByteArray(batchImportFile.getInputStream());
        } catch (IOException e) {
            log.warn(
                "[deletePanBlackListBatch] Failed in read attachment when import blacklist by card number.",
                e);
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "Missing attachment.");
        }
        // Verify batch file content
        BatchImportFileValidator.valid(batchFileContent);

        return new ApiResponse<>(
            riskFacade.deletePanBlackList(
                batchFileContent,
                issuerBankId,
                getUserAccount()));
    }

    @GetMapping("/classic-rba/status")
    @Secured("ROLE_CLASSIC_RBA_QUERY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.CLASSIC_RBA_STATUS)
    public ApiResponse classicRbaMonitor() {
        return classicRbaFacade.getClassicRbaStatus()
          .map(ApiResponse::valueOf)
          .orElse(ApiResponse.SUCCESS_API_RESPONSE);
    }

    @GetMapping("/classic-rba/{issuerBankId}")
    @PreAuthorize(
      "hasRole('ROLE_CLASSIC_RBA_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.CLASSIC_RBA_LIST)
    public ApiResponse classicRbaList(@PathVariable("issuerBankId") long issuerBankId) {
        return classicRbaFacade.getClassicRbaSetting(issuerBankId)
          .map(ApiResponse::valueOf)
          .orElse(ApiResponse.SUCCESS_API_RESPONSE);

    }

    /**
     * 更新經典風控模組驗證狀態
     */
    @PostMapping("/classic-rba/update")
    @PreAuthorize(
      "hasRole('ROLE_CLASSIC_RBA_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#updateDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.CLASSIC_RBA_UPDATE)
    public ApiResponse classicRbaUpdate(
      @Valid @RequestBody ClassicRbaSettingUpdateReqDto updateDto) {
        updateDto.setUser(getUserAccount());
        return new ApiResponse<>(classicRbaFacade.updateClassicRbaSetting(updateDto));
    }

    @PostMapping("classic-rba/report")
    @PreAuthorize("hasRole('ROLE_CLASSIC_RBA_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.CLASSIC_RBA_LIST)
    public ApiResponse classicRbaReport(@Valid @RequestBody ClassicRbaReportReqDto queryDto) {

        return new ApiResponse();
    }

    /**
     * 黑名單 - 裝置ID - 新增 (透過交易紀錄查詢加入)
     */
    @PostMapping("/blacklist/device-id/create")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_DEVICE_ID_CREATE_BY_TRANSACTION_LOG)
    @PreAuthorize("hasRole('ROLE_BLACK_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#request.issuerBankId)")
    public ApiResponse createBlackListDevice(
      @Valid @RequestBody BlackListDeviceOperationReqDto request) {
        if (request.getIds() == null || request.getIds().isEmpty()) {
            String errMsg = String.format("ids %s", MessageConstants.get(MessageConstants.COLUMN_NOT_EMPTY));
            throw new OceanException(ResultStatus.COLUMN_NOT_EMPTY, errMsg);
        }

        request.setUser(getUserAccount());
        return new ApiResponse<>(riskFacade.createBlackListDevice(request));
    }

    /**
     * 黑名單 - 裝置ID - 查詢
     */
    @PostMapping("/blacklist/device-id/query")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_DEVICE_ID_LIST)
    @PreAuthorize("hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    public ApiPageResponse getBlackListDeviceInfo(
      @Valid @RequestBody BlackListDeviceIdQueryDTO queryDto) {
        BlackListDeviceIdQueryDTO.valid(queryDto);

        PagingResultDTO result = riskFacade.getBlackListDeviceInfo(queryDto);
        if (result == null) {
            return new ApiPageResponse(
              ResultStatus.SERVER_ERROR, "Failed in query blacklist device-panId list.");
        } else {
            return new ApiPageResponse(result);
        }
    }

    /**
     * 黑名單 - 裝置ID - 匯出CSV (全部的裝置)
     */
    @PostMapping("/blacklist/device-id/query/export/csv")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_DEVICE_ID_EXPORT)
    @PreAuthorize("hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    public void exportBlackListDeviceIdCsvByQuery(
      @Valid @RequestBody BlackListDeviceIdQueryExportDTO queryDto) {
        if (queryDto.getStartTime() == null || queryDto.getStartTime() <= 0) {
            queryDto.setStartTime(0L);
        }

        if (queryDto.getEndTime() == null || queryDto.getEndTime() <= 0) {
            queryDto.setEndTime(Long.MAX_VALUE);
        }

        riskFacade.exportBlacklistDeviceIdCsvByQuery(queryDto);
    }

    /**
     * 黑名單 - 裝置ID - 匯出CSV (勾選的裝置)
     */
    @PostMapping("/blacklist/device-id/id/export/csv")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_DEVICE_ID_EXPORT)
    @PreAuthorize("hasRole('ROLE_BLACK_LIST_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    public void exportBlackListDeviceIdCsvByQuery(@Valid @RequestBody IdsQueryDTO queryDto) {
        riskFacade.exportBlacklistDeviceIdCsvByIds(queryDto);
    }

    /**
     * 黑名單 - 裝置ID - 刪除
     */
    @PostMapping("/blacklist/device-id/delete")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BLACK_LIST_DEVICE_ID_DELETE)
    @PreAuthorize("hasRole('ROLE_BLACK_LIST_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#deleteDto.issuerBankId)")
    public ApiResponse deleteBlackListDevice(@Valid @RequestBody DeleteDataDTO deleteDto) {
        deleteDto.setUser(getUserAccount());
        return new ApiResponse<>(riskFacade.deleteBlackListDevice(deleteDto));
    }

}
