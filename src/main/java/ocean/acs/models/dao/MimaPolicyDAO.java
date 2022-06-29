package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.MimaPolicyDO;

/**
 * MimaPolicyDAO
 *
 * @author Alan Chen
 */
public interface MimaPolicyDAO {

    boolean isPolicyExistByIssuerBankId(long issuerBankId);

    boolean isPolicyExistById(long id);

    Optional<MimaPolicyDO> findByIssuerBankId(long issuerBankId);

    boolean createPolicy(MimaPolicyDO mimaPolicyDO);

    MimaPolicyDO createDefaultPolicy(long issuerBankId);

    boolean updatePolicy(MimaPolicyDO dto);
}
