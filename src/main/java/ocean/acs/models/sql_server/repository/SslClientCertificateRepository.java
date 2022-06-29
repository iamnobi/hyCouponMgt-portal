package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.SslClientCertificate;

@Repository
public interface SslClientCertificateRepository extends CrudRepository<SslClientCertificate, Long> {

    @Override
    @Query("select s from SslClientCertificate s where s.deleteFlag = 0 and s.activity = 1")
    List<SslClientCertificate> findAll();

    @Query(value = "select * from ssl_client_certificate where delete_flag = 0 and activity = ?1 and card_brand = ?2",
            nativeQuery = true)
    Optional<SslClientCertificate> findUnDeleteByActivityAndCardBrand(Integer activity,
            String cardBrandStr);

    @Query(value = "select * from ssl_client_certificate where delete_flag=0 and card_brand = ?1",
            nativeQuery = true)
    List<SslClientCertificate> findUnDeleteByCardBrand(String cardBrand);

    @Transactional
    @Modifying
    @Query(value = "update ssl_client_certificate set delete_flag = 1 where card_brand = ?1",
            nativeQuery = true)
    int deleteByCardBrand(String cardBrand);

    @Query(value = "select * from ssl_client_certificate where delete_flag = 0 and activity = ?1 and card_brand = ?2 and key_status = ?3",
            nativeQuery = true)
    Optional<SslClientCertificate> findUnDeleteByActivityAndCardBrandAndKeyStatus(Integer activity,
            String cardBrandStr, Integer keyStatus);

    @Transactional
    @Modifying
    @Query(value = "update ssl_client_certificate set delete_flag = 1 where delete_flag = 0 and activity = ?1 and card_brand = ?2 and key_status = ?3",
            nativeQuery = true)
    int deleteByActivityAndCardBrandAndKeyStatus(Integer activity, String cardBrand,
            Integer keyStatus);

    @Query(value = "select * from ssl_client_certificate where delete_flag = 0 and id = ?1",
            nativeQuery = true)
    Optional<SslClientCertificate> findById(Long id);

}
