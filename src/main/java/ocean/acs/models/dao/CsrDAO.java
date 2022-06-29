package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.CsrDO;
import ocean.acs.models.data_object.entity.CsrDO.CertType;

public interface CsrDAO {
  CsrDO save(CsrDO csrDO) throws DatabaseException;

  Optional<CsrDO> findByModulusSha256Hex(
      CertType certType,
      int threeDSVersion,
      Long issuerBankId,
      String cardBrand,
      String modulusSha256Hex)
      throws DatabaseException;

  /**
   * Available:
   * - 未過期 (certExpireMillis)
   * - 有上傳憑證 (certificate != null)
   * - order by createMillis desc
   */
  List<CsrDO> findAvailableCertificateList(
      CertType certType,
      int threeDSVersion,
      Long issuerBankId,
      String cardBrand)
      throws DatabaseException;

  Optional<CsrDO> findById(Long id) throws DatabaseException;
}
