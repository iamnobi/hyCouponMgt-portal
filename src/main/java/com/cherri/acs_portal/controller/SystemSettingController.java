package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.constant.SystemConstants;
import com.cherri.acs_portal.controller.request.KeyImportReqDTO;
import com.cherri.acs_portal.controller.request.KeyImportVerifyReqDTO;
import com.cherri.acs_portal.controller.request.KeyOperationReqDTO;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.controller.response.KeyImportVerifyResDTO;
import com.cherri.acs_portal.controller.response.LanguageResDTO;
import com.cherri.acs_portal.dto.ApiPageResponse;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.system.*;
import com.cherri.acs_portal.facade.SystemSettingFacade;
import com.cherri.acs_portal.model.enumerator.CavvKeyType;
import com.cherri.acs_portal.validator.validation.CardBrandValidation;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.CardType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/** 系統設定Controller */
@RestController
@Log4j2
@Validated
@RequestMapping("/auth/system-setting")
public class SystemSettingController extends ContextProvider {

  private final SystemSettingFacade facade;

  @Autowired
  public SystemSettingController(SystemSettingFacade facade) {
    this.facade = facade;
  }

  /**
   * Mcc Import from csv file
   * @apiNote csv example
   * 1111,2222
   * 3333
   * 4444
   * @return is import success.
   */
  @PostMapping("/mcc/import")
  @PreAuthorize("hasRole('ROLE_GENERAL_SETTING_MODIFY')")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.MCC_IMPORT)
  public ApiResponse<Boolean> importMccFromCsv(
      @RequestParam("mcc_csv") MultipartFile mccCodeCsv) {
    return facade.importMccFromCsv(mccCodeCsv, getUserAccount());
  }

  /**
   * Query all mcc code list
   * @return mcc code list
   */
  @GetMapping("/mcc/query")
  @PreAuthorize("hasRole('ROLE_GENERAL_SETTING_QUERY')")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.MCC_LIST)
  public ApiResponse<List<String>> queryAllMcc() {
   return facade.queryAllMcc();
  }

  @PostMapping("/bin-range/query")
  @PreAuthorize("hasRole('ROLE_SYS_BIN_RANGE_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.BIN_RANGE_LIST)
  public ApiPageResponse queryBinRange(@Valid @RequestBody BinRangeQueryDTO queryDto) {
    queryDto.setCardBrand(SystemConstants.CARD_BRAND);
    PagingResultDTO<BinRangeDTO> binPagingResult = facade.queryBinRange(queryDto);

    if (binPagingResult == null) {
      return new ApiPageResponse(ResultStatus.SERVER_ERROR, "Failed in query bin range.");
    } else {
      return new ApiPageResponse<>(binPagingResult);
    }
  }

  @PostMapping("/bin-range")
  @PreAuthorize("hasRole('ROLE_SYS_BIN_RANGE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#binRangeDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.BIN_RANGE_CREATE)
  public ApiResponse addBinRange(@Valid @RequestBody BinRangeDTO binRangeDTO) {
    binRangeDTO.setCardBrand(SystemConstants.CARD_BRAND);
    binRangeDTO.setCardType(CardType.CREDIT.name());
    binRangeDTO.setUser(getUserAccount());
    return new ApiResponse<>(facade.addBinRange(binRangeDTO));
  }

  @PostMapping("/bin-range/update")
  @PreAuthorize("hasRole('ROLE_SYS_BIN_RANGE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#binRangeDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.BIN_RANGE_UPDATE)
  public ApiResponse updateBinRange(@Valid @RequestBody BinRangeDTO binRangeDTO) {
    binRangeDTO.setCardBrand(SystemConstants.CARD_BRAND);
    binRangeDTO.setCardType(CardType.CREDIT.name());
    binRangeDTO.setUser(getUserAccount());
    if (binRangeDTO.getId() <= 0) {
      return new ApiResponse(
          ResultStatus.NO_SUCH_DATA, "Failed in update bin range, missing critical argument.");
    }

    return new ApiResponse<>(facade.updateBinRange(binRangeDTO));
  }

  @PostMapping("/bin-range/delete")
  @PreAuthorize("hasRole('ROLE_SYS_BIN_RANGE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#deleteDataDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.BIN_RANGE_DELETE)
  public ApiResponse deleteBinRange(@RequestBody DeleteDataDTO deleteDataDTO) {
    deleteDataDTO.setUser(getUserAccount());
    DataEditResultDTO result = facade.deleteBinRange(deleteDataDTO);
    return new ApiResponse<>(result);
  }

  /** 查詢基本設定 */
  @GetMapping("/general-setting")
  @PreAuthorize("hasRole('ROLE_GENERAL_SETTING_QUERY')")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.GENERAL_SETTING_LIST)
  public ApiResponse getGeneralSetting() {
      return new ApiResponse<>(facade.getGeneralSetting());
  }

  /** 修改基本設定 */
  @PostMapping("/general-setting")
  @PreAuthorize("hasRole('ROLE_GENERAL_SETTING_MODIFY')")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.GENERAL_SETTING_UPDATE)
  public ApiResponse updateGeneralSetting(@Valid @RequestBody GeneralSettingUpdateDTO updateDto) {
    facade.updateGeneralSetting(updateDto, getUserAccount());
    return ApiResponse.SUCCESS_API_RESPONSE;
  }

  /** 取得語系清單 */
  @GetMapping("/language-list")
  @Secured("ROLE_SYS_CHALLENGE_VIEW_QUERY")
  public ApiResponse<List<LanguageResDTO>> getLanguageList() {
    List<LanguageResDTO> result = facade.listLanguage();
    return new ApiResponse<>(result);
  }

  @GetMapping("/error-code/function-names")
  @Secured("ROLE_SYS_ERROR_CODE_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ERROR_CODE_LIST)
  public ApiResponse getErrorIssueGroup() {
    return new ApiResponse<>(facade.getErrorIssueGroup());
  }

  //  @GetMapping("/error-code/{id}")
  //  @Secured("ROLE_SYS_ERROR_CODE_QUERY")
  //  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ERROR_CODE_LIST)
  //  public ApiResponse getByFunctionId(@PathVariable Long id) {
  //    return new ApiResponse(facade.getErrorCodeByGroupId(id));
  //  }

  @PostMapping("/error-code/update")
  @Secured("ROLE_SYS_ERROR_CODE_MODIFY")
//  @PreAuthorize("hasRole('ROLE_SYS_ERROR_CODE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#errorCodeGroupDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ERROR_CODE_UPDATE)
  public ApiResponse updateErrorCodeMessage(@RequestBody ErrorIssueGroupDTO errorCodeGroupDTO) {
    errorCodeGroupDTO.setUser(getUserAccount());
    return new ApiResponse<>(facade.updateErrorCodeMessageGroup(errorCodeGroupDTO));
  }

  /** 查詢密鑰列表 */
  @GetMapping("/key/list/{issuerBankId}")
  @PreAuthorize("hasRole('ROLE_SYS_KEY_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.KEY_LIST)
  public ApiResponse getKeyList(@PathVariable Long issuerBankId) {
    List<SecretKeyDTO> resultList = facade.getKeyList(issuerBankId);
    return ApiResponse.valueOf(resultList);
  }

    /** 編輯密鑰 */
    @PostMapping("/key/update")
    @PreAuthorize("hasRole('ROLE_SYS_KEY_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#reqDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.KEY_MODIFY)
    public ApiResponse updateKey(@RequestBody @Valid KeyOperationReqDTO reqDto) {
        SecretKeyDTO serviceDto = new SecretKeyDTO(reqDto);
        serviceDto.setOperator(getUserAccount());
        return new ApiResponse<>(facade.saveOrUpdateKey(serviceDto));
    }


    /** 匯入 cavv key - confirm */
    @PostMapping("/key/import/confirm")
    @PreAuthorize("hasRole('ROLE_SYS_KEY_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#reqDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.KEY_IMPORT_CONFIRM)
    public ApiResponse importKeyConfirm(@RequestBody @Valid KeyImportReqDTO reqDto) {
        facade.importKeyConfirm(reqDto, getUserAccount());
        return ApiResponse.SUCCESS_API_RESPONSE;
    }


    /** 匯入 cavv key by file - confirm */
    @PostMapping("/key/import/confirm/file")
    @PreAuthorize(
        "hasRole('ROLE_SYS_KEY_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.KEY_IMPORT_CONFIRM)
    public ApiResponse importKeyConfirmFile(
        @RequestParam(value = "keyComponentsFile") MultipartFile keyComponentsFile,
        @RequestParam(value = "issuerBankId") Long issuerBankId,
        @RequestParam(value = "cardBrand") @CardBrandValidation String cardBrand,
        @RequestParam(value = "keyType") CavvKeyType keyType) {

      // read file
      List<String> keyComponents = readCavvKeyComponentFile(keyComponentsFile);

      // convert to dto
      KeyImportReqDTO reqDto = new KeyImportReqDTO();
      reqDto.setIssuerBankId(issuerBankId);
      reqDto.setCardBrand(cardBrand);
      reqDto.setKeyType(keyType);
      reqDto.setKeyComponents(keyComponents);

      facade.importKeyConfirm(reqDto, getUserAccount());
      return ApiResponse.SUCCESS_API_RESPONSE;
    }

    /**
     * Read file and convert to cavv key components
     * @param keyComponentsFile
     * @return list of cavv key components
     */
    private List<String> readCavvKeyComponentFile(MultipartFile keyComponentsFile) {
      String fileString;
      try {
        fileString = new String(keyComponentsFile.getBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "Unable to read file", e);
      }
      return Arrays.asList(fileString.split(","));
    }
}
