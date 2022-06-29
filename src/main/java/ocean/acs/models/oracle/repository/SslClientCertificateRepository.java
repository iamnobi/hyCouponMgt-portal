package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.SslClientCertificate;

@Repository
public interface SslClientCertificateRepository extends CrudRepository<SslClientCertificate, Long> {

    @Override
    @Query("select s from SslClientCertificate s where s.deleteFlag = 0 and s.activity = 1")
    List<SslClientCertificate> findAll();

    @Query(value = "select * from SSL_CLIENT_CERTIFICATE where delete_flag=0 and ACTIVITY=?1 and CARD_BRAND=?2",
            nativeQuery = true)
    Optional<SslClientCertificate> findUnDeleteByActivityAndCardBrand(Integer activity,
            String cardBrandStr);

    @Query(value = "select * from SSL_CLIENT_CERTIFICATE where delete_flag=0 and CARD_BRAND=?1",
            nativeQuery = true)
    List<SslClientCertificate> findUnDeleteByCardBrand(String cardBrand);

    @Transactional
    @Modifying
    @Query(value = "update SSL_CLIENT_CERTIFICATE set DELETE_FLAG=1 where CARD_BRAND=?1",
            nativeQuery = true)
    int deleteByCardBrand(String cardBrand);

    @Query(value = "select * from SSL_CLIENT_CERTIFICATE where delete_flag=0 and ACTIVITY=?1 and CARD_BRAND=?2 and KEY_STATUS=?3",
            nativeQuery = true)
    Optional<SslClientCertificate> findUnDeleteByActivityAndCardBrandAndKeyStatus(Integer activity,
            String cardBrandStr, Integer keyStatus);

    @Transactional
    @Modifying
    @Query(value = "update SSL_CLIENT_CERTIFICATE set DELETE_FLAG=1 where delete_flag=0 and ACTIVITY=?1 and CARD_BRAND=?2 and KEY_STATUS=?3",
            nativeQuery = true)
    int deleteByActivityAndCardBrandAndKeyStatus(Integer activity, String cardBrand,
            Integer keyStatus);

    @Query(value = "select * from SSL_CLIENT_CERTIFICATE where delete_flag = 0 and id = ?1",
            nativeQuery = true)
    Optional<SslClientCertificate> findById(Long id);

}
