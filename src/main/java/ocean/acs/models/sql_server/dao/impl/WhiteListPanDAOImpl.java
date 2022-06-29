package ocean.acs.models.sql_server.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.commons.utils.PageQuerySqlUtils;
import ocean.acs.models.dao.WhiteListPanDAO;
import ocean.acs.models.data_object.entity.WhiteListPanDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.WhiteListPanCreateDO;
import ocean.acs.models.data_object.portal.WhiteListPanQueryDO;
import ocean.acs.models.data_object.portal.WhiteListQueryResultDO;
import ocean.acs.models.sql_server.entity.WhiteListPan;

@Log4j2
@Repository
@AllArgsConstructor
public class WhiteListPanDAOImpl implements WhiteListPanDAO {

    private static final String WHITE_LIST_QUERY_TOTAL = "select count(1) as row_count "
            + "from pan_info p inner join white_list_pan w on p.id = w.pan_id where w.delete_flag = 0 ";

    private static final String WHITE_LIST_QUERY_COLUMN = "select p.id as pid, w.id as wid, "
            + "p.card_brand as card_brand, p.card_number as card_number, p.card_number_hash as card_number_hash, "
            + "p.card_number_en as card_number_en, w.create_millis as create_millis, w.audit_status as audit_status "
            + "from pan_info p inner join white_list_pan w on p.id = w.pan_id where w.delete_flag = 0 ";

    private static final String IS_CARD_NUMBER_HASH_EXISTED_SQL =
            "select count(p.id) total from pan_info p "
                    + "join white_list_pan b on p.id = b.pan_id where p.issuer_bank_id = :issuerBankId and "
                    + "p.card_number_hash = :cardNumberHash and b.delete_flag = 0";

