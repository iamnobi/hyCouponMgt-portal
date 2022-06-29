package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.AuditContentDTO;
import com.cherri.acs_portal.dto.audit.AuditListQueryDTO;
import com.cherri.acs_portal.dto.audit.AuditResultDTO;
import com.cherri.acs_portal.dto.audit.AuditingLogQueryDTO;
import com.cherri.acs_portal.manager.PermissionManager;
import com.cherri.acs_portal.service.AuditService;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AuditFacade {

    private final AuditService auditService;
    private final PermissionManager permissionManager;

    @Autowired
    public AuditFacade(AuditService auditService, PermissionManager permissionManager) {
        this.auditService = auditService;
        this.permissionManager = permissionManager;
    }

    public PagingResultDTO<AuditContentDTO> getPendingListWithoutSelf(
      AuditListQueryDTO queryDTO) {
        AuditFunctionType functionType = AuditFunctionType.getBySymbol(queryDTO.getFunctionType());

        Pageable paging = queryDTO.getPageable();

        return auditService
          .getPendingListWithoutSelf(queryDTO.getIssuerBankId(), functionType, paging);
    }

    public DataEditResultDTO applyAuditResult(AuditResultDTO resultDTO) {
        AuditFunctionType functionType = AuditFunctionType.getBySymbol(resultDTO.getFunctionType());
        if (functionType == AuditFunctionType.UNKNOWN
          || !permissionManager.hasPermission(functionType.getPermissionType())) {
            throw new OceanException(ResultStatus.ACCESS_DENIED_INVALID_ENDPOINT);
        }

        return auditService.applyAuditResult(resultDTO);
    }

    public Optional<List<AuditFunctionType>> getUserAuditFunctionList() {
        return auditService.getUserAuditFunctionList();
    }

    public PagingResultDTO<AuditContentDTO> getPersonalLog(AuditingLogQueryDTO queryDTO) {
        Pageable paging = queryDTO.getPageable();

        return auditService.getPersonalLog(queryDTO, paging);
    }
}
