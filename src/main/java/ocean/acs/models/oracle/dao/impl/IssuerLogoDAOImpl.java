package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.IssuerLogoDAO;
import ocean.acs.models.data_object.entity.IssuerLogoDO;
import ocean.acs.models.oracle.entity.BinRange;
import ocean.acs.models.oracle.entity.IssuerLogo;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class IssuerLogoDAOImpl implements IssuerLogoDAO {

    private final ocean.acs.models.oracle.repository.IssuerLogoRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public List<IssuerLogoDO> findAll() throws DatabaseException {
        try {
            return repo.findAll().stream().map(IssuerLogoDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<IssuerLogoDO> findById(Long id) {
        return repo.findById(id).map(IssuerLogoDO::valueOf);
    }

    @Override
    public Optional<IssuerLogoDO> findByIssuerBankId(Long issuerBakId) {
        return repo.findByIssuerBankId(issuerBakId).map(IssuerLogoDO::valueOf);
    }

    @Override
    public Optional<IssuerLogoDO> findTopOneByIssuerBankIdAndNotDelete(Long issuerBankId) {
        return repo.findTopOneByIssuerBankIdAndNotDelete(issuerBankId).map(IssuerLogoDO::valueOf);
    }

    @Override
    public IssuerLogoDO save(IssuerLogoDO issuerLogoDO) {
        try {
            IssuerLogo issuerLogo = IssuerLogo.valueOf(issuerLogoDO);
            return IssuerLogoDO.valueOf(repo.save(issuerLogo));
        } catch (Exception e) {
            log.error("[save] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(IssuerLogo.class, issuerBankId, deleter, deleteMillis);
    }

}
