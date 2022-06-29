package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ClassicRbaCheckDAO;
import ocean.acs.models.data_object.entity.ClassicRbaCheckDO;
import ocean.acs.models.sql_server.entity.ClassicRbaCheck;

@Log4j2
@Repository
@AllArgsConstructor
public class ClassicRbaCheckDAOImpl implements ClassicRbaCheckDAO {

    private final ocean.acs.models.sql_server.repository.ClassicRbaCheckRepository repo;

    @Override
    public List<ClassicRbaCheckDO> findAll() throws DatabaseException {
        try {
            return repo.findAll().stream().map(ClassicRbaCheckDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[ClassicRbaCheckDAO] system error.");
            log.error(e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<ClassicRbaCheckDO> findClassicRbaSetting(long issuerBankId)
            throws DatabaseException {
        try {
            return repo.findByIssuerBankIdAndDeleteFlagFalse(issuerBankId)
                    .map(ClassicRbaCheckDO::valueOf);
        } catch (Exception e) {
            log.error("[findClassicRbaSetting] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<ClassicRbaCheckDO> save(ClassicRbaCheckDO classicRbaCheckDO)
            throws DatabaseException {
        try {
            ClassicRbaCheck classicRbaCheck = ClassicRbaCheck.valueOf(classicRbaCheckDO);
            return Optional.of(ClassicRbaCheckDO.valueOf(repo.save(classicRbaCheck)));
        } catch (Exception e) {
            log.error("[save] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        //TODO Implement method
        return 0;
    }

}
