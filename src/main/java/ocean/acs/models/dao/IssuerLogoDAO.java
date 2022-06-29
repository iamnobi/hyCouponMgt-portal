package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.IssuerLogoDO;

public interface IssuerLogoDAO {

    List<IssuerLogoDO> findAll() throws DatabaseException;

    Optional<IssuerLogoDO> findById(Long id);

    Optional<IssuerLogoDO> findByIssuerBankId(Long issuerBakId);

    Optional<IssuerLogoDO> findTopOneByIssuerBankIdAndNotDelete(Long issuerBankId);

    IssuerLogoDO save(IssuerLogoDO issuerLogoDO);
    
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
