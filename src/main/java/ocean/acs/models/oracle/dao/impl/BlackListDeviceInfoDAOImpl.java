package ocean.acs.models.oracle.dao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.constant.PortalMessageConstants;
import ocean.acs.commons.enumerator.DeviceChannel;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.BlackListDeviceInfoDAO;
import ocean.acs.models.data_object.entity.BlackListDeviceInfoDO;
import ocean.acs.models.data_object.portal.BlackListDeviceIdQueryDO;
import ocean.acs.models.data_object.portal.ComplexBlackListDeviceDO;
import ocean.acs.models.data_object.portal.IdsQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.oracle.entity.BlackListDeviceInfo;

@Log4j2
@Repository
@AllArgsConstructor
public class BlackListDeviceInfoDAOImpl implements BlackListDeviceInfoDAO {

    private static final String SELECT_DEIVE_ID_BY_TX_LOG_ID =
            "select tx.id as ID, tx.issuer_bank_id as ISSUER_BANK_ID, "
                    + "tx.device_id as DEVICE_ID, tx.device_channel as DEVICE_CHANNEL, "
                    + "auth.browser_user_agent as BROWSER_USER_AGENT, tx.ip as IP "
                    + "from kernel_transaction_log tx join authentication_log auth "
                    + "on tx.authentication_log_id = auth.id where tx.id in (:ids)";

    private final ocean.acs.models.oracle.repository.BlackListDeviceInfoRepository repo;
    private final ocean.acs.models.oracle.repository.TransactionLogRepository transactionLogRepo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    private final NamedParameterJdbcTemplate npJdbcTemplate;

