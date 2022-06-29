package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ocean.acs.models.sql_server.entity.SigningCertificate;

@Repository
public interface SigningCertificateRepository extends JpaRepository<SigningCertificate, Long> {

    @Query(value = "select sc from SigningCertificate sc "
            + "join IssuerBank ib on sc.issuerBankId = ib.id "
            + "where ib.id = :issuerBankId and sc.cardBrand = :cardBrand and ib.deleteFlag = false")
    Optional<SigningCertificate> findByCardBrandAndBankId(@Param("cardBrand") String cardBrand,
            @Param("issuerBankId") Long issuerBankId);

    @Query(value = "select sign from SigningCertificate sign "
            + "join IssuerBank bank on sign.issuerBankId = bank.id "
            + "where bank.id = :issuerBankId and sign.cardBrand = :cardBrand")
    Optional<SigningCertificate> findByCardBrandAndBankCode(@Param("cardBrand") String cardBrand,
            @Param("issuerBankId") Long issuerBankId);

    @Query(value = "select case when count(sign)> 0 then true else false end "
            + "from SigningCertificate sign " + "join IssuerBank bank on sign.issuerBankId = bank.id "
            + "where bank.id = :issuerBankId and sign.cardBrand = :cardBrand")
    Boolean existsByCardBrandAndIssuerBankId(@Param("cardBrand") String cardBrand,
            @Param("issuerBankId") Long issuerBankId);

    @Query(value = "select sign from SigningCertificate sign "
            + "join IssuerBank bank on sign.issuerBankId = bank.id "
            + "where bank.id = :issuerBankId and sign.cardBrand = :cardBrand")
    Optional<SigningCertificate> findJCBSigningCertificate(@Param("cardBrand") String cardBrand,
            @Param("issuerBankId") Long issuerBankId);

    @Transactional
    @Modifying
    @Query(value = "update SigningCertificate sign set sign.currentSubCaCertificateId = null "
            + "where sign.issuerBankId = (select id from IssuerBank bank where bank.id = :issuerBankId)")
    int cleanSubCertificateId(@Param("issuerBankId") Long issuerBankId);

}
