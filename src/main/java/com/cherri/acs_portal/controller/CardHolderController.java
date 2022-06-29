package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.ApiPageResponse;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.attempt.GrantedLogDTO;
import com.cherri.acs_portal.dto.attempt.GrantedLogQueryDTO;
import com.cherri.acs_portal.dto.cardholder.HolderIdQueryDTO;
import com.cherri.acs_portal.dto.cardholder.HolderQueryDTO;
import com.cherri.acs_portal.dto.cardholder.HolderSummaryDTO;
import com.cherri.acs_portal.dto.cardholder.ThreeDSVerifiedLogDTO;
import com.cherri.acs_portal.dto.cardholder.ThreeDSVerifyDTO;
import com.cherri.acs_portal.dto.cardholder.UnlockOtpVerifyDTO;
import com.cherri.acs_portal.dto.whitelist.AttemptGrantedDTO;
import com.cherri.acs_portal.dto.whitelist.AttemptSettingLimitationDTO;
import com.cherri.acs_portal.facade.CardHolderFacade;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.models.data_object.entity.OtpOperationLogDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 持卡人/卡片管理 */
@Log4j2
@RestController
@RequestMapping("/auth/cardholder")
public class CardHolderController extends ContextProvider {
  @Autowired
  private CardHolderFacade holderFacade;

