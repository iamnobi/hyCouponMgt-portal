package ocean.acs.models.dao;

/**
 * MfaDAO
 *
 * @author Alan Chen
 */
public interface MfaDAO {

    boolean isRegistered(long issuerBankId, String account);

    String findSecretKey(long issuerBankId, String account);

    void update(long issuerBankId, String account, String secretKey);

    void save(long issuerBankId, String account, String secretKey);

}
