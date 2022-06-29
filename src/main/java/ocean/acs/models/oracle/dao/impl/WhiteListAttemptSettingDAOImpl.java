package ocean.acs.models.oracle.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.neovisionaries.i18n.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.WhiteListAttemptSettingDAO;
import ocean.acs.models.data_object.entity.WhiteListAttemptSettingDO;
import ocean.acs.models.data_object.kernel.WhiteListAttemptPanAuthStatusDO;
import ocean.acs.models.data_object.portal.GrantedLogQueryDO;
import ocean.acs.models.data_object.portal.GrantedTransactionLogDO;
import ocean.acs.models.oracle.entity.WhiteListAttemptSetting;

@Log4j2
@Repository
@AllArgsConstructor
public class WhiteListAttemptSettingDAOImpl implements WhiteListAttemptSettingDAO {

    private final ocean.acs.models.oracle.repository.WhiteListAttemptSettingRepository repo;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<WhiteListAttemptPanAuthStatusDO> getLatestWhiteListAttemptByPanInfoId(
            Long panInfoId) throws DatabaseException {
        Pageable page = PageRequest.of(0, 1, Sort.Direction.DESC, "createMillis");
        try {
            Page<WhiteListAttemptPanAuthStatusDO> result = repo.findByPanId(panInfoId, page);
            return result.stream().findFirst();
        } catch (Exception e) {
            log.error("[getLatestWhiteListAttemptIdByPanInfoId] unknown exception, panInfoId={}",
                    panInfoId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void updateTriesQuota(Long whiteListPanAttemptID, Integer triesQuota, String updater)
            throws DatabaseException {
        long now = System.currentTimeMillis();

        log.debug("[updateTriesQuota] whiteListPanAttemptID={}, triesQuota={}, updater={}",
                whiteListPanAttemptID, triesQuota, updater);
        try {
            repo.updatePermittedQuota(triesQuota, updater, now, whiteListPanAttemptID);
        } catch (Exception e) {
            log.error(
                    "[updateTriesQuota] unknown exception, whiteListPanAttemptID={}, triesQuota={},updater={}",
                    whiteListPanAttemptID, triesQuota, updater, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<WhiteListAttemptSettingDO> findOneByPanId(Long panId) {
        return repo.findByPanId(panId).map(WhiteListAttemptSettingDO::valueOf);
    }

    @Override
    public WhiteListAttemptSettingDO saveOrUpdate(WhiteListAttemptSettingDO settingDO) {
        WhiteListAttemptSetting whiteListAttemptSetting =
                WhiteListAttemptSetting.valueOf(settingDO);
        return WhiteListAttemptSettingDO.valueOf(repo.save(whiteListAttemptSetting));
    }

    @Override
    public Page<WhiteListAttemptSettingDO> findAttemptSettingByPanId(GrantedLogQueryDO logQueryDto,
            Pageable pageable) {
        return repo.findByIssuerBankIdAndPanId(logQueryDto.getIssuerBankId(),
                logQueryDto.getPanId(), pageable).map(WhiteListAttemptSettingDO::valueOf);
    }

    @Override
    public List<GrantedTransactionLogDO> getGrantedTransactionLog(Long attemptSettingId) {
        // 查詢 1.0 & 2.0 ATTEMPT_SETTING_ID = ?
        String queryCmd =
            "select *\n"
                + "from (\n"
                + "         (select k.CREATE_MILLIS      CREATE_MILLIS,\n"
                + "                 AL.PURCHASE_CURRENCY PURCHASE_CURRENCY,\n"
                + "                 AL.PURCHASE_AMOUNT   PURCHASE_AMOUNT,\n"
                + "                 AL.PURCHASE_EXPONENT PURCHASE_EXPONENT\n"
                + "          from KERNEL_TRANSACTION_LOG k\n"
                + "                   join AUTHENTICATION_LOG AL on k.AUTHENTICATION_LOG_ID = AL.ID\n"
                + "          where k.ATTEMPT_SETTING_ID = :attemptSettingId)\n"
                + "\n"
                + "         union\n"
                + "\n"
                + "         (select pa.CREATE_MILLIS         CREATE_MILLIS,\n"
                + "                 pa.PURCHASE_CURRENCY     PURCHASE_CURRENCY,\n"
                + "                 pa.PURCHASE_PURCH_AMOUNT PURCHASE_PURCH_AMOUNT,\n"
                + "                 pa.PURCHASE_EXPONENT     PURCHASE_EXPONENT\n"
                + "          from PA_LOG pa\n"
                + "          where pa.ATTEMPT_SETTING_ID = :attemptSettingId)\n"
                + "     )\n"
                + "order by CREATE_MILLIS desc";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("attemptSettingId", attemptSettingId);

        List<GrantedTransactionLogDO> list = jdbcTemplate.query(queryCmd, parameters,
                (resultSet, i) -> convertResultSetToEntity(resultSet));

        return list;
    }

    private GrantedTransactionLogDO convertResultSetToEntity(ResultSet resultSet)
            throws SQLException {
        GrantedTransactionLogDO queryResult = new GrantedTransactionLogDO();
        int purchaseExponent = resultSet.getInt("PURCHASE_EXPONENT");
        double purchaseAmount = resultSet.getDouble("PURCHASE_AMOUNT");
        if (purchaseExponent > 0) {
            purchaseAmount = purchaseAmount / (Math.pow(10, purchaseExponent));
        }
        queryResult.setAmount(purchaseAmount);
        String currencyName = resultSet.getString("PURCHASE_CURRENCY");
        if (StringUtils.isNumeric(currencyName)) {
            CurrencyCode currencyCode = CurrencyCode.getByCode(Integer.parseInt(currencyName));
            currencyName = currencyCode == null ? "unknown" : currencyCode.name();
        } else {
            currencyName = "";
        }
        queryResult.setCurrency(currencyName);
        queryResult.setExecuteMillis(resultSet.getLong("CREATE_MILLIS"));

        return queryResult;
    }

    @Override
    public int sumByTriesPermitted(Long issuerBankId, long startMillis, long endMillis) {
        try {
            Integer sum = repo.sumByTriesPermitted(issuerBankId, startMillis, endMillis);
            return sum == null ? 0 : sum;
        } catch (Exception e) {
            log.error(
                    "[sumByTriesPermitted] unknown exception, issuerBankId={}, startMillis={}, endMillis={}",
                    issuerBankId, startMillis, endMillis, e);
            return -1;
        }
    }

    @Override
    public int sumByTriesQuota(Long issuerBankId, long startMillis, long endMillis) {
        try {
            Integer sum = repo.sumByTriesQuota(issuerBankId, startMillis, endMillis);
            return sum == null ? 0 : sum;
        } catch (Exception e) {
            log.error(
                    "[sumByTriesQuota] unknown exception, issuerBankId={}, startMillis={}, endMillis={}",
                    issuerBankId, startMillis, endMillis, e);
            return -1;
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return repo.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);
    }

}
