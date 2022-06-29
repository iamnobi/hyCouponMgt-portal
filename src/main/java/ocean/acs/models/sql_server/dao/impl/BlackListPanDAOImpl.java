package ocean.acs.models.sql_server.dao.impl;

import static java.lang.Long.max;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.utils.PageQuerySqlUtils;
import ocean.acs.commons.utils.StringCustomizedUtils;
import ocean.acs.models.dao.BlackListPanDAO;
import ocean.acs.models.data_object.entity.BlackListPanDO;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.kernel.BlackListPanAuthStatusDO;
import ocean.acs.models.data_object.portal.BlackListPanQueryDO;
import ocean.acs.models.data_object.portal.BlackListQueryResultDO;
import ocean.acs.models.data_object.portal.PageQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.sql_server.entity.BlackListPan;

@Log4j2
@Repository
@AllArgsConstructor
public class BlackListPanDAOImpl implements BlackListPanDAO {

    private static final String BLACK_LIST_QUERY_TOTAL =
            "select count(1) as row_count from pan_info p "
                    + "inner join black_list_pan b on p.id = b.pan_id where 1=1 ";

    private static final String BLACK_LIST_QUERY =
            "select p.id as pid, b.id as bid, p.card_brand, p.card_number, p.card_number_en, p.card_number_hash, "
                    + "b.black_list_pan_batch_id, b.create_millis, b.trans_status, b.audit_status from pan_info p "
                    + "inner join black_list_pan b on p.id = b.pan_id where 1=1 ";

    private static final String EXISTS_CARD_NUMBER_SQL =
            "select count(p.id) as total from pan_info p "
                    + "join black_list_pan b on p.id = b.pan_id where issuer_bank_id=:issuerBankId and "
                    + "p.card_number_hash = :cardNumberHash and b.black_list_pan_batch_id = :batchId and b.delete_flag = 0";

    private final ocean.acs.models.sql_server.repository.BlackListPanRepository repo;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public boolean existsByPanInfoId(long panInfoId) {
        // TODO
        return false;
    }

