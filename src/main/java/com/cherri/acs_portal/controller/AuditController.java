package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.dto.ApiPageResponse;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.AuditContentDTO;
import com.cherri.acs_portal.dto.audit.AuditListQueryDTO;
import com.cherri.acs_portal.dto.audit.AuditResultDTO;
import com.cherri.acs_portal.dto.audit.AuditingLogQueryDTO;
import com.cherri.acs_portal.facade.AuditFacade;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("auth/auditing")
public class AuditController extends ContextProvider {
  private final AuditFacade auditFacade;

  @Autowired
  public AuditController(AuditFacade auditFacade) {
    this.auditFacade = auditFacade;
  }

  /** 取得執行項目 */
  @GetMapping("/functionType")
  @Secured("ROLE_LOGIN_EVERYONE")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.GRANTED_AUDIT_FUNCTION_LIST)
  public ApiResponse getGrantedAuditFunctionList() {
    Optional<List<AuditFunctionType>> listOpt = auditFacade.getUserAuditFunctionList();

    return listOpt.map((Function<List<AuditFunctionType>, ApiResponse>) ApiResponse::new).orElseGet(() -> new ApiResponse(
            ResultStatus.SERVER_ERROR,
            "Failed in get granted function list of audition, please contact system administrator."));
  }

  /** 覆核與放行（通過/拒絕）*/
  @PostMapping("/signing")
  @PreAuthorize("hasRole('ROLE_LOGIN_EVERYONE') && @permissionService.checkAccessMultipleBank(authentication.principal,#resultDTO.issuerBankId) and @permissionService.checkAuditPermission(authentication.principal,#resultDTO.issuerBankId, #resultDTO.functionType)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SIGNING_AUDITING)
  public ApiResponse signAuditResult(@Valid @RequestBody AuditResultDTO resultDTO) {
    return new ApiResponse<>(auditFacade.applyAuditResult(resultDTO));
  }

  /** 待審核項目 */
  @PostMapping("/list")
  @PreAuthorize("hasRole('ROLE_LOGIN_EVERYONE') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDTO.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.AUDITING_LIST)
  public ApiPageResponse getAuditingList(@Valid @RequestBody AuditListQueryDTO queryDTO) {
    PagingResultDTO<AuditContentDTO> auditContentListPaging =
        auditFacade.getPendingListWithoutSelf(queryDTO);
    if (auditContentListPaging == null) {
      return new ApiPageResponse(
          ResultStatus.SERVER_ERROR,
          "Failed in get auditing list with unknown exception, please contact system administrator.");
    } else {
      return new ApiPageResponse<>(auditContentListPaging);
    }
  }

  /** 覆核狀態查詢 */
  @PostMapping("/log/personal")
  @Secured("ROLE_LOGIN_EVERYONE")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.AUDITING_PERSONAL_LOG)
  public ApiPageResponse getAuditingLogOfPersonal(@Valid @RequestBody AuditingLogQueryDTO logDTO) {
    PagingResultDTO<AuditContentDTO> auditLogListPaging = auditFacade.getPersonalLog(logDTO);

    if (auditLogListPaging == null) {
      return new ApiPageResponse(
              ResultStatus.SERVER_ERROR,
              "Failed in get auditing log list with unknown exception, please contact system administrator.");
    } else {
      return new ApiPageResponse<>(auditLogListPaging);
    }
  }
}
