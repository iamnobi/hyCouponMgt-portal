package com.cherri.acs_portal.service;

import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.transactionLog.PReqRecordRequestDTO;
import com.cherri.acs_portal.dto.transactionLog.PReqRecordResponseDTO;
import com.cherri.acs_portal.model.dao.PReqRecordDAO;
import ocean.acs.models.oracle.entity.PreparationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PReqRecordService {

    private final PReqRecordDAO preqRecordDAO;

    public PReqRecordService(PReqRecordDAO preqRecordDAO) {
        this.preqRecordDAO = preqRecordDAO;
    }

    public PagingResultDTO<PReqRecordResponseDTO> getRecords(PReqRecordRequestDTO request) {
        Specification<PreparationLog> specification = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(
                    cb.and((request.getThreeDSOperatorId() == null)
                            ? cb.conjunction()
                            : cb.equal(root.get("threeDSServerOperatorID"), request.getThreeDSOperatorId()))
            );
            predicateList.add(
                    cb.and((request.getStartMillis() == null)
                            ? cb.conjunction()
                            : cb.ge(root.get("createMillis"), request.getStartMillis()))
            );
            predicateList.add(
                    cb.and((request.getEndMillis() == null)
                            ? cb.conjunction()
                            : cb.le(root.get("createMillis"), request.getEndMillis()))
            );

            query.orderBy(cb.desc(root.get("createMillis")));

            Predicate[] predicates = new Predicate[predicateList.size()];
            return cb.and(predicateList.toArray(predicates));
        };

        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getPageSize());

        Page<PreparationLog> page = preqRecordDAO.findAll(specification, pageRequest);
        List<PReqRecordResponseDTO> data = page.getContent().stream()
                .map(PReqRecordResponseDTO::valueOf)
                .collect(Collectors.toList());
        PagingResultDTO<PReqRecordResponseDTO> pagingResultDTO = PagingResultDTO.valueOf(page);
        pagingResultDTO.setData(data);
        return pagingResultDTO;
    }

}
