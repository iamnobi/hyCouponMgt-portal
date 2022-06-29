package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ReportDailyErrorReason;

@Repository
public interface ReportDailyErrorReasonRepository
        extends CrudRepository<ReportDailyErrorReason, Long> {

    List<ReportDailyErrorReason> findByYearAndMonth(int year, int month);

    List<ReportDailyErrorReason> findByYearAndMonthAndDayOfMonth(int year, int month, int day);

    Optional<ReportDailyErrorReason> findByIssuerBankIdAndYearAndMonthAndDayOfMonth(
            Long issuerBankId, int year, int month, int day);
}
