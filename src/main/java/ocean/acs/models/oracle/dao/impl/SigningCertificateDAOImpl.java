package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.SigningCertificateDAO;
import ocean.acs.models.data_object.entity.SigningCertificateDO;
import ocean.acs.models.oracle.entity.SigningCertificate;

@Repository
@AllArgsConstructor
public class SigningCertificateDAOImpl implements SigningCertificateDAO {

    private final ocean.acs.models.oracle.repository.SigningCertificateRepository repo;

    @Override
    public Optional<SigningCertificateDO> findByCardBrandAndBankId(
        int version,
        String cardBrand,
        Long issuerBankId) {
        return repo.findByCardBrandAndBankId(version, cardBrand, issuerBankId)
                .map(SigningCertificateDO::valueOf);
    }

    @Override
    public Optional<SigningCertificateDO> findByCardBrandAndIssuerBankId(
        int version,
        String cardBrand,
        Long issuerBankId) {
        return repo.findByCardBrandAndBankCode(version, cardBrand, issuerBankId)
                .map(SigningCertificateDO::valueOf);
    }

    @Override
    public boolean existsByCardBrand(int version, String cardBrand, Long issuerBankId) {
        return repo.existsByCardBrandAndIssuerBankId(version, cardBrand, issuerBankId);
    }

    @Override
    public SigningCertificateDO create(SigningCertificateDO signingCertificateDO) {
        SigningCertificate signingCertificate = SigningCertificate.valueOf(signingCertificateDO);
        signingCertificate.setUpdateMillis(System.currentTimeMillis());
        return SigningCertificateDO.valueOf(repo.save(signingCertificate));
    }

    @Override
    public SigningCertificateDO update(SigningCertificateDO signingCertificateDO) {
        SigningCertificate signingCertificate = SigningCertificate.valueOf(signingCertificateDO);
        signingCertificate.setUpdateMillis(System.currentTimeMillis());
        return SigningCertificateDO.valueOf(repo.save(signingCertificate));
    }

    @Override
    public Optional<SigningCertificateDO> findById(Long id) {
        return repo.findById(id).map(SigningCertificateDO::valueOf);
    }

    @Override
    public Optional<SigningCertificateDO> findJCBSigningCertificate(
        int version,
        String cardBrand,
        Long issuerBankId) {
        return repo.findJCBSigningCertificate(version, cardBrand, issuerBankId)
                .map(SigningCertificateDO::valueOf);
    }

    @Override
    public List<SigningCertificateDO> findAll() {
        return repo.findAll().stream().map(SigningCertificateDO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public boolean cleanSubCertificateId(int version, Long issuerBankId) {
        int result = repo.cleanSubCertificateId(version, issuerBankId);
        return result > 0;
    }

    @Override
    public boolean cleanRootCertificateId(int version, Long issuerBankId) {
        int result = repo.cleanRootCertificateId(version, issuerBankId);
        return result > 0;
    }

}
