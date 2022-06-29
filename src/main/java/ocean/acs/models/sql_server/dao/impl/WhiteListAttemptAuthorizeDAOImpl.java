package ocean.acs.models.sql_server.dao.impl;

import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.WhiteListAttemptAuthorizeDAO;
import ocean.acs.models.data_object.entity.WhiteListAttemptAuthorizeDO;
import ocean.acs.models.sql_server.entity.WhiteListAttemptAuthorize;

@Repository
@AllArgsConstructor
public class WhiteListAttemptAuthorizeDAOImpl implements WhiteListAttemptAuthorizeDAO {

    private final ocean.acs.models.sql_server.repository.WhiteListAttemptAuthorizeRepository repo;

    @Override
    public WhiteListAttemptAuthorizeDO save(WhiteListAttemptAuthorizeDO attemptAuthorizeDO) {
        WhiteListAttemptAuthorize attemptAuthorize =
                WhiteListAttemptAuthorize.valueOf(attemptAuthorizeDO);
        return WhiteListAttemptAuthorizeDO.valueOf(repo.save(attemptAuthorize));
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        //TODO Implement method
        return 0;
    }
}
