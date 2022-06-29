package ocean.acs.models.oracle.dao.impl;

import static java.lang.Long.max;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
import ocean.acs.models.oracle.entity.BlackListPan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class BlackListPanDAOImpl implements BlackListPanDAO {

    private static final String BLACK_LIST_QUERY_TOTAL =
            "select count(1) as ROW_COUNT from PAN_INFO p inner join BLACK_LIST_PAN b on p.ID = b.PAN_ID where 1=1 ";
    private static final String BLACK_LIST_QUERY =
            "select p.ID as PID, b.ID as BID, p.CARD_BRAND, p.CARD_NUMBER, p.CARD_NUMBER_EN, p.CARD_NUMBER_HASH, "
                    + "b.BLACK_LIST_PAN_BATCH_ID, b.CREATE_MILLIS, b.TRANS_STATUS, b.AUDIT_STATUS "
                    + "from PAN_INFO p " + "inner join BLACK_LIST_PAN b on p.ID = b.PAN_ID "
                    + "where 1=1 ";

    private static final String EXISTS_CARD_NUMBER_SQL =
            "select count(p.id) total from pan_info p\n"
                    + "join black_list_pan b on p.id = b.pan_id\n"
                    + "where issuer_bank_id=:issuerBankId and p.card_number_hash = :cardNumberHash and b.black_list_pan_batch_id = :batchId and b.delete_flag=0";


    private final ocean.acs.models.oracle.repository.BlackListPanRepository repo;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public boolean existsByPanInfoId(long panInfoId) throws DatabaseException {
        try {
            return repo.existsByPanIdAndDeleteFlagFalse(panInfoId);
        } catch (Exception e) {
            log.error("[existsByPanInfoId] unknown exception, panInfoId={}", panInfoId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
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

    private String orderByCreateMillisDesc() {
        return " order by b.create_millis desc";
    }

    @Override
    public PagingResultDO<BlackListQueryResultDO> queryPaginationBlackListPan(
            BlackListPanQueryDO queryDO) throws DatabaseException {
        Map<String, Object> params = new HashMap<>();
        String queryAllCondition = createBlackListQueryCondition(queryDO, params);

        String totalCmd = BLACK_LIST_QUERY_TOTAL + queryAllCondition;
        String queryCmd = BLACK_LIST_QUERY + queryAllCondition + orderByCreateMillisDesc();

        return queryPaginationResult(totalCmd, queryCmd, params, queryDO);
    }

    @Override
    public List<BlackListQueryResultDO> queryByBlackListId(List<Long> blackListIds)
            throws DatabaseException {

        String cardNumQueryCondition = "and b.ID in (:Ids)";

        String totalCmd = BLACK_LIST_QUERY_TOTAL + cardNumQueryCondition;
        String queryCmd = BLACK_LIST_QUERY + cardNumQueryCondition + orderByCreateMillisDesc();

        Map<String, Object> params = new HashMap<>();
        params.put("Ids", blackListIds);


        PagingResultDO<BlackListQueryResultDO> queryResult = queryPaginationResult(totalCmd,
                queryCmd, params, PageQueryDO.builder().pageSize(Integer.MAX_VALUE).build());
        return queryResult.getData();
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
            log.error(
                    "[existsByCardNumberHashAndBatchId] unknown exception, cardNumberHash={}, batchId={}",
                StringUtils.normalizeSpace(cardNumberHash),
                batchId, e);
            String errMsg = String.format(
                    "[existsByCardNumberHashAndBatchId] error message=%s, cardNumberHash=%s, batchId=%d",
                    e.getMessage(), cardNumberHash, batchId);
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
        return repo.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);
    }

    @Override
    public int deleteByPanInfoId(long panInfoId, String deleter, long deleteMillis) {
        return repo.deleteByPanInfoId(panInfoId, deleter, deleteMillis);
    }

    private static long convertUpdateMillis(Long updateMillis) {
        return null == updateMillis ? 0L : updateMillis;
    }

    private PagingResultDO<BlackListQueryResultDO> queryPaginationResult(String totalCmd,
            String queryCmd, Map<String, Object> parameters, PageQueryDO queryDO)
            throws DatabaseException {
        try {
            Long total = jdbcTemplate.queryForObject(totalCmd, parameters,
                    (rs, rowNum) -> rs.getLong(1));

            queryCmd = PageQuerySqlUtils.get(queryCmd);
            long startRowNumber =
                    PageQuerySqlUtils.getStartRowNumber(queryDO.getPage(), queryDO.getPageSize());
            long limit = PageQuerySqlUtils.getLimit(queryDO.getPage(), queryDO.getPageSize());
            parameters.put("startRowNumber", startRowNumber);
            parameters.put("limit", limit);

            List<BlackListQueryResultDO> list = jdbcTemplate.query(queryCmd, parameters,
                    (resultSet, i) -> convertResultSetToEntity(resultSet));

            PagingResultDO<BlackListQueryResultDO> pagingResultDO = new PagingResultDO<>(list);
            total = total == null ? 0L : total;
            pagingResultDO.setTotal(total);
            pagingResultDO.setCurrentPage(queryDO.getPage());
            pagingResultDO
                    .setTotalPages(PageQuerySqlUtils.getTotalPage(total, queryDO.getPageSize()));

            return pagingResultDO;
        } catch (Exception e) {
            log.error(
                    "[queryPaginationResult] unknown exception, "
                            + "totalCmd={}, queryCmd={}, parameters={}, pageQueryDTO={}",
                    totalCmd, queryCmd, parameters, queryDO, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    private BlackListQueryResultDO convertResultSetToEntity(ResultSet resultSet)
            throws SQLException {
        BlackListQueryResultDO queryResult = new BlackListQueryResultDO();
        queryResult.setPanId(resultSet.getString("PID"));
        queryResult.setId(resultSet.getString("BID"));
        queryResult.setAuthStatus(TransStatus.codeOf(resultSet.getString("TRANS_STATUS")).name());
        queryResult.setCardBrand(resultSet.getString("CARD_BRAND"));
        queryResult.setCardNumber(resultSet.getString("CARD_NUMBER"));
        queryResult.setEnCardNumber(resultSet.getString("CARD_NUMBER_EN"));
        queryResult.setCardNumberHash(resultSet.getString("CARD_NUMBER_HASH"));
        queryResult.setBlackListPanBatchId(resultSet.getLong("BLACK_LIST_PAN_BATCH_ID"));
        queryResult.setCreateMillis(resultSet.getLong("CREATE_MILLIS"));
        queryResult
                .setAuditStatus(AuditStatus.getStatusBySymbol(resultSet.getString("AUDIT_STATUS")));

        return queryResult;
    }

    private String createBlackListQueryCondition(BlackListPanQueryDO queryDO,
            Map<String, Object> params) {

        String queryAllCondition = "";

        if (null != queryDO.getIssuerBankId()) {
            queryAllCondition += "and p.ISSUER_BANK_ID=:issuerBankId ";
            params.put("issuerBankId", queryDO.getIssuerBankId());
        }
        if (StringCustomizedUtils.isNotEmpty(queryDO.getCardBrand())) {
            queryAllCondition += "and p.CARD_BRAND=:cardBrand ";
            params.put("cardBrand", queryDO.getCardBrand().trim().toUpperCase());
        }
        if (StringCustomizedUtils.isNotEmpty(queryDO.getCardNumberHash())) {
            queryAllCondition += "and p.CARD_NUMBER_HASH=:cardNumberHash ";
            params.put("cardNumberHash", queryDO.getCardNumberHash());
        }
        queryAllCondition += "and b.CREATE_MILLIS between :startTime and :endTime ";
        params.put("startTime", queryDO.getStartTime());
        params.put("endTime", queryDO.getEndTime());

        queryAllCondition += "and b.DELETE_FLAG=0 ";

        return queryAllCondition;
    }

}
