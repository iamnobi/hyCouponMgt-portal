package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ChallengeViewOtpSetting;

@Repository
public interface ChallengeViewOtpSettingRepository
        extends CrudRepository<ChallengeViewOtpSetting, Long> {

    @Query("select c from ChallengeViewOtpSetting c where deleteFlag=0 order by updateMillis, createMillis desc")
    List<ChallengeViewOtpSetting> findByNotDeleteAndOrderByUpdateMillisAndCreateMillisDes();

    Optional<ChallengeViewOtpSetting> findByIssuerBankIdAndDeleteFlagFalse(Long issuerBankId);

}