    @Override
    public Optional<BlackListDeviceInfoDO> findByIssuerBankIdAndDeviceId(Long issuerBankId,
            String deviceId) throws DatabaseException {
        log.debug("[findByIssuerBankIdAndDeviceId] issuerBankId={}, deviceID={}", issuerBankId,
                deviceId);
        try {
            Optional<BlackListDeviceInfo> blacklistDeviceInfoOpt =
                    repo.findByIssuerBankIdAndDeviceId(issuerBankId, deviceId);
            return blacklistDeviceInfoOpt.map(BlackListDeviceInfoDO::valueOf);
        } catch (Exception e) {
            log.error("[findByIssuerBankIdAndDeviceId] system error, issuerBankId={}, deviceID={}",
                    issuerBankId, deviceId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public BlackListDeviceInfoDO save(BlackListDeviceInfoDO blackListDeviceDO) {
        BlackListDeviceInfo blackListDeviceInfo = BlackListDeviceInfo.valueOf(blackListDeviceDO);
        return BlackListDeviceInfoDO.valueOf(repo.save(blackListDeviceInfo));
    }

    @Override
    public List<BlackListDeviceInfoDO> saveAll(List<BlackListDeviceInfoDO> deviceInfoDOs) {
        List<BlackListDeviceInfo> entities = deviceInfoDOs.stream()
                .map(BlackListDeviceInfo::valueOf).collect(Collectors.toList());
        Iterable<BlackListDeviceInfo> iter = repo.saveAll(entities);
        return StreamSupport.stream(iter.spliterator(), false).map(BlackListDeviceInfoDO::valueOf)
                .collect(Collectors.toList());

    }

    @Override
    public boolean existsByIssuerBankIdAndDeviceIDAndNotDelete(Long issuerBankId, String deviceID) throws DatabaseException {
        try {
            return repo.existsByIssuerBankIdAndDeviceIDAndDeleteFlag(issuerBankId, deviceID, Boolean.FALSE);
        } catch (Exception e) {
            log.error("[existsByIssuerBankIdAndDeviceIDAndNotDelete] unknown exception, deviceID={}", deviceID, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<BlackListDeviceInfoDO> findByIdAndNotDelete(Long id) {
        return repo.findByIdAndNotDelete(id).map(BlackListDeviceInfoDO::valueOf);
    }

    @Override
    public List<ComplexBlackListDeviceDO> findByKernelTransactionLogIds(List<Long> txLogIDs) {
        Map<String, Object> params = new HashMap<>();
        params.put("ids", txLogIDs);

        try {
            List<ComplexBlackListDeviceDO> data = npJdbcTemplate.query(SELECT_DEIVE_ID_BY_TX_LOG_ID,
                    params, (ResultSet rs, int rowNum) -> {
                        return ComplexBlackListDeviceDO.builder().id(rs.getLong("ID"))
                                .issuerBankId(rs.getLong("ISSUER_BANK_ID"))
                                .deviceId(rs.getString("DEVICE_ID"))
                                .deviceChannel(rs.getString("DEVICE_CHANNEL"))
                                .browserUserAgent(rs.getString("BROWSER_USER_AGENT"))
                                .ip(rs.getString("IP")).build();
                    });

            return data;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("[findByTxLogID] unknown exception, txLogIDS={}",
                StringUtils.normalizeSpace(Arrays.toString(txLogIDs.toArray())), e);
            throw e;
        }
    }

    @Override
    public PagingResultDO<ComplexBlackListDeviceDO> query(BlackListDeviceIdQueryDO queryDO) {
        Page<BlackListDeviceInfo> deviceInfoPage = doQuery(queryDO);

        List<Integer> reasonCodeList = Arrays.asList(ResultStatus.BLACK_DEVICE_ID_C.getCode(),
                ResultStatus.BLACK_DEVICE_ID_N.getCode());
        List<ComplexBlackListDeviceDO> data = deviceInfoPage.getContent().stream().map(entity -> {
            ComplexBlackListDeviceDO.Builder builder = ComplexBlackListDeviceDO.builder()
                    .id(entity.getId()).issuerBankId(entity.getIssuerBankId())
                    .deviceId(entity.getDeviceID()).deviceChannel(entity.getDeviceChannel())
                    .browserUserAgent(entity.getBrowserUserAgent()).ip(entity.getIp())
                    .transStatus(entity.getTransStatus()).operator(entity.getCreator())
                    .auditStatus(entity.getAuditStatus()).createMillis(entity.getCreateMillis());

            Integer blockTimes = transactionLogRepo
                    .countByDeviceIDAndAresResultReasonCodeIn(entity.getDeviceID(), reasonCodeList);
            builder.blockTimes(blockTimes);

            return builder.build();
        }).collect(Collectors.toList());

        PagingResultDO<ComplexBlackListDeviceDO> response = PagingResultDO.valueOf(deviceInfoPage);
        response.setData(data);
        return response;
    }

    @Override
    public List<ComplexBlackListDeviceDO> findByIds(IdsQueryDO queryDO) {
        PageRequest page = PageRequest.of(0, 1000, Sort.Direction.DESC, "createMillis");
        List<BlackListDeviceInfo> dataList = repo.findByIdIn(queryDO.getIds(), page);
        if (dataList == null || dataList.isEmpty()) {
            return Collections.emptyList();
        }

        List<ComplexBlackListDeviceDO> resultList = new ArrayList<ComplexBlackListDeviceDO>();
        List<Integer> reasonCodeList = Arrays.asList(ResultStatus.BLACK_DEVICE_ID_C.getCode(),
                ResultStatus.BLACK_DEVICE_ID_N.getCode());
        for (BlackListDeviceInfo entity : dataList) {
            ComplexBlackListDeviceDO.Builder builder = ComplexBlackListDeviceDO.builder()
                    .id(entity.getId()).issuerBankId(entity.getIssuerBankId())
                    .deviceId(entity.getDeviceID()).deviceChannel(entity.getDeviceChannel())
                    .browserUserAgent(entity.getBrowserUserAgent()).ip(entity.getIp())
                    .transStatus(entity.getTransStatus()).operator(entity.getCreator())
                    .auditStatus(entity.getAuditStatus()).createMillis(entity.getCreateMillis());

            Integer blockTimes = transactionLogRepo
                    .countByDeviceIDAndAresResultReasonCodeIn(entity.getDeviceID(), reasonCodeList);
            builder.blockTimes(blockTimes);

            resultList.add(builder.build());
        }

        return resultList;
    }

    @Override
    public void updateTransStatusAndUpdaterByIssuerBankId(Long issuerBankId,
            TransStatus transStatus, String updater) {
        repo.updateTransStatusAndUpdaterAndUpdateMillisByIssuerBankIdAndNotDelete(issuerBankId,
                transStatus.getCode(), updater, System.currentTimeMillis());
    }

    @Override
    public void deleteByIds(Long id, String deleter) {
        repo.updateDeleteFlagAndDeleterAndDeleteMillisById(id, deleter, System.currentTimeMillis());
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(BlackListDeviceInfo.class, issuerBankId, deleter, deleteMillis);
    }

    private Page<BlackListDeviceInfo> doQuery(BlackListDeviceIdQueryDO queryDO) {
        PageRequest page = PageRequest.of(queryDO.getPage() - 1, queryDO.getPageSize(),
                Sort.Direction.DESC, "createMillis");

        Page<BlackListDeviceInfo> deviceInfoPage;
        try {
            deviceInfoPage = repo.findAll((Specification<BlackListDeviceInfo>) (deviceInfoRoot,
                    criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predList = new ArrayList<>();
                Predicate pred;

                Long startTime = queryDO.getStartTime();
                Long endTime = queryDO.getEndTime();
                if (startTime != null && endTime == null) {
                    pred = criteriaBuilder.ge(deviceInfoRoot.get("createMillis"), startTime);
                    predList.add(pred);
                } else if (startTime == null && endTime != null) {
                    pred = criteriaBuilder.le(deviceInfoRoot.get("createMillis"), endTime);
                    predList.add(pred);
                } else if (startTime != null && endTime != null) {
                    pred = criteriaBuilder.between(deviceInfoRoot.get("createMillis"), startTime,
                            endTime);
                    predList.add(pred);
                }

                pred = criteriaBuilder.equal(deviceInfoRoot.get("issuerBankId"),
                        queryDO.getIssuerBankId());
                predList.add(pred);

                String deviceChannels = queryDO.getDeviceChannels();
                if (null == deviceChannels || deviceChannels.isEmpty()) {
                    List<String> deviceChannelList = new ArrayList<String>();
                    DeviceChannel.stream().forEach(dc -> deviceChannelList.add(dc.getCode()));
                    pred = deviceInfoRoot.get("deviceChannel").in(deviceChannelList);
                } else {
                    pred = criteriaBuilder.equal(deviceInfoRoot.get("deviceChannel"),
                            deviceChannels);
                }
                predList.add(pred);

                pred = criteriaBuilder.equal(deviceInfoRoot.get("deleteFlag"), 0);
                predList.add(pred);

                Predicate[] predicates = new Predicate[predList.size()];
                return criteriaBuilder.and(predList.toArray(predicates));
            }, page);
        } catch (Exception e) {
            log.error("[doQuery] unknown exception, queryDto={}", queryDO, e);
            String errMsg = String.format("Query DeviceID, %s, error message: %s query params:%s",
                    PortalMessageConstants.DB_READ_ERROR, e.getMessage(), queryDO);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, errMsg);
        }

        return deviceInfoPage;
    }

}
