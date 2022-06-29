package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.ThreeDSVerifiedDAO;
import ocean.acs.models.data_object.entity.ThreeDSVerifiedOperationLogDO;
import ocean.acs.models.data_object.portal.PageQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.PortalThreeDSVerifiedLogDO;
import ocean.acs.models.sql_server.entity.ThreeDSVerifiedOperationLog;

@Repository
@AllArgsConstructor
public class ThreeDSVerifiedDAOImpl implements ThreeDSVerifiedDAO {

    private final ocean.acs.models.sql_server.repository.ThreeDSVerifiedLogRepository repo;

    @Override
    public Optional<ThreeDSVerifiedOperationLogDO> findById(Long id) {
        return repo.findById(id).map(ThreeDSVerifiedOperationLogDO::valueOf);
    }

    @Override
    public Optional<ThreeDSVerifiedOperationLogDO> save(
            ThreeDSVerifiedOperationLogDO threeDSVerifiedOperationLogDO) {
        ThreeDSVerifiedOperationLog threeDSVerifiedOperationLog =
                ThreeDSVerifiedOperationLog.valueOf(threeDSVerifiedOperationLogDO);
        return Optional
                .of(ThreeDSVerifiedOperationLogDO.valueOf(repo.save(threeDSVerifiedOperationLog)));
    }

    @Override
    public PagingResultDO<PortalThreeDSVerifiedLogDO> getInfoByCardHolderIdAndIssuerBankId(Long panId,
            Long issuerBankId, PageQueryDO queryDO) {
        PageRequest pageReq = PageRequest.of(queryDO.getPage() - 1, queryDO.getPageSize(),
                Sort.Direction.DESC, "createMillis");
        Page<ThreeDSVerifiedOperationLog> queryResult =
                repo.findByPanIdAndIssuerBankId(panId, issuerBankId, pageReq);
        if (queryResult == null || queryResult.isEmpty()) {
            return PagingResultDO.empty();
        }

        List<PortalThreeDSVerifiedLogDO> data = queryResult.getContent().stream()
                .map(PortalThreeDSVerifiedLogDO::valueOf).collect(Collectors.toList());
        PagingResultDO<PortalThreeDSVerifiedLogDO> pagingResultDO = new PagingResultDO<>(data);
        pagingResultDO.setTotal(queryResult.getTotalElements());
        pagingResultDO.setCurrentPage(queryDO.getPage());
        pagingResultDO.setTotalPages(queryResult.getTotalPages());

        return pagingResultDO;
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        //TODO Implement method
        return 0;
    }

}
