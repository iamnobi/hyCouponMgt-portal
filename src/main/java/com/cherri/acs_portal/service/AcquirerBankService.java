package com.cherri.acs_portal.service;

import com.cherri.acs_portal.controller.request.AcquirerBankAddAcquirerBinRequest;
import com.cherri.acs_portal.controller.request.AcquirerBankCreateRequest;
import com.cherri.acs_portal.controller.request.AcquirerBankUpdateRequest;
import com.cherri.acs_portal.dto.AcquirerBankDTO;
import com.cherri.acs_portal.dto.AcquirerBinDTO;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.oracle.dao.impl.AcquirerBankAcquirerBinDAOImpl;
import ocean.acs.models.oracle.dao.impl.AcquirerBankDAOImpl;
import ocean.acs.models.oracle.entity.AcquirerBank;
import ocean.acs.models.oracle.entity.AcquirerBankAcquirerBin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class AcquirerBankService {

  private final AcquirerBankDAOImpl acquirerBankDAO;
  private final AcquirerBankAcquirerBinDAOImpl acquirerBankAcquirerBinDAO;

  public void createAcquirerBank(AcquirerBankCreateRequest request, String user) {
    try {
      //00003 allow control asc/3ds oper id by user
      /*AcquirerBank acquirerBank =
        new AcquirerBank(
          null,
          request.getName(),
          request.getThreeDSServerRefNumber(),
          this.create3DSOperatorId(),
          user,
          System.currentTimeMillis(),
          null,
          null);*/
      AcquirerBank acquirerBank =
              new AcquirerBank(
                      null,
                      request.getName(),
                      request.getThreeDSServerRefNumber(),
                      request.getThreeDSOperatorId(),
                      user,
                      System.currentTimeMillis(),
                      null,
                      null);

      acquirerBankDAO.save(acquirerBank);
    } catch (DatabaseException e) {
      throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
    }
  }

  @Transactional
  public void deleteAcquirerBank(Long id) {
    try {
      // delete acquirer bank
      acquirerBankDAO.deleteById(id);

      // delete acquirer bin
      acquirerBankAcquirerBinDAO.deleteByAcquirerBankId(id);
    } catch (DatabaseException e) {
      throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
    }
  }

  public void updateAcquirerBank(AcquirerBankUpdateRequest request, String user) {
    try {
      AcquirerBank acquirerBank =
          acquirerBankDAO
              .findById(request.getId())
              .orElseThrow(() -> new OceanException(ResultStatus.NO_SUCH_DATA));

      acquirerBank.setName(request.getName());
      acquirerBank.setThreeDSServerRefNumber(request.getThreeDSServerRefNumber());
      acquirerBank.setUpdater(user);
      acquirerBank.setUpdateMillis(System.currentTimeMillis());

      acquirerBankDAO.save(acquirerBank);

    } catch (DatabaseException e) {
      throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
    }
  }

  public List<AcquirerBankDTO> findAcquirerBankList() {
    try {
      return acquirerBankDAO.findAll().stream()
          .map(AcquirerBankDTO::valueOf)
          .collect(Collectors.toList());
    } catch (DatabaseException e) {
      throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
    }
  }

  public List<AcquirerBinDTO> findAcquirerBinByAcquirerBankId(Long acquirerBankId) {
    try {
      return acquirerBankAcquirerBinDAO.findByAcquirerBankId(acquirerBankId).stream()
          .map(AcquirerBinDTO::valueOf)
          .collect(Collectors.toList());
    } catch (DatabaseException e) {
      throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
    }
  }

  public void addAcquirerBin(AcquirerBankAddAcquirerBinRequest request, String user) {
    AcquirerBankAcquirerBin acquirerBin =
        new AcquirerBankAcquirerBin(
            null, request.getId(), request.getAcquirerBin(), user, System.currentTimeMillis());
    try {
      acquirerBankAcquirerBinDAO.save(acquirerBin);
    } catch (DatabaseException e) {
      throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
    }
  }

  public void deleteAcquirerBin(Long id) {
    try {
      acquirerBankAcquirerBinDAO.deleteById(id);
    } catch (DatabaseException e) {
      throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
    }
  }

  //00003 allow control asc/3ds oper id by user
  public boolean is3dsOperatorIdExist(AcquirerBankCreateRequest request){
    try
    {
      return acquirerBankDAO.findBythreeDSOperatorId(request.getThreeDSOperatorId()).isPresent();
    } catch (DatabaseException e) {
      throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
    }
  }

  private String create3DSOperatorId() throws DatabaseException {
    String newSequence = acquirerBankDAO.getThreeDSOperatorIdNextVal().toString();
    String new3DSOperatorId = "ITMX_3DS_" + StringUtils.leftPad(newSequence, 7, "0");

    log.info("[createAcquirerBank] new3DSOperatorId=" + new3DSOperatorId);
    return new3DSOperatorId;
  }
}
