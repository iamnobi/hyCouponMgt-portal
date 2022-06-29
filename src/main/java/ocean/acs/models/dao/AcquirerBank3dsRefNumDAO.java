package ocean.acs.models.dao;

import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.oracle.entity.AcquirerBank3dsRefNum;

import java.util.List;

public interface AcquirerBank3dsRefNumDAO {
  List<AcquirerBank3dsRefNum> findAll() throws DatabaseException;

  void addAcquirerBank3dsRefNum(AcquirerBank3dsRefNum acquirerBank3dsRefNum) throws DatabaseException;

  void deleteAcquirerBank3dsRefNum(Long id) throws DatabaseException;
}