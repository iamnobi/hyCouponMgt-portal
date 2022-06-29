package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.SmsTemplate;

@Repository
public interface SmsTemplateRepository extends CrudRepository<SmsTemplate, Long> {

    @Override
    @Query(value = "select * from sms_template where delete_flag=0 order by update_millis, create_millis desc nulls last",
            nativeQuery = true)
    List<SmsTemplate> findAll();

    @Query(value = "select * from sms_template t where issuer_Bank_Id = ?1 and delete_flag=0 order by update_millis, create_millis desc nulls last",
            nativeQuery = true)
    List<SmsTemplate> findByIssuerBankId(Long issuerBankId);

}
