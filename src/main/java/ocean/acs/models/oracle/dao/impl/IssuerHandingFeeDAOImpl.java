package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import ocean.acs.models.data_object.entity.IssuerHandingFeeDO;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.IssuerHandingFeeDAO;
import ocean.acs.models.oracle.entity.IssuerHandingFee;

@Log4j2
@Repository
@AllArgsConstructor
public class IssuerHandingFeeDAOImpl implements IssuerHandingFeeDAO {

    private final ocean.acs.models.oracle.repository.IssuerHandingFeeRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public Optional<IssuerHandingFeeDO> getByIssuerBankId(Long issuerBankId) {
        return repo.getByIssuerBankId(issuerBankId).map(IssuerHandingFeeDO::valueOf);
    }

    @Override
    public List<IssuerHandingFeeDO> listByIssuerBankIdIn(Set<Long> issuerBankIdSet) {
        return repo.findByIssuerBankIdIn(issuerBankIdSet).stream().map(IssuerHandingFeeDO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<IssuerHandingFeeDO> findOneById(Long issuerHandingFeeId) {
        return repo.findById(issuerHandingFeeId).map(IssuerHandingFeeDO::valueOf);
    }

    @Override
    public Boolean save(IssuerHandingFeeDO issuerHandingFeeDO) {
        try {
            IssuerHandingFee issuerHandingFee = IssuerHandingFee.valueOf(issuerHandingFeeDO);
            issuerHandingFee = repo.save(issuerHandingFee);
            return null != issuerHandingFee.getId();
        } catch (Exception e) {
            log.error("[save] unknown exception", e);
            return false;
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(IssuerHandingFee.class, issuerBankId, deleter, deleteMillis);
    }

}
