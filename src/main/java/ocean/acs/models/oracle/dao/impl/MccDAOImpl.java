package ocean.acs.models.oracle.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.MccDAO;
import ocean.acs.models.oracle.entity.Mcc;
import ocean.acs.models.oracle.repository.MccRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * MccDAOImpl
 */
@Log4j2
@AllArgsConstructor
@Service
public class MccDAOImpl implements MccDAO {

  private final MccRepository repository;
  @Override
  public boolean deleteAll() throws DatabaseException {
    try {
    repository.deleteAll();
    return true;
    } catch (Exception e) {
      throw new DatabaseException(
          ResultStatus.DB_SAVE_ERROR,
          e.getMessage(),
          e
      );
    }
  }

  @Override
  public boolean saveAll(Collection<String> list, String creator)
      throws DatabaseException {
    try {
      List<Mcc> mccList = Mcc.valueOf(list, creator);
      List<Mcc> resultList = repository.saveAll(mccList);
      return true;
    } catch (Exception e) {
      throw new DatabaseException(
          ResultStatus.DB_SAVE_ERROR,
          e.getMessage(),
          e
      );
    }
  }

  @Override
  public List<String> queryAll() throws DatabaseException {
    try {
    List<Mcc> mccList = repository.findAll(Sort.by(Sort.Direction.ASC, "mcc"));
    return mccList.stream().map(mcc -> mcc.getMcc())
        .collect(Collectors.toList());
    } catch (Exception e) {
      throw new DatabaseException(
          ResultStatus.DB_READ_ERROR,
          e.getMessage(),
          e
      );
    }
  }
}
