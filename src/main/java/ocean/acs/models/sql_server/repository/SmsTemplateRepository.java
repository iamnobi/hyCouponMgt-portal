package ocean.acs.models.sql_server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.SmsTemplate;

@Repository
public interface SmsTemplateRepository extends CrudRepository<SmsTemplate, Long> {

    @Override
    @Query(value = "select * from sms_template where delete_flag = 0 order by update_millis, create_millis desc",
            nativeQuery = true)
    List<SmsTemplate> findAll();

    @Query(value = "select * from sms_template t where issuer_bank_id = ?1 and delete_flag = 0 order by update_millis, create_millis desc",
            nativeQuery = true)
    List<SmsTemplate> findByIssuerBankId(Long issuerBankId);

}