    private final ocean.acs.models.sql_server.repository.WhiteListPanRepository repo;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<Long> getWhiteListIdByPanInfoId(Long panInfoId) throws DatabaseException {
        Pageable page = PageRequest.of(0, 1, Sort.Direction.DESC, "createMillis");
        try {
            Page<Long> result = repo.findIdByPanId(panInfoId, page);
            return result.stream().findFirst();
        } catch (Exception e) {
            log.error("[getWhiteListIdByPanInfoId] unknown exception, panInfoId={}", panInfoId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public WhiteListPanDO add(WhiteListPanCreateDO createDO, Long panInfoId) {
        WhiteListPan whiteListPan = WhiteListPan.valueOf(createDO, panInfoId);
        return WhiteListPanDO.valueOf(repo.save(whiteListPan));
    }

    @Override
    public WhiteListPanDO update(WhiteListPanDO whiteListPanDO) {
        WhiteListPan whiteListPan = WhiteListPan.valueOf(whiteListPanDO);
        return WhiteListPanDO.valueOf(repo.save(whiteListPan));
    }

    @Override
    public Optional<WhiteListPanDO> update(WhiteListPanCreateDO whiteListPanCreateDO) {
        if (whiteListPanCreateDO == null) {
            log.error("[update] Failed in update white list pan due to missing argument content");
            throw new IllegalArgumentException(
                    "Failed in update white list pan due to missing argument content");
        }

        try {
            WhiteListPan whiteListPan = repo.findById(whiteListPanCreateDO.getId()).map(e -> {
                e.setAuditStatus(whiteListPanCreateDO.getAuditStatus().getSymbol());
                e.setUpdater(whiteListPanCreateDO.getUser());
                e.setUpdateMillis(System.currentTimeMillis());
                return e;
            }).map(repo::save).orElseThrow(() -> {
                log.error(
                        "[update] Failed in update white list pan due to unknown content with id={}",
                        whiteListPanCreateDO.getId());
                return new OceanExceptionForPortal("Command failed in missing target content.");
            });

            return Optional.of(WhiteListPanDO.valueOf(whiteListPan));
        } catch (Exception e) {
            log.error("[update] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public PagingResultDO<WhiteListQueryResultDO> query(WhiteListPanQueryDO queryDO) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        List<String> conditionList = new ArrayList<>();
        conditionList.add(" and w.create_millis between :startTime and :endTime");
        parameters.addValue("startTime", queryDO.getStartTime());
        parameters.addValue("endTime", queryDO.getEndTime());

        conditionList.add(" and p.issuer_bank_id = :issuerBankId");
        parameters.addValue("issuerBankId", queryDO.getIssuerBankId());

        if (StringUtils.isNotBlank(queryDO.getCardBrand())) {
            conditionList.add(" and p.card_brand = :cardBrand");
            parameters.addValue("cardBrand", queryDO.getCardBrand());
        }
        if (StringUtils.isNotBlank(queryDO.getCardNumberHash())) {
            conditionList.add(" and p.card_number_hash = :cardNumberHash");
            parameters.addValue("cardNumberHash", queryDO.getCardNumberHash());
        }
        String condition = String.join(" ", conditionList);

        // Query total counts.
        String totalCmd = WHITE_LIST_QUERY_TOTAL + condition;
        Long total =
                jdbcTemplate.queryForObject(totalCmd, parameters, (rs, rowNum) -> rs.getLong(1));

        // Pagination.
        String queryCmd = WHITE_LIST_QUERY_COLUMN + condition + this.orderByCreateMillisDesc();
        queryCmd = PageQuerySqlUtils.getPaginationSQLForQuery(queryCmd, queryDO.getPage().intValue(),
                queryDO.getPageSize().intValue());

        List<WhiteListQueryResultDO> list = jdbcTemplate.query(queryCmd.toString(), parameters,
                (resultSet, i) -> this.convertResultSetToDO(resultSet));

        PagingResultDO<WhiteListQueryResultDO> pagingResultDO = new PagingResultDO<>(list);
        pagingResultDO.setTotal(total == null ? 0 : total);
        pagingResultDO.setCurrentPage(queryDO.getPage());
        pagingResultDO.setTotalPages(PageQuerySqlUtils.getTotalPage(total, queryDO.getPageSize()));
        return pagingResultDO;
    }

    @Override
    public Optional<WhiteListPanDO> findById(Long id) {
        return repo.findById(id).map(WhiteListPanDO::valueOf);
    }

    @Override
    public PagingResultDO<WhiteListQueryResultDO> findByIds(List<Long> ids, Pageable pageable) {
        try {
            String idQueryCondition = "and w.id in (:ids)";
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("ids", ids);

            // Query total counts.
            String totalCmd = WHITE_LIST_QUERY_TOTAL + idQueryCondition;
            Long total = jdbcTemplate.queryForObject(totalCmd, parameters,
                    (rs, rowNum) -> rs.getLong(1));

            // Pagination.
            int pageIndex = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();
            String queryCmd =
                    WHITE_LIST_QUERY_COLUMN + idQueryCondition + this.orderByCreateMillisDesc();
            queryCmd = PageQuerySqlUtils.getPaginationSQLForQuery(queryCmd, pageIndex, pageSize);

            List<WhiteListQueryResultDO> list = jdbcTemplate.query(queryCmd, parameters,
                    (resultSet, i) -> this.convertResultSetToDO(resultSet));

            PagingResultDO<WhiteListQueryResultDO> pagingResultDO = new PagingResultDO<>(list);
            pagingResultDO.setTotal(total == null ? 0 : total);
            pagingResultDO.setCurrentPage(pageIndex);
            pagingResultDO.setTotalPages(PageQuerySqlUtils.getTotalPage(total, pageSize));
            return pagingResultDO;

        } catch (Exception e) {
            log.error("[findByIds] unknown exception, ids={}, page={}",
                StringUtils.normalizeSpace(Arrays.toString(ids.toArray())),
                StringUtils.normalizeSpace(pageable.toString()), e);
            return new PagingResultDO<>();
        }
    }

    @Override
    public boolean existsCardNumberHash(Long issuerBankId, String cardNumberHash) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("cardNumberHash", cardNumberHash);
        parameters.addValue("issuerBankId", issuerBankId);
        Integer total = jdbcTemplate.queryForObject(IS_CARD_NUMBER_HASH_EXISTED_SQL, parameters,
                Integer.class);
        return total != null && total > 0;
    }

    @Override
    public Optional<WhiteListPanDO> delete(DeleteDataDO deleteDataDO) {
        return repo.findById(deleteDataDO.getId()).map(e -> {
            e.setAuditStatus(deleteDataDO.getAuditStatus().getSymbol());
            e.setDeleteFlag(true);
            e.setDeleter(deleteDataDO.getUser());
            e.setDeleteMillis(System.currentTimeMillis());
            return e;
        }).map(repo::save).map(WhiteListPanDO::valueOf);
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        // TODO Implement method
        return 0;
    }

    private String orderByCreateMillisDesc() {
        return " order by w.create_millis desc";
    }

    private WhiteListQueryResultDO convertResultSetToDO(ResultSet resultSet) throws SQLException {
        WhiteListQueryResultDO queryResult = new WhiteListQueryResultDO();
        queryResult.setPanId(resultSet.getLong("pid"));
        queryResult.setId(resultSet.getLong("wid"));
        queryResult.setCardBrand(resultSet.getString("card_brand"));
        queryResult.setCardNumber(resultSet.getString("card_number"));
        queryResult.setCardNumberHash(resultSet.getString("card_number_hash"));
        queryResult.setCardNumberEn(resultSet.getString("card_number_en"));
        queryResult.setCreateMillis(resultSet.getLong("create_millis"));
        queryResult
                .setAuditStatus(AuditStatus.getStatusBySymbol(resultSet.getString("audit_status")));

        return queryResult;
    }
}