    @Override
    public Optional<BlackListPanAuthStatusDO> getLatestByPanInfoId(Long panInfoId)
            throws DatabaseException {
        try {
            List<BlackListPanAuthStatusDO> result = repo.findByPanInfoId(panInfoId);

            // 若更新時間>建立時間，則以更新時間做降冪排序，否則依建立時間降冪排序
            return result.stream()
                    .max(Comparator.comparing(dto -> max(dto.getBlackListPanCreateMillis(),
                            convertUpdateMillis(dto.getBlackListPanBatchUpdateMillis()))));
        } catch (Exception e) {
            log.error("[getLatestByPanInfoId] unknown exception, panInfoId={}", panInfoId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<BlackListPanDO> add(PanInfoDO panInfoDo, long batchId, String user,
            TransStatus transStatus) {
        BlackListPan blackListPan = new BlackListPan();
        blackListPan.setPanId(panInfoDo.getId());
        blackListPan.setTransStatus(transStatus.getCode());
        blackListPan.setBlackListPanBatchId(batchId);
        blackListPan.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        blackListPan.setCreator(user);
        blackListPan.setCreateMillis(System.currentTimeMillis());

        blackListPan = repo.save(blackListPan);
        return Optional.of(BlackListPanDO.valueOf(blackListPan));
    }

    @Override
    public Optional<BlackListPanDO> add(BlackListPanDO blackListPanDO) {
        BlackListPan blackListPan = BlackListPan.valueOf(blackListPanDO);
        blackListPan = repo.save(blackListPan);
        return Optional.of(BlackListPanDO.valueOf(blackListPan));
    }

    @Override
    public void saveAll(List<BlackListPanDO> blackListPanDoList) {
        List<BlackListPan> blackListPanList =
                blackListPanDoList.stream().map(BlackListPan::valueOf).collect(Collectors.toList());
        repo.saveAll(blackListPanList);
    }

    @Override
    public BlackListPanDO saveOrUpdate(BlackListPanDO blackListPanDO) throws DatabaseException {
        try {
            BlackListPan blackListPan = BlackListPan.valueOf(blackListPanDO);
            return BlackListPanDO.valueOf(repo.save(blackListPan));
        } catch (Exception e) {
            String errMsg = String.format("error message=%s, updateData=%s", e.getMessage(),
                    blackListPanDO);
            log.error("[saveOrUpdate] unknown exception, errMsg={}", errMsg, e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, errMsg);
        }
    }

    @Override
    public List<BlackListQueryResultDO> queryBlackListPan(BlackListPanQueryDO queryDO)
            throws DatabaseException {
        Map<String, Object> params = new HashMap<>();
        String queryAllCondition = createBlackListQueryCondition(queryDO, params);
        String queryCmd = BLACK_LIST_QUERY + queryAllCondition + orderByCreateMillisDesc();
        try {
            return jdbcTemplate.query(queryCmd, params,
                    (resultSet, i) -> convertResultSetToEntity(resultSet));
        } catch (Exception e) {
            log.error("[queryBlackListPan] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public PagingResultDO<BlackListQueryResultDO> queryPaginationBlackListPan(
            BlackListPanQueryDO queryDO) throws DatabaseException {
        try {
            Map<String, Object> params = new HashMap<>();
            String queryAllCondition = createBlackListQueryCondition(queryDO, params);

            // Query total counts.
            String totalCmd = BLACK_LIST_QUERY_TOTAL + queryAllCondition;
            Long total =
                    jdbcTemplate.queryForObject(totalCmd, params, (rs, rowNum) -> rs.getLong(1));

            // Pagination.
            String queryCmd = BLACK_LIST_QUERY + queryAllCondition + this.orderByCreateMillisDesc();
            queryCmd = PageQuerySqlUtils.getPaginationSQLForQuery(queryCmd, queryDO.getPage().intValue(),
                    queryDO.getPageSize().intValue());

            List<BlackListQueryResultDO> list = jdbcTemplate.query(queryCmd.toString(), params,
                    (resultSet, i) -> convertResultSetToEntity(resultSet));

            PagingResultDO<BlackListQueryResultDO> pagingResultDO = new PagingResultDO<>(list);
            pagingResultDO.setTotal(total == null ? 0L : total);
            pagingResultDO.setCurrentPage(queryDO.getPage());
            pagingResultDO
                    .setTotalPages(PageQuerySqlUtils.getTotalPage(total, queryDO.getPageSize()));
            return pagingResultDO;

        } catch (Exception e) {
            log.error("[queryPaginationResult] error message={} pageQueryDTO={}", e.getMessage(),
                    queryDO, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public List<BlackListQueryResultDO> queryByBlackListId(List<Long> blackListIds)
            throws DatabaseException {
        try {
            String cardNumQueryCondition = "and b.id in (:ids)";
            Map<String, Object> params = new HashMap<>();
            params.put("ids", blackListIds);

            PageQueryDO queryDo = new PageQueryDO();
            queryDo.setPageSize(Integer.MAX_VALUE);

            // Query total counts.
            String totalCmd = BLACK_LIST_QUERY_TOTAL + cardNumQueryCondition;
            Long total =
                    jdbcTemplate.queryForObject(totalCmd, params, (rs, rowNum) -> rs.getLong(1));

            // Pagination.
            String queryCmd =
                    BLACK_LIST_QUERY + cardNumQueryCondition + this.orderByCreateMillisDesc();
            queryCmd = PageQuerySqlUtils.getPaginationSQLForQuery(queryCmd, queryDo.getPage().intValue(),
                    queryDo.getPageSize().intValue());

            List<BlackListQueryResultDO> list = jdbcTemplate.query(queryCmd.toString(), params,
                    (resultSet, i) -> convertResultSetToEntity(resultSet));

            PagingResultDO<BlackListQueryResultDO> pagingResultDO = new PagingResultDO<>(list);
            pagingResultDO.setTotal(total == null ? 0L : total);
            pagingResultDO.setCurrentPage(queryDo.getPage());
            pagingResultDO
                    .setTotalPages(PageQuerySqlUtils.getTotalPage(total, queryDo.getPageSize()));
            return pagingResultDO.getData();

        } catch (Exception e) {
            log.error("[queryPaginationResult] error message={}", e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean existsByCardNumberHashAndBatchId(Long issuerBankId, String cardNumberHash,
            Long batchId) throws DatabaseException {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("issuerBankId", issuerBankId);
        parameters.addValue("cardNumberHash", cardNumberHash);
        parameters.addValue("batchId", batchId);
        try {
            Integer total =
                    jdbcTemplate.queryForObject(EXISTS_CARD_NUMBER_SQL, parameters, Integer.class);
            return total != null && total > 0;
        } catch (Exception e) {
            String errMsg = String.format(
                    "[existsByCardNumberHashAndBatchId] error message=%s, cardNumberHash=%s, batchId=%d",
                    e.getMessage(), cardNumberHash, batchId);
            log.error(StringUtils.normalizeSpace(errMsg), e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, errMsg);
        }
    }

    @Override
    public Optional<BlackListPanDO> findById(Long id) throws DatabaseException {
        try {
            return repo.findById(id).map(BlackListPanDO::valueOf);
        } catch (Exception e) {
            log.error("[findById] unknown exception, id={}", id, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean updateTransStatusByBlackListPanBatchId(TransStatus transStatus, String updater,
            Long blackListPanBatchID) {
        try {
            repo.updateTransStatusByBlackListPanBatchId(transStatus.getCode(), updater,
                    System.currentTimeMillis(), blackListPanBatchID);
            return true;
        } catch (Exception e) {
            log.error(
                    "[updateTransStatusByBlackListPanBatchId] unknown exception, transStatus={}, update={}, blackListPanBatchID={}",
                    transStatus, updater, blackListPanBatchID, e);
        }
        return false;
    }

    @Override
    public boolean deleteByBlackListPanBatchId(String deleter, long blackListPanBatchID) {
        try {
            repo.deleteByBlackListPanBatchId(blackListPanBatchID, deleter,
                    System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            log.error(
                    "[deleteByBlackListPanBatchId] unknown exception, deleter={}, blackListPanBatchID={}",
                StringUtils.normalizeSpace(deleter),
                blackListPanBatchID, e);
        }
        return false;
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        // TODO Implement method
        return 0;
    }

    @Override
    public int deleteByPanInfoId(long panInfoId, String deleter, long deleteMillis) {
        // TODO Implement method
        return 0;
    }

    private static long convertUpdateMillis(Long updateMillis) {
        return null == updateMillis ? 0L : updateMillis;
    }

    private String createBlackListQueryCondition(BlackListPanQueryDO queryDO,
            Map<String, Object> params) {
        StringBuilder queryAllCondition = new StringBuilder();

        if (null != queryDO.getIssuerBankId()) {
            queryAllCondition.append("and p.issuer_bank_id = :issuerBankId ");
            params.put("issuerBankId", queryDO.getIssuerBankId());
        }
        if (StringCustomizedUtils.isNotEmpty(queryDO.getCardBrand())) {
            queryAllCondition.append("and p.card_brand = :cardBrand ");
            params.put("cardBrand", queryDO.getCardBrand().trim().toUpperCase());
        }
        if (StringCustomizedUtils.isNotEmpty(queryDO.getCardNumberHash())) {
            queryAllCondition.append("and p.card_number_hash = :cardNumberHash ");
            params.put("cardNumberHash", queryDO.getCardNumberHash());
        }

        queryAllCondition.append("and b.create_millis between :startTime and :endTime ");
        params.put("startTime", queryDO.getStartTime());
        params.put("endTime", queryDO.getEndTime());

        queryAllCondition.append("and b.delete_flag = 0 ");
        return queryAllCondition.toString();
    }

    private BlackListQueryResultDO convertResultSetToEntity(ResultSet resultSet)
            throws SQLException {
        BlackListQueryResultDO queryResult = new BlackListQueryResultDO();
        queryResult.setPanId(resultSet.getString("pid"));
        queryResult.setId(resultSet.getString("bid"));
        queryResult.setAuthStatus(TransStatus.codeOf(resultSet.getString("trans_status")).name());
        queryResult.setCardBrand(resultSet.getString("card_brand"));
        queryResult.setCardNumber(resultSet.getString("card_number"));
        queryResult.setEnCardNumber(resultSet.getString("card_number_en"));
        queryResult.setCardNumberHash(resultSet.getString("card_number_hash"));
        queryResult.setBlackListPanBatchId(resultSet.getLong("black_list_pan_batch_id"));
        queryResult.setCreateMillis(resultSet.getLong("create_millis"));
        queryResult
                .setAuditStatus(AuditStatus.getStatusBySymbol(resultSet.getString("audit_status")));
        return queryResult;
    }

    private String orderByCreateMillisDesc() {
        return " order by b.create_millis desc";
    }

}
