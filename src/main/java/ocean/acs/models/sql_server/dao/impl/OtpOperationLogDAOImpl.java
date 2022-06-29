package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.OtpOperationLogDAO;
import ocean.acs.models.data_object.entity.OtpOperationLogDO;
import ocean.acs.models.data_object.portal.HolderIdQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.sql_server.entity.OtpOperationLog;

@Repository
@AllArgsConstructor
public class OtpOperationLogDAOImpl implements OtpOperationLogDAO {

    private final ocean.acs.models.sql_server.repository.OtpOperationLogRepository repo;

    @Override
    public Optional<OtpOperationLogDO> findById(Long id) {
        return repo.findById(id).map(OtpOperationLogDO::valueOf);
    }

    @Override
    public OtpOperationLogDO save(OtpOperationLogDO otpOperationLogDO) {
        OtpOperationLog otpOperationLog = OtpOperationLog.valueOf(otpOperationLogDO);
        return OtpOperationLogDO.valueOf(repo.save(otpOperationLog));
    }

    @Override
    public PagingResultDO<OtpOperationLogDO> getByPanIdAndIssuerBankId(HolderIdQueryDO queryDO) {
        Pageable pageReq = PageRequest.of(queryDO.getPage() - 1, queryDO.getPageSize(),
                Sort.Direction.DESC, "createMillis");
        Page<OtpOperationLog> queryResult = repo.findByPanIdAndIssuerBankId(queryDO.getPanId(),
                queryDO.getIssuerBankId(), pageReq);

        if (queryResult == null || queryResult.isEmpty()) {
            return PagingResultDO.empty();
        }

        List<OtpOperationLogDO> queryDoResult = queryResult.getContent().stream()
                .map(OtpOperationLogDO::valueOf).collect(Collectors.toList());
        PagingResultDO<OtpOperationLogDO> pagingResultDO = new PagingResultDO<>(queryDoResult);
        pagingResultDO.setTotal(queryResult.getTotalElements());
        pagingResultDO.setCurrentPage(queryResult.getNumber() + 1);
        pagingResultDO.setTotalPages(queryResult.getTotalPages());
        return pagingResultDO;
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        //TODO Implement method
        return 0;
    }

}
