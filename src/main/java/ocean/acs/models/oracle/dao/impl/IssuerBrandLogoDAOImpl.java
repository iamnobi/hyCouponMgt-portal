package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.IssuerBrandLogoDAO;
import ocean.acs.models.data_object.entity.IssuerBrandLogoDO;
import ocean.acs.models.oracle.entity.IssuerBrandLogo;

@Repository
@AllArgsConstructor
public class IssuerBrandLogoDAOImpl implements IssuerBrandLogoDAO {

    private final ocean.acs.models.oracle.repository.IssuerBrandLogoRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public Optional<IssuerBrandLogoDO> findByIssuerBankId(Long issuerBankId) {
        return repo.findByIssuerBankId(issuerBankId).map(IssuerBrandLogoDO::valueOf);
    }

    @Override
    public IssuerBrandLogoDO save(IssuerBrandLogoDO issuerLogoDO) {
        IssuerBrandLogo issuerBrandLogo = IssuerBrandLogo.valueOf(issuerLogoDO);
        return IssuerBrandLogoDO.valueOf(repo.save(issuerBrandLogo));
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(IssuerBrandLogo.class, issuerBankId, deleter, deleteMillis);
    }

}
