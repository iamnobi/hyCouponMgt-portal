package ocean.acs.models.dao;

import java.util.Collection;
import java.util.List;
import ocean.acs.commons.exception.DatabaseException;

/**
 * MccDAO
 */
public interface MccDAO {
  boolean deleteAll() throws DatabaseException;
  boolean saveAll(Collection<String> mccList, String creator) throws DatabaseException;
  List<String> queryAll() throws DatabaseException;
}
