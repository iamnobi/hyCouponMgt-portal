package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.OtpSendingSetting;

@Repository
public interface OtpSendingSettingRepository extends CrudRepository<OtpSendingSetting, Long> {
    
    @Override
    @Query("select c from OtpSendingSetting c where deleteFlag=0")
    List<OtpSendingSetting> findAll();

    @Query(value = "select * from otp_sending_setting where id = :id and delete_flag = 0",
            nativeQuery = true)
    Optional<OtpSendingSetting> findByIdAndNotDelete(@Param("id") Long id);

    @Query(value = "select 1 from otp_sending_setting where issuer_bank_id = :issuerBankId and delete_flag = 0",
            nativeQuery = true)
    Optional<Integer> existByIssuerBankIdAndNotDelete(@Param("issuerBankId") Long issuerBankId);

    @Query(value = "select * from otp_sending_setting where issuer_bank_id = :issuerBankId and delete_flag = 0",
            nativeQuery = true)
    Optional<OtpSendingSetting> findByIssuerBankIdAndNotDelete(
            @Param("issuerBankId") Long issuerBankId);

    @Query(value = "select id from otp_sending_setting where issuer_bank_id = :issuerBankId and delete_flag = 0",
            nativeQuery = true)
    Optional<Long> findIdByIssuerBankIdAndNotDelete(@Param("issuerBankId") Long issuerBankId);

}
