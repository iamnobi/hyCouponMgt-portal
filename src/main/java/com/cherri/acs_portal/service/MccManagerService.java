package com.cherri.acs_portal.service;

import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.MccDAO;
import org.springframework.stereotype.Service;

/**
 * MccManagerService
 */
@Log4j2
@Service
@AllArgsConstructor
public class MccManagerService {

    private final MccDAO mccDao;

  @Transactional
  public boolean saveMcc(Set<String> mccSet, String creator) {

    try {
      mccDao.deleteAll();
    } catch (DatabaseException e) {
      throw new OceanException(e.getResultStatus(), e.getMessage());
    }

    try {
      return mccDao.saveAll(mccSet, creator);
    } catch (DatabaseException e) {
      throw new OceanException(e.getResultStatus(), e.getMessage());
    }
  }

  public List<String> queryAllMcc() {
    try {
      return mccDao.queryAll();
    } catch (DatabaseException e) {
      throw new OceanException(e.getResultStatus(), e.getMessage());
    }
  }
}

