package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.CsrDO.CertType;
import ocean.acs.models.oracle.entity.Csr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CsrRepository extends JpaRepository<Csr, Long> {

  Optional<Csr>
      findByCertTypeAndThreeDSVersionAndIssuerBankIdAndCardBrandAndModulusSha256HexAndDeleteFlagFalse(
          CertType certType,
          int threeDSVersion,
          Long issuerBankId,
          String cardBrand,
          String modulusSha256Hex);

  @Query("select c from Csr c "
      + "where "
        + "c.certType = :certType and "
        + "c.threeDSVersion = :threeDSVersion and "
        + "c.issuerBankId = :issuerBankId and "
        + "c.cardBrand = :cardBrand and "
        + "c.certificate is not null and "
        + "c.certExpireMillis > :currentMillis "
      + "order by c.createMillis desc ")
  List<Csr> findAvailableCertificateList(
      @Param("certType") CertType certType,
      @Param("threeDSVersion") int threeDSVersion,
      @Param("issuerBankId") Long issuerBankId,
      @Param("cardBrand") String cardBrand,
      @Param("currentMillis") Long currentMillis
  );
}
