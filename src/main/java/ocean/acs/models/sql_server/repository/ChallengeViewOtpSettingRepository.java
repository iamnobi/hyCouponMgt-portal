package ocean.acs.models.sql_server.repository;

import ocean.acs.models.sql_server.entity.ChallengeViewOtpSetting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeViewOtpSettingRepository
        extends CrudRepository<ChallengeViewOtpSetting, Long> {

    @Query("select c from ChallengeViewOtpSetting c where deleteFlag=0 order by updateMillis, createMillis desc")
    List<ChallengeViewOtpSetting> findByNotDeleteAndOrderByUpdateMillisAndCreateMillisDes();

    Optional<ChallengeViewOtpSetting> findByIssuerBankIdAndDeleteFlagFalse(Long issuerBankId);
}
