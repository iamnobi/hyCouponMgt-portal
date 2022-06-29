package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.BlackListIpDAO;
import ocean.acs.models.data_object.kernel.BlackListIpDO;

@Log4j2
@Repository
@AllArgsConstructor
public class BlackListIpDAOImpl implements BlackListIpDAO {

    private final ocean.acs.models.oracle.repository.BlackListIpRepository repo;

    @Override
    public List<BlackListIpDO> findAllIpBy(Long issuerBankID) throws DatabaseException {
        try {
            return repo.findIssuerBankIdAndIpAndCidrAndTransStatusByIssuerBankId(issuerBankID);
        } catch (Exception e) {
            log.error("[findAllIpBy] unknown exception, issuerBankID={}", issuerBankID, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }
}
