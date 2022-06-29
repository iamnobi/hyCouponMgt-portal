package com.cherri.acs_portal.model.dao;

import ocean.acs.models.oracle.entity.PreparationLog;
import ocean.acs.models.oracle.repository.PReqRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class PReqRecordDAO {

    private final PReqRecordRepository pReqRecordRepository;

    public PReqRecordDAO(PReqRecordRepository pReqRecordRepository) {
        this.pReqRecordRepository = pReqRecordRepository;
    }

    public Page<PreparationLog> findAll(Specification<PreparationLog> specification, PageRequest pageRequest) {
        return pReqRecordRepository.findAll(specification, pageRequest);
    }

}
