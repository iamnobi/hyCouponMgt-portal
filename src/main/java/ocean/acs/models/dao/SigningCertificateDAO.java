package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.SigningCertificateDO;

public interface SigningCertificateDAO {

    Optional<SigningCertificateDO> findByCardBrandAndBankId(int version, String cardBrand, Long issuerBankId);

    Optional<SigningCertificateDO> findByCardBrandAndIssuerBankId(int version, String cardBrand,
            Long issuerBankId);

    boolean existsByCardBrand(int version, String cardBrand, Long issuerBankId);

    SigningCertificateDO create(SigningCertificateDO signingCertificateDO);

    SigningCertificateDO update(SigningCertificateDO signingCertificateDO);

    Optional<SigningCertificateDO> findById(Long id);

    Optional<SigningCertificateDO> findJCBSigningCertificate(int version, String cardBrand,
            Long issuerBankId);

    List<SigningCertificateDO> findAll();

    boolean cleanSubCertificateId(int version, Long issuerBankId);
    boolean cleanRootCertificateId(int version, Long issuerBankId);

}
