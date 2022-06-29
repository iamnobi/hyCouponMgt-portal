package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.MimaRecordDO;

/**
 * MimaRecordDAO
 *
 * @author Alan Chen
 */
public interface MimaRecordDAO {
    boolean isDuplicatedByMimaRecordHistory(long issuerBankId, String account, int range,
      String encryptedMima);

    boolean saveRecord(MimaRecordDO mimaRecord);

    Optional<MimaRecordDO> findLastChangeMimaRecord(long issuerBankId, String account);
}
