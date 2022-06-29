package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.controller.request.AcquirerBank3dsSdkRefNumCreateRequest;
import com.cherri.acs_portal.controller.request.AcquirerBank3dsSdkRefNumDeleteRequest;
import com.cherri.acs_portal.controller.request.AcquirerBankAddAcquirerBinRequest;
import com.cherri.acs_portal.controller.request.AcquirerBankCreateRequest;
import com.cherri.acs_portal.controller.request.AcquirerBankUpdateRequest;
import com.cherri.acs_portal.controller.request.IdRequest;
import com.cherri.acs_portal.dto.AcquirerBank3dsRefNumDTO;
import com.cherri.acs_portal.dto.AcquirerBankDTO;
import com.cherri.acs_portal.dto.AcquirerBinDTO;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.facade.AcquirerBankFacade;
import com.cherri.acs_portal.service.AcquirerBank3dsRefNumService;
import com.cherri.acs_portal.service.AcquirerBankService;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Acquirer Bank 管理 */
@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/auth/acquirer-bank")
public class AcquirerBankController extends ContextProvider {

  private final AcquirerBankService service;

  private final AcquirerBankFacade acquirerBankFacade;

  /**
   * 查詢3DS SDK Reference Number
   *
   * @return 3DS SDK Reference Number List
   */
  @Secured("ROLE_ACQUIRER_BANK_QUERY")
  @GetMapping("/3ds-sdk")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_QUERY_THREEDS_REF_NUMBERS)
  public ApiResponse<List<AcquirerBank3dsRefNumDTO>> query3dsSdkRefNumList() {
    return acquirerBankFacade.query3dsRefNumList();
  }

  /**
   * 新增3DS SDK Reference Number
   *
   * @param request AcquirerBank3dsSdkRefNumCreateRequest
   * @return isSuccess
   */
  @Secured("ROLE_ACQUIRER_BANK_MODIFY")
  @PostMapping("/3ds-sdk/create")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_ADD_THREEDS_REF_NUMBER)
  public ApiResponse<Boolean> create3dsSdk(
      @RequestBody @Valid AcquirerBank3dsSdkRefNumCreateRequest request) {
    return acquirerBankFacade.create3dsRefNum(request, getUserAccount());
  }

  /**
   * 刪除3DS SDK Reference Number
   *
   * @param request AcquirerBank3dsSdkRefNumDeleteRequest
   * @return isDeleted
   */
  @Secured("ROLE_ACQUIRER_BANK_MODIFY")
  @PostMapping("/3ds-sdk/delete")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_DELETE_THREEDS_REF_NUMBER)
  public ApiResponse<Boolean> delete3dsSdk(
      @RequestBody @Valid AcquirerBank3dsSdkRefNumDeleteRequest request) {
    return acquirerBankFacade.delete3dsRefNum(request);
  }

  /** 新增 Acquirer Bank */
  @PostMapping("/create")
  @Secured("ROLE_ACQUIRER_BANK_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_CREATE)
  public ApiResponse createAcquirerBank(@RequestBody @Valid AcquirerBankCreateRequest request) {

    //00003 allow control asc/3ds oper id by user
    log.info("Check Dup 3DSS Oper ID");
    if (service.is3dsOperatorIdExist(request)) {
      log.info("Dup 3DSS Oper ID:{}", StringUtils.normalizeSpace(request.getThreeDSOperatorId()));
      throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, "3DSS Operator ID duplicate in system.");
    }

    service.createAcquirerBank(request, getUserAccount());
    return ApiResponse.SUCCESS_API_RESPONSE;
  }

  /** 刪除 Acquirer Bank */
  @PostMapping("/delete")
  @Secured("ROLE_ACQUIRER_BANK_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_DELETE)
  public ApiResponse deleteAcquirerBank(@RequestBody @Valid IdRequest request) {
    service.deleteAcquirerBank(request.getId());
    return ApiResponse.SUCCESS_API_RESPONSE;
  }

  /** 編輯 Acquirer Bank */
  @PostMapping("/edit")
  @Secured("ROLE_ACQUIRER_BANK_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_MODIFY)
  public ApiResponse editAcquirerBank(@RequestBody @Valid AcquirerBankUpdateRequest request) {
    service.updateAcquirerBank(request, getUserAccount());
    return ApiResponse.SUCCESS_API_RESPONSE;
  }

  /** 查詢 Acquirer Bank */
  @GetMapping("/")
  @Secured("ROLE_ACQUIRER_BANK_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_GET)
  public ApiResponse<List<AcquirerBankDTO>> findAcquirerBankList() {
    return new ApiResponse<>(service.findAcquirerBankList());
  }

  /** 查詢 Acquirer Bank 下拉選單 */
  @GetMapping("/list")
  public ApiResponse<List<AcquirerBankDTO>> findAcquirerBankListDropDown() {
    return new ApiResponse<>(service.findAcquirerBankList());
  }

  /** 查詢 Acquirer BIN */
  @PostMapping("/bin")
  @Secured("ROLE_ACQUIRER_BANK_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_DELETE)
  public ApiResponse<List<AcquirerBinDTO>> findAcquirerBin(@RequestBody @Valid IdRequest request) {
    return new ApiResponse<>(service.findAcquirerBinByAcquirerBankId(request.getId()));
  }

  /** 查詢 Acquirer BIN */
  @PostMapping("/bin/create")
  @Secured("ROLE_ACQUIRER_BANK_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_ADD_BIN)
  public ApiResponse addAcquirerBin(@RequestBody @Valid AcquirerBankAddAcquirerBinRequest request) {
    service.addAcquirerBin(request, getUserAccount());
    return ApiResponse.SUCCESS_API_RESPONSE;
  }

  /** 刪除 Acquirer BIN */
  @PostMapping("/bin/delete")
  @Secured("ROLE_ACQUIRER_BANK_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACQUIRER_BANK_DELETE)
  public ApiResponse deleteAcquirerBin(
      @RequestBody @Valid IdRequest request) {
    service.deleteAcquirerBin(request.getId());
    return ApiResponse.SUCCESS_API_RESPONSE;
  }
}
