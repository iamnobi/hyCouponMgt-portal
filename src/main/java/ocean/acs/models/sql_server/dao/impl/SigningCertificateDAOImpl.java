package ocean.acs.models.sql_server.dao.impl;//package ocean.acs.models.sql_server.dao.impl;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import org.springframework.stereotype.Repository;
//import lombok.AllArgsConstructor;
//import ocean.acs.models.dao.SigningCertificateDAO;
//import ocean.acs.models.data_object.entity.SigningCertificateDO;
//import ocean.acs.models.sql_server.entity.SigningCertificate;
//
//@Repository
//@AllArgsConstructor
//public class SigningCertificateDAOImpl implements SigningCertificateDAO {
//
//    private final ocean.acs.models.sql_server.repository.SigningCertificateRepository repo;
//
//    @Override
//    public Optional<SigningCertificateDO> findByCardBrandAndBankId(String cardBrand,
//            Long issuerBankId) {
//        return repo.findByCardBrandAndBankId(cardBrand, issuerBankId)
//                .map(SigningCertificateDO::valueOf);
//    }
//
//    @Override
//    public Optional<SigningCertificateDO> findByCardBrandAndIssuerBankId(String cardBrand,
//            Long issuerBankId) {
//        return repo.findByCardBrandAndBankCode(cardBrand, issuerBankId)
//                .map(SigningCertificateDO::valueOf);
//    }
//
//    @Override
//    public boolean existsByCardBrand(String cardBrand, Long issuerBankId) {
//        return repo.existsByCardBrandAndIssuerBankId(cardBrand, issuerBankId);
//    }
//
//    @Override
//    public SigningCertificateDO create(SigningCertificateDO signingCertificateDO) {
//        SigningCertificate signingCertificate = SigningCertificate.valueOf(signingCertificateDO);
//        signingCertificate.setUpdateMillis(System.currentTimeMillis());
//        return SigningCertificateDO.valueOf(repo.save(signingCertificate));
//    }
//
//    @Override
//    public SigningCertificateDO update(SigningCertificateDO signingCertificateDO) {
//        SigningCertificate signingCertificate = SigningCertificate.valueOf(signingCertificateDO);
//        signingCertificate.setUpdateMillis(System.currentTimeMillis());
//        return SigningCertificateDO.valueOf(repo.save(signingCertificate));
//    }
//
//    @Override
//    public Optional<SigningCertificateDO> findById(Long id) {
//        return repo.findById(id).map(SigningCertificateDO::valueOf);
//    }
//
//    @Override
//    public Optional<SigningCertificateDO> findJCBSigningCertificate(String cardBrand,
//            Long issuerBankId) {
//        return repo.findJCBSigningCertificate(cardBrand, issuerBankId)
//                .map(SigningCertificateDO::valueOf);
//    }
//
//    @Override
//    public List<SigningCertificateDO> findAll() {
//        return repo.findAll().stream().map(SigningCertificateDO::valueOf)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public boolean cleanSubCertificateId(Long issuerBankId) {
//        int result = repo.cleanSubCertificateId(issuerBankId);
//        return result > 0;
//    }
//
//}
