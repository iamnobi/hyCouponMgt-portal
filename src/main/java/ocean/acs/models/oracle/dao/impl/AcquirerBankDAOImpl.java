package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.oracle.entity.AcquirerBank;
import ocean.acs.models.oracle.repository.AcquirerBankRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class AcquirerBankDAOImpl {

  private final AcquirerBankRepository acquirerBankRepository;

  public AcquirerBank save(AcquirerBank acquirerBank) throws DatabaseException {
    try {
      return acquirerBankRepository.save(acquirerBank);
    } catch (Exception e) {
      log.error("[save] error", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }
  //00003 allow control asc/3ds oper id by user
  public Optional<AcquirerBank> findBythreeDSOperatorId(String threeDSOperatorId) throws DatabaseException {
    try
    {
      return acquirerBankRepository.findBythreeDSOperatorId(threeDSOperatorId);
    } catch (Exception e) {
      log.error("[save] findBythreeDSOperatorId", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }

  public Optional<AcquirerBank> findById(Long id) throws DatabaseException {
    try {
      return acquirerBankRepository.findById(id);
    } catch (Exception e) {
      log.error("[save] findById", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }

  public List<AcquirerBank> findAll() throws DatabaseException {
    try {
      return acquirerBankRepository.findAll(Sort.by(Direction.ASC, "createMillis"));
    } catch (Exception e) {
      log.error("[findAll] error", e);
      throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }

  public void deleteById(Long id) throws DatabaseException {
    try {
      acquirerBankRepository.deleteById(id);
    } catch (Exception e) {
      log.error("[deleteById] error", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }

  public Long getThreeDSOperatorIdNextVal() throws DatabaseException {
    try {
      return acquirerBankRepository.getThreeDSOperatorIdNextVal();
    } catch (Exception e) {
      log.error("[getThreeDSOperatorIdNextVal] error", e);
      throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }
}
