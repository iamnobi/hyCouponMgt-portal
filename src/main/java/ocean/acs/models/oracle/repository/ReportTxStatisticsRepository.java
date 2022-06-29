package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO;
import ocean.acs.models.oracle.entity.ReportTxStatistics;

@Repository
public interface ReportTxStatisticsRepository extends CrudRepository<ReportTxStatistics, Long> {

    @Query("select new ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO(issuerBankId,year,month,dayOfMonth,total,otpCount,nCount,aCount,yCount,cyCount,cnCount,rCount,uCount) "
            + "from ReportTxStatistics report "
            + "where report.issuerBankId = ?1 and dataMillis between ?2 and ?3")
    List<PortalReportTxStatisticsDO> findByIssuerBankIdAndDataMillisBetween(Long issuerBankId,
            long startMillis, long endMillis);

    // 不撈出id <= 0以下的系統預設資料
    @Query("select new ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO(report.issuerBankId,report.year,report.month,report.dayOfMonth,report.total,report.otpCount,report.nCount,report.aCount,report.yCount,report.cyCount,report.cnCount,report.rCount,report.uCount) "
            + "from ReportTxStatistics report "
            + "where report.issuerBankId = (select bank.id from IssuerBank bank where bank.id > 0 and report.issuerBankId = bank.id and bank.deleteFlag = false) and report.dataMillis between ?1 and ?2 "
            + "order by report.issuerBankId asc, report.dayOfMonth asc")
    List<PortalReportTxStatisticsDO> findByDataMillisBetween(long startMillis, long endMillis);

    Optional<ReportTxStatistics> findByIssuerBankIdAndYearAndMonthAndDayOfMonth(Long bankId,
            int year, int month, int day);

    // 不撈出id <= 0以下的系統預設資料
    @Query("select new ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO(report.issuerBankId,report.year,report.month,report.dayOfMonth,report.total,report.otpCount,report.nCount,report.aCount,report.yCount,report.cyCount,report.cnCount,report.rCount,report.uCount) "
            + "from ReportTxStatistics report "
            + "where report.issuerBankId = (select bank.id from IssuerBank bank where bank.id > 0 and report.issuerBankId = bank.id and bank.deleteFlag = false) "
            + "and report.year = :year and report.month = :month "
            + "order by report.issuerBankId asc, report.dayOfMonth asc")
    List<PortalReportTxStatisticsDO> findByYearAndMonth(@Param("year") int year,
            @Param("month") int month);

    // 不撈出id <= 0以下的系統預設資料
    @Query("select new ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO(report.issuerBankId,report.year,report.month,report.dayOfMonth,report.total,report.otpCount,report.nCount,report.aCount,report.yCount,report.cyCount,report.cnCount,report.rCount,report.uCount) "
            + "from ReportTxStatistics report "
            + "where report.issuerBankId = (select bank.id from IssuerBank bank where bank.id > 0 and report.issuerBankId = bank.id and bank.deleteFlag = false) "
            + "and report.year = :year and report.month = :month and report.dayOfMonth = :day "
            + "order by report.issuerBankId asc, report.dayOfMonth asc")
    List<PortalReportTxStatisticsDO> findByYearAndMonthAndDay(@Param("year") int year,
            @Param("month") int month, @Param("day") int day);

    @Query("select new ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO(issuerBankId,year,month,dayOfMonth,total,otpCount,nCount,aCount,yCount,cyCount,cnCount,rCount,uCount) "
            + "from ReportTxStatistics report "
            + "where report.issuerBankId = :issuerBankId and report.year = :year and report.month = :month")
    List<PortalReportTxStatisticsDO> findByIssuerBankIdAndYearAndMonth(
            @Param("issuerBankId") long issuerBankId, @Param("year") int year,
            @Param("month") int month);

    @Query("select new ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO(issuerBankId,year,month,dayOfMonth,total,otpCount,nCount,aCount,yCount,cyCount,cnCount,rCount,uCount) "
            + "from ReportTxStatistics report "
            + "where report.issuerBankId = :issuerBankId and report.year = :year and report.month = :month and report.dayOfMonth = :day")
    List<PortalReportTxStatisticsDO> findByIssuerBankIdAndYearAndMonthAndDay(
            @Param("issuerBankId") long issuerBankId, @Param("year") int year,
            @Param("month") int month, @Param("day") int day);
}
