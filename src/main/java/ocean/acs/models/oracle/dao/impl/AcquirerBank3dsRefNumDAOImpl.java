package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.AcquirerBank3dsRefNumDAO;
import ocean.acs.models.oracle.entity.AcquirerBank3dsRefNum;
import ocean.acs.models.oracle.repository.AcquirerBank3dsRefNumRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class AcquirerBank3dsRefNumDAOImpl implements AcquirerBank3dsRefNumDAO {

  private final AcquirerBank3dsRefNumRepository repo;

  @Override
  public List<AcquirerBank3dsRefNum> findAll() throws DatabaseException {
    try {
      return repo.findAll(Sort.by(Sort.Direction.ASC, "sdkReferenceNumber"));
    } catch (Exception e) {
      log.error("[findAll] error", e);
      throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }

  @Override
  public void addAcquirerBank3dsRefNum(AcquirerBank3dsRefNum acquirerBank3dsRefNum) throws DatabaseException {
    try {
      repo.save(acquirerBank3dsRefNum);
    } catch (Exception e) {
      log.error("[addAcquirerBank3dsRefNum] error", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }

  @Override
  public void deleteAcquirerBank3dsRefNum(Long id) throws DatabaseException {
    try {
      repo.deleteById(id);
    } catch (Exception e) {
      log.error("[deleteAcquirerBank3dsRefNum] error", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }
}
