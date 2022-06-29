package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.CsrDAO;
import ocean.acs.models.data_object.entity.CsrDO;
import ocean.acs.models.data_object.entity.CsrDO.CertType;
import ocean.acs.models.oracle.entity.Csr;
import ocean.acs.models.oracle.repository.CsrRepository;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class CsrDAOImpl implements CsrDAO {

  private final CsrRepository csrRepository;

  @Override
  public CsrDO save(CsrDO csrDO) throws DatabaseException {
    try {
      return CsrDO.valueOf(csrRepository.save(Csr.valueOf(csrDO)));
    } catch (Exception e) {
      log.error("[save] error", e);
      throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }

  @Override
  public Optional<CsrDO> findByModulusSha256Hex(
      CertType certType,
      int threeDSVersion,
      Long issuerBankId,
      String cardBrand,
      String modulusSha256Hex)
      throws DatabaseException {
    try {
      return csrRepository
          .findByCertTypeAndThreeDSVersionAndIssuerBankIdAndCardBrandAndModulusSha256HexAndDeleteFlagFalse(
              certType, threeDSVersion, issuerBankId, cardBrand, modulusSha256Hex)
          .map(CsrDO::valueOf);
    } catch (Exception e) {
      log.error("[findByModulusSha256Hex] error", e);
      throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }

  @Override
  public List<CsrDO> findAvailableCertificateList(CertType certType, int threeDSVersion,
      Long issuerBankId, String cardBrand) throws DatabaseException {
    try {
      return csrRepository
          .findAvailableCertificateList(
              certType, threeDSVersion, issuerBankId, cardBrand, System.currentTimeMillis())
          .stream()
          .map(CsrDO::valueOf)
          .collect(Collectors.toList());
    } catch (Exception e) {
      log.error("[findAvailableCertificateList] error", e);
      throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }

  @Override
  public Optional<CsrDO> findById(Long id) throws DatabaseException {
    try {
      return csrRepository.findById(id).map(CsrDO::valueOf);
    } catch (Exception e) {
      log.error("[findById] error", e);
      throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }
}
