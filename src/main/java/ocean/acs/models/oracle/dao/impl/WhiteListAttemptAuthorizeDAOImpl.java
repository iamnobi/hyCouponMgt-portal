package ocean.acs.models.oracle.dao.impl;

import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.WhiteListAttemptAuthorizeDAO;
import ocean.acs.models.data_object.entity.WhiteListAttemptAuthorizeDO;
import ocean.acs.models.oracle.entity.WhiteListAttemptAuthorize;

@Repository
@AllArgsConstructor
public class WhiteListAttemptAuthorizeDAOImpl implements WhiteListAttemptAuthorizeDAO {

    private final ocean.acs.models.oracle.repository.WhiteListAttemptAuthorizeRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public WhiteListAttemptAuthorizeDO save(WhiteListAttemptAuthorizeDO attemptAuthorizeDO) {
        WhiteListAttemptAuthorize attemptAuthorize =
                WhiteListAttemptAuthorize.valueOf(attemptAuthorizeDO);
        return WhiteListAttemptAuthorizeDO.valueOf(repo.save(attemptAuthorize));
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(WhiteListAttemptAuthorize.class, issuerBankId, deleter, deleteMillis);
    }
}
