package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.oracle.entity.AcquirerBankAcquirerBin;
import ocean.acs.models.oracle.repository.AcquirerBankAcquirerBinRepository;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class AcquirerBankAcquirerBinDAOImpl {

  private final AcquirerBankAcquirerBinRepository acquirerBankAcquirerBinRepository;

  public AcquirerBankAcquirerBin save(AcquirerBankAcquirerBin acquirerBankAcquirerBin)
      throws DatabaseException {
    try {
      return acquirerBankAcquirerBinRepository.save(acquirerBankAcquirerBin);
    } catch (Exception e) {
      log.error("[save] error", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }

  public void deleteById(Long id) throws DatabaseException {
    try {
      acquirerBankAcquirerBinRepository.deleteById(id);
    } catch (Exception e) {
      log.error("[deleteById] error", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }

  public void deleteByAcquirerBankId(Long id) throws DatabaseException {
    try {
      acquirerBankAcquirerBinRepository.deleteByAcquirerBankId(id);
    } catch (Exception e) {
      log.error("[deleteByAcquirerBankId] error", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }

  public List<AcquirerBankAcquirerBin> findByAcquirerBankId(Long acquirerBankId)
      throws DatabaseException {
    try {
      return acquirerBankAcquirerBinRepository.findByAcquirerBankId(acquirerBankId);
    } catch (Exception e) {
      log.error("[findByAcquirerBankId] error", e);
      throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }

  public Optional<AcquirerBankAcquirerBin> findByAcquirerBin(String acquirerBin) throws Exception {
    try {
      return acquirerBankAcquirerBinRepository.findByAcquirerBin(acquirerBin);
    } catch (Exception e) {
      log.error("[findByAcquirerBin] error", e);
      throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }
}
