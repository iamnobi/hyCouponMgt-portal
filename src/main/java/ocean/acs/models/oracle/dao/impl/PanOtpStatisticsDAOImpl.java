package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.PanOtpStatisticsDAO;
import ocean.acs.models.data_object.entity.PanOtpStatisticsDO;
import ocean.acs.models.oracle.entity.PanOtpStatistics;

@Log4j2
@Repository
@AllArgsConstructor
public class PanOtpStatisticsDAOImpl implements PanOtpStatisticsDAO {

    private final ocean.acs.models.oracle.repository.PanOtpStatisticsRepository repo;

    @Override
    public Optional<PanOtpStatisticsDO> findByPanInfoId(Long panInfoId) throws DatabaseException {
        try {
            return repo.findByPanInfoId(panInfoId).map(PanOtpStatisticsDO::valueOf);
        } catch (Exception e) {
            log.error("[findByPanInfoId] unknown exception, panInfoId={}", panInfoId, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean isOtpLock(Long panInfoId, Integer maxOtpVerifyCount) throws DatabaseException {
        try {
            return repo.isOtpLock(panInfoId, maxOtpVerifyCount);
        } catch (Exception e) {
            log.error("[isOtpLock] unknown exception, panInfoId={}, maxOtpVerifyCount={}",
                    panInfoId, maxOtpVerifyCount, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public boolean isExistsByPanInfoId(Long panInfoId) throws DatabaseException {
        try {
            return repo.existsByPanInfoIdAndDeleteFlagFalse(panInfoId);
        } catch (Exception e) {
            log.error("[isExistsByPanInfoId] unknown exception, PanInfoId='{}'", panInfoId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void increaseVerifyOtpCount(Long panInfoId, String updater) throws DatabaseException {
        try {
            repo.updateVerifyOtpCount(panInfoId, updater, System.currentTimeMillis());
        } catch (Exception e) {
            log.error("[increaseVerifyOtpCount] unknown exception, panInfoId={}", panInfoId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void initVerifyOtpCount(Long panInfoId, String updater) throws DatabaseException {
        try {
            repo.initVerifyOtpCount(panInfoId, updater, System.currentTimeMillis());
        } catch (Exception e) {
            log.error("[initVerifyOtpCount] unknown exception, panInfoId={}", panInfoId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public PanOtpStatisticsDO save(PanOtpStatisticsDO panOtpStatisticsDO) throws DatabaseException {
        try {
            PanOtpStatistics panOtpStatistics = PanOtpStatistics.valueOf(panOtpStatisticsDO);
            return PanOtpStatisticsDO.valueOf(repo.save(panOtpStatistics));
        } catch (Exception e) {
            log.error("[save] unknown exception, PanOtpStatistics={}", panOtpStatisticsDO, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void saveOrUpdate(PanOtpStatisticsDO panOtpStatisticsDO) throws DatabaseException {
        try {
            PanOtpStatistics panOtpStatistics = PanOtpStatistics.valueOf(panOtpStatisticsDO);
            repo.save(panOtpStatistics);
        } catch (Exception e) {
            log.error("[saveOrUpdate] unknown exception, PanOtpStatistics={}", panOtpStatisticsDO,
                    e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public List<PanOtpStatisticsDO> saveAll(List<PanOtpStatisticsDO> list)
            throws DatabaseException {
        try {
            List<PanOtpStatistics> PanOtpStatisticsDoList =
                    list.stream().map(PanOtpStatistics::valueOf).collect(Collectors.toList());
            Iterable<PanOtpStatistics> panOtpStatisticsIter = repo.saveAll(PanOtpStatisticsDoList);
            return StreamSupport.stream(panOtpStatisticsIter.spliterator(), false)
                    .map(PanOtpStatisticsDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[saveAll] unknown exception, list={}", list, e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return repo.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);
    }

}
