package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.dto.ApiPageResponse;
import com.cherri.acs_portal.dto.transactionLog.PReqRecordRequestDTO;
import com.cherri.acs_portal.dto.transactionLog.PReqRecordResponseDTO;
import com.cherri.acs_portal.facade.PReqRecordFacade;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/preparation")
public class PReqRecordController extends ContextProvider {

    private final PReqRecordFacade facade;

    public PReqRecordController(PReqRecordFacade facade) {
        this.facade = facade;
    }

    @PostMapping("/record")
    @Secured("ROLE_TX_QUERY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PREQ_RECORD_LIST)
    public ApiPageResponse<PReqRecordResponseDTO> getRecords(@RequestBody PReqRecordRequestDTO request) {
        return new ApiPageResponse<>(facade.getRecords(request));
    }

}
