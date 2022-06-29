package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.transactionLog.PReqRecordRequestDTO;
import com.cherri.acs_portal.dto.transactionLog.PReqRecordResponseDTO;
import com.cherri.acs_portal.service.PReqRecordService;
import org.springframework.stereotype.Component;

@Component
public class PReqRecordFacade {

    private final PReqRecordService pReqRecordService;

    public PReqRecordFacade(PReqRecordService pReqRecordService) {
        this.pReqRecordService = pReqRecordService;
    }

    public PagingResultDTO<PReqRecordResponseDTO> getRecords(PReqRecordRequestDTO request) {
        return pReqRecordService.getRecords(request);
    }

}
