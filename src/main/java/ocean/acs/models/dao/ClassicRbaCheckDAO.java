package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ClassicRbaCheckDO;

public interface ClassicRbaCheckDAO {

    List<ClassicRbaCheckDO> findAll() throws DatabaseException;

    Optional<ClassicRbaCheckDO> findClassicRbaSetting(long issuerBankId) throws DatabaseException;

    Optional<ClassicRbaCheckDO> save(ClassicRbaCheckDO classicRbaCheckDO) throws DatabaseException;

    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
