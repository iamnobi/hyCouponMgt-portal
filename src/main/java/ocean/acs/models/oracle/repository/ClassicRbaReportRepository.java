package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ClassicRbaReport;

@Repository
public interface ClassicRbaReportRepository extends JpaRepository<ClassicRbaReport, Long> {

    List<ClassicRbaReport> findByYearAndMonthAndDayOfMonthAndDeleteFlagFalse(int year, int month,
            int dayOfMonth);
}
