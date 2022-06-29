package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO;
import ocean.acs.models.oracle.entity.ReportTxStatisticsDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportTxStatisticsDetailRepository
        extends CrudRepository<ReportTxStatisticsDetail, Long> {

    @Query(value = "select new ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO( t.cardBrandName,\n"
            + "sum(t.uRate), sum(t.nRate), sum(t.aresYCount), sum(t.aresNCount),\n"
            + "sum(t.aresUCount), sum(t.aresACount), sum(t.aresCCount),\n"
            + "sum(t.aresRCount), sum(t.rreqYCount), sum(t.rreqNCount),\n"
            + "sum(t.rreqUCount), sum(t.rreqACount), sum(t.rreqRCount))\n"
            + "from ReportTxStatisticsDetail t\n"
            + "where issuerBankId = ?1 and dataMillis between ?2 and ?3 group by cardBrandName")
    List<ReportTxStatisticsDetailDO> statisticByDataMillisBetween(Long issuerBankId,
            long startMillis, long endMillis);

    Optional<ReportTxStatisticsDetail> findByIssuerBankIdAndCardBrandNameAndYearAndMonthAndDayOfMonth(
            Long issuerBankId, String cardBrand, int year, int month, int day);

    @Query("select new ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO( t.cardBrandName,\n"
            + "sum(t.uRate), sum(t.nRate), sum(t.aresYCount), sum(t.aresNCount),\n"
            + "sum(t.aresUCount), sum(t.aresACount), sum(t.aresCCount),\n"
            + "sum(t.aresRCount), sum(t.rreqYCount), sum(t.rreqNCount),\n"
            + "sum(t.rreqUCount), sum(t.rreqACount), sum(t.rreqRCount))\n"
            + "from ReportTxStatisticsDetail t\n"
            + "where issuerBankId = :issuerBankId and year = :year and month = :month "
            + "group by cardBrandName")
    List<ReportTxStatisticsDetailDO> statisticByIssuerBankIdAndYearAndMonth(
            @Param("issuerBankId") long issuerBankId, @Param("year") int year,
            @Param("month") int month);

    @Query("select new ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO( t.cardBrandName,\n"
            + "sum(t.uRate), sum(t.nRate), sum(t.aresYCount), sum(t.aresNCount),\n"
            + "sum(t.aresUCount), sum(t.aresACount), sum(t.aresCCount),\n"
            + "sum(t.aresRCount), sum(t.rreqYCount), sum(t.rreqNCount),\n"
            + "sum(t.rreqUCount), sum(t.rreqACount), sum(t.rreqRCount))\n"
            + "from ReportTxStatisticsDetail t\n"
            + "where issuerBankId = :issuerBankId and year = :year and month = :month and dayOfMonth = :day "
            + "group by cardBrandName")
    List<ReportTxStatisticsDetailDO> statisticByIssuerBankIdAndYearAndMonthAndDay(
            @Param("issuerBankId") long issuerBankId, @Param("year") int year,
            @Param("month") int month, @Param("day") int day);

    @Query("select new ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO( t.cardBrandName,\n"
        + "sum(t.uRate), sum(t.nRate), sum(t.aresYCount), sum(t.aresNCount),\n"
        + "sum(t.aresUCount), sum(t.aresACount), sum(t.aresCCount),\n"
        + "sum(t.aresRCount), sum(t.rreqYCount), sum(t.rreqNCount),\n"
        + "sum(t.rreqUCount), sum(t.rreqACount), sum(t.rreqRCount))\n"
        + "from ReportTxStatisticsDetail t\n"
        + "where year = :year and month = :month "
        + "group by cardBrandName")
    List<ReportTxStatisticsDetailDO> statisticByYearAndMonth(
        @Param("year") int year,
        @Param("month") int month);

    @Query("select new ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO( t.cardBrandName,\n"
        + "sum(t.uRate), sum(t.nRate), sum(t.aresYCount), sum(t.aresNCount),\n"
        + "sum(t.aresUCount), sum(t.aresACount), sum(t.aresCCount),\n"
        + "sum(t.aresRCount), sum(t.rreqYCount), sum(t.rreqNCount),\n"
        + "sum(t.rreqUCount), sum(t.rreqACount), sum(t.rreqRCount))\n"
        + "from ReportTxStatisticsDetail t\n"
        + "where year = :year and month = :month and dayOfMonth = :day "
        + "group by cardBrandName")
    List<ReportTxStatisticsDetailDO> statisticByYearAndMonthAndDay(
        @Param("year") int year,
        @Param("month") int month,
        @Param("day") int day);
}
