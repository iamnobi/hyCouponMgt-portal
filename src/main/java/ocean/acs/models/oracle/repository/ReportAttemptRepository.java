package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.data_object.portal.PortalReportAttemptDO;
import ocean.acs.models.oracle.entity.ReportAttempt;

@Repository
public interface ReportAttemptRepository extends CrudRepository<ReportAttempt, Long> {

    @Query("select new ocean.acs.models.data_object.portal.PortalReportAttemptDO(issuerBankId,year,month,dayOfMonth,permittedCount,realTriesCount,percentage) "
            + "from ReportAttempt report "
            + "where report.issuerBankId = ?1 and dataMillis between ?2 and ?3 ")
    List<PortalReportAttemptDO> findByIssuerBankIdAndDataMillisBetween(Long issuerBankId,
            long startMillis, long endMillis);

    // 不撈出id <= 0以下的系統預設資料
    @Query("select new ocean.acs.models.data_object.portal.PortalReportAttemptDO(issuerBankId,year,month,dayOfMonth,permittedCount,realTriesCount,percentage) "
            + "from ReportAttempt report "
            + "where report.issuerBankId = (select id from IssuerBank bank where bank.id > 0 and report.issuerBankId = bank.id and deleteFlag = 0) and dataMillis between ?1 and ?2 "
            + "order by report.issuerBankId asc, report.dayOfMonth asc")
    List<PortalReportAttemptDO> findByDataMillisBetween(long startMillis, long endMillis);

    Optional<ReportAttempt> findByIssuerBankIdAndYearAndMonthAndDayOfMonth(Long issuerBankId,
            int year, int month, int day);

    // 不撈出id <= 0以下的系統預設資料
    @Query("select new ocean.acs.models.data_object.portal.PortalReportAttemptDO(issuerBankId,year,month,dayOfMonth,permittedCount,realTriesCount,percentage) "
            + "from ReportAttempt report "
            + "where report.issuerBankId = (select id from IssuerBank bank where bank.id > 0 and report.issuerBankId = bank.id and deleteFlag = 0) "
            + "and report.year = :year and report.month = :month "
            + "order by report.issuerBankId asc, report.dayOfMonth asc")
    List<PortalReportAttemptDO> findByYearAndMonth(@Param("year") int year,
            @Param("month") int month);

    // 不撈出id <= 0以下的系統預設資料
    @Query("select new ocean.acs.models.data_object.portal.PortalReportAttemptDO(issuerBankId,year,month,dayOfMonth,permittedCount,realTriesCount,percentage) "
            + "from ReportAttempt report "
            + "where report.issuerBankId = (select id from IssuerBank bank where bank.id > 0 and report.issuerBankId = bank.id and deleteFlag = 0) "
            + "and report.year = :year and report.month = :month and report.dayOfMonth = :day "
            + "order by report.issuerBankId asc, report.dayOfMonth asc")
    List<PortalReportAttemptDO> findByYearAndMonthAndDay(@Param("year") int year,
            @Param("month") int month, @Param("day") int day);

    @Query("select new ocean.acs.models.data_object.portal.PortalReportAttemptDO(issuerBankId,year,month,dayOfMonth,permittedCount,realTriesCount,percentage) "
            + "from ReportAttempt report "
            + "where report.issuerBankId = :issuerBankId and report.year = :year and report.month = :month")
    List<PortalReportAttemptDO> findByIssuerBankIdAndYearAndMonth(
            @Param("issuerBankId") long issuerBankId, @Param("year") int year,
            @Param("month") int month);

    @Query("select new ocean.acs.models.data_object.portal.PortalReportAttemptDO(issuerBankId,year,month,dayOfMonth,permittedCount,realTriesCount,percentage) "
            + "from ReportAttempt report "
            + "where report.issuerBankId = :issuerBankId and report.year = :year and report.month = :month and report.dayOfMonth = :day")
    List<PortalReportAttemptDO> findByIssuerBankIdAndYearAndMonthAndDay(
            @Param("issuerBankId") long issuerBankId, @Param("year") int year,
            @Param("month") int month, @Param("day") int day);
}