  /** 查詢持卡人個人資訊 */
  @PostMapping("/records")
  @PreAuthorize(
      "hasRole('ROLE_CARD_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.CARDHOLDER_GET)
  public ApiResponse getCardHolderSummary(@Valid @RequestBody HolderQueryDTO queryDTO) {
    if (StringUtils.isAllBlank(queryDTO.getIdentityNumber(), queryDTO.getRealCardNumber())) {
      return new ApiResponse(
          ResultStatus.ILLEGAL_ARGUMENT,
          "Failed in query holder info. Because of missing query argument.");
    }
    HolderSummaryDTO summary = holderFacade.getHolderSummary(queryDTO);
    return new ApiResponse<>(summary);
  }

  /** OTP解鎖 */
  @PostMapping("/otp/unlock")
  @PreAuthorize(
      "hasRole('ROLE_CARD_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#unlockOtpVerifyDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.CARDHOLDER_OTP_UNLOCK_UPDATE)
  public ApiResponse unlockOtp(@Valid @RequestBody UnlockOtpVerifyDTO unlockOtpVerifyDto) {
    unlockOtpVerifyDto.setUser(getUserAccount());
    return new ApiResponse<>(holderFacade.unlockOtp(unlockOtpVerifyDto));
  }

  /** 取得人工彈性授權設定值 */
  @GetMapping("/attempt-auth/{issuerBankId}")
  @PreAuthorize(
    "hasRole('ROLE_CARD_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ATTEMPT_AUTH_SETTING_GET)
  public ApiResponse<AttemptSettingLimitationDTO> getWhiteListAttemptSetting(
    @PathVariable Long issuerBankId) {
    AttemptSettingLimitationDTO setting = holderFacade.getAttemptAuthorizeSetting(issuerBankId);
    return new ApiResponse<>(setting);
  }

  /** 人工彈性授權設定 */
  @PostMapping("/attempt-auth")
  @PreAuthorize(
      "hasRole('ROLE_CARD_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#grantedDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ATTEMPT_AUTH_SETTING_CREATE)
  public ApiResponse grantWhiteListAttempt(@Valid @RequestBody AttemptGrantedDTO grantedDto) {
    grantedDto.setCreator(getUserAccount());
    return new ApiResponse<>(holderFacade.grantAttemptSetting(grantedDto));
  }

  /** 人工彈性授權紀錄 */
  @PostMapping("/attempt-auth/record")
  @PreAuthorize(
      "hasRole('ROLE_CARD_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#grantedLogQueryDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ATTEMPT_AUTH_LOG_LIST)
  public ApiPageResponse getGrantedLog(@Valid @RequestBody GrantedLogQueryDTO grantedLogQueryDTO) {
    PagingResultDTO<GrantedLogDTO> grantedLogResult =
        holderFacade.getAttemptGrantedLog(grantedLogQueryDTO);
    return new ApiPageResponse<>(grantedLogResult);
  }

  //  /** 人工彈性授權紀錄匯出 */
  //  @PostMapping("/attempt-auth/record/export/csv")
  //  @PreAuthorize(
  //
  // "hasRole('ROLE_CARD_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDTO.issuerBankId)")
  //  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ATTEMPT_AUTH_LOG_EXPORT)
  //  public void exportAttemptLogCsv(@Valid @RequestBody GrantedLogQueryDTO queryDTO)
  //      throws IOException {
  //    holderFacade.exportAttemptLogToCSV(queryDTO);
  //  }

  /** OTP紀錄 */
  @PostMapping("/otp/record")
  @PreAuthorize(
      "hasRole('ROLE_CARD_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#holderIdQueryDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.OTP_LOG_LIST)
  public ApiPageResponse getOtpLockLog(@Valid @RequestBody HolderIdQueryDTO holderIdQueryDTO) {
    if (holderIdQueryDTO.getPageSize() == null)
      holderIdQueryDTO.setPageSize(5);
    if (holderIdQueryDTO.getPageSize() > EnvironmentConstants.PAGINATION_MAX_ROWS)
      holderIdQueryDTO.setPageSize(EnvironmentConstants.PAGINATION_MAX_ROWS);

    PagingResultDTO<OtpOperationLogDO> pageResult =
      holderFacade.getOtpLogByPanId(holderIdQueryDTO);
    if (pageResult == null) {
      return new ApiPageResponse(
        ResultStatus.SERVER_ERROR,
        "Failed in get OTP operation Log, please contact system administrator.");
    }
    return new ApiPageResponse<>(pageResult);
  }

  //  /** OTP紀錄匯出 */
  //  @PostMapping("/otp/record/export/csv")
  //  @PreAuthorize(
  //
  // "hasRole('ROLE_CARD_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#holderIdQueryDTO.issuerBankId)")
  //  @AuditLogHandler(methodName = AuditLogMethodNameEnum.OTP_LOG_EXPORT)
  //  public void exportOtpLockLogCsv(@Valid @RequestBody HolderIdQueryDTO holderIdQueryDTO)
  //      throws IOException {
  //    if (holderIdQueryDTO.getPageSize() == null
  //        || holderIdQueryDTO.getPageSize() > EnvironmentConstants.PAGINATION_MAX_ROWS)
  //      holderIdQueryDTO.setPageSize(EnvironmentConstants.PAGINATION_MAX_ROWS);
  //
  //    holderFacade.exportOtpOperationLog(holderIdQueryDTO);
  //  }

  /** 3DS驗證狀態啟用 */
  @PostMapping("/3DS/verify-enabled/update")
  @PreAuthorize(
      "hasRole('ROLE_CARD_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#verifyDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.CARDHOLDER_3DS_VERIFY_ENABLED_UPDATE)
  public ApiResponse update3DSVerifyStatus(@Valid @RequestBody ThreeDSVerifyDTO verifyDTO) {
    verifyDTO.setUser(getUserAccount());
    return new ApiResponse<>(holderFacade.update3DSVerifyStatus(verifyDTO));
  }

  /** 3DS驗證紀錄 */
  @PostMapping("/3DS/record")
  @PreAuthorize(
      "hasRole('ROLE_CARD_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#holderIdQueryDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.THREE_DS_LOG_LIST)
  public ApiPageResponse getThreeDSVerifiedLog(
      @Valid @RequestBody HolderIdQueryDTO holderIdQueryDTO) {
    if (holderIdQueryDTO.getPageSize() == null) holderIdQueryDTO.setPageSize(5);
    if (holderIdQueryDTO.getPageSize() > EnvironmentConstants.PAGINATION_MAX_ROWS)
      holderIdQueryDTO.setPageSize(EnvironmentConstants.PAGINATION_MAX_ROWS);

    PagingResultDTO<ThreeDSVerifiedLogDTO> pageResult =
        holderFacade.getThreeDSVerifiedLogByPanId(holderIdQueryDTO);
    return new ApiPageResponse<>(pageResult);
  }

  //  /** 3DS驗證紀錄匯出 */
  //  @PostMapping("/3DS/record/export/csv")
  //  @PreAuthorize(
  //
  // "hasRole('ROLE_CARD_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#holderIdQueryDTO.issuerBankId)")
  //  @AuditLogHandler(methodName = AuditLogMethodNameEnum.THREE_DS_LOG_EXPORT)
  //  public void exportThreeDSVerifiedLogCsv(@Valid @RequestBody HolderIdQueryDTO holderIdQueryDTO)
  //      throws IOException {
  //    if (holderIdQueryDTO.getPageSize() == null
  //        || holderIdQueryDTO.getPageSize() > EnvironmentConstants.PAGINATION_MAX_ROWS)
  //      holderIdQueryDTO.setPageSize(EnvironmentConstants.PAGINATION_MAX_ROWS);
  //    holderFacade.exportThreeDSVerifiedLog(holderIdQueryDTO);
  //  }
}
