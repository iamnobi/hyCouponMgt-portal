package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ReportMonthErrorReason;

@Repository
public interface ReportMonthErrorReasonRepository
        extends CrudRepository<ReportMonthErrorReason, Long> {

    List<ReportMonthErrorReason> findByYearAndMonth(int year, int month);

    Optional<ReportMonthErrorReason> findByIssuerBankIdAndYearAndMonth(Long issuerBankId, int year,
            int month);
}
