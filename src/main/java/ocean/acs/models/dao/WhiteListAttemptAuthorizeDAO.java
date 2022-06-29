package ocean.acs.models.dao;

import ocean.acs.models.data_object.entity.WhiteListAttemptAuthorizeDO;

public interface WhiteListAttemptAuthorizeDAO {

    WhiteListAttemptAuthorizeDO save(WhiteListAttemptAuthorizeDO attemptAuthorizeDO);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
