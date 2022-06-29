//package com.cherri.acs_portal.controller;
//
//import com.cherri.acs_portal.config.FiscProperties;
//import com.cherri.acs_portal.model.entity.AuditLog;
//import com.cherri.acs_portal.model.entity.Auditing;
//import com.cherri.acs_portal.model.entity.TransactionLog;
//import com.cherri.acs_portal.model.entity.transaction.AuthenticationLog;
//import com.cherri.acs_portal.model.entity.transaction.AuthenticationMeLog;
//import com.cherri.acs_portal.model.entity.transaction.ChallengeCodeLog;
//import com.cherri.acs_portal.model.entity.transaction.ChallengeLog;
//import com.cherri.acs_portal.model.entity.transaction.ChallengeMeLog;
//import com.cherri.acs_portal.model.entity.transaction.ChallengeSelectInfoLog;
//import com.cherri.acs_portal.model.entity.transaction.ErrorMessageLog;
//import com.cherri.acs_portal.model.entity.transaction.ResultLog;
//import com.cherri.acs_portal.model.entity.transaction.ResultMeLog;
//import com.cherri.acs_portal.model.entity.transaction.SdkUiTypeLog;
//import com.cherri.acs_portal.model.entity.transaction.ThreeDSMethodLog;
//import com.cherri.acs_portal.model.enumerator.MessageType;
//import com.cherri.acs_portal.model.repository.AuditLogRepository;
//import com.cherri.acs_portal.model.repository.AuditingRepository;
//import com.cherri.acs_portal.model.repository.AuthenticationLogRepository;
//import com.cherri.acs_portal.model.repository.AuthenticationMeLogRepository;
//import com.cherri.acs_portal.model.repository.ChallengeCodeLogRepository;
//import com.cherri.acs_portal.model.repository.ChallengeLogRepository;
//import com.cherri.acs_portal.model.repository.ChallengeMeLogRepository;
//import com.cherri.acs_portal.model.repository.ChallengeSelectInfoLogRepository;
//import com.cherri.acs_portal.model.repository.ErrorMessageLogRepository;
//import com.cherri.acs_portal.model.repository.ResultLogRepository;
//import com.cherri.acs_portal.model.repository.ResultMeLogRepository;
//import com.cherri.acs_portal.model.repository.SdkUiTypeLogRepository;
//import com.cherri.acs_portal.model.repository.ThreeDSMethodLogRepository;
//import com.cherri.acs_portal.model.repository.TransactionLogRepository;
//import com.cherri.acs_portal.service.HouseKeepingJobService;
//import com.cherri.acs_portal.util.AesUtils;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//import org.springframework.util.Base64Utils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * WARNING: This controller class is used for test Housekeeping feature only, <br>
// *     and should be removed after testing.
// */
//@RestController
//@RequestMapping("/housekeeping")
//public class HouseKeepingTestController {
//
//    @Autowired
//    @Qualifier("acsPortalNamedParameterJdbcTemplate")
//    NamedParameterJdbcTemplate acsJdbcTemplate;
//
//    @Autowired
//    @Qualifier("prod41NamedParameterJdbcTemplate")
//    NamedParameterJdbcTemplate prodJdbcTemplate;
//
//    @Autowired private ThreeDSMethodLogRepository threeDSMethodLogRepo;
//    @Autowired private AuthenticationLogRepository authenticationLogRepo;
//    @Autowired private SdkUiTypeLogRepository sdkUiTypeLogRepo;
//    @Autowired private AuthenticationMeLogRepository authenticationMeLogRepo;
//    @Autowired private ResultLogRepository resultLogRepo;
//    @Autowired private ResultMeLogRepository resultMeLogRepo;
//    @Autowired private ErrorMessageLogRepository errorMessageLogRepo;
//    @Autowired private TransactionLogRepository transactionLogRepo;
//    @Autowired private ChallengeLogRepository challengeLogRepo;
//    @Autowired private ChallengeSelectInfoLogRepository challengeSelectInfoLogRepo;
//    @Autowired private ChallengeMeLogRepository challengeMeLogRepo;
//    @Autowired private ChallengeCodeLogRepository challengeCodeLogRepo;
//    @Autowired private AuditingRepository auditingRepo;
//    @Autowired private AuditLogRepository auditLogRepo;
//
//    @Autowired private HouseKeepingJobService houseKeepingJobService;
//
//    @Autowired private FiscProperties fiscProperties;
//
//    private final String CREATOR = "test";
//    private final int CREATE_RECORD_COUNT = 5;
//
//    @GetMapping("/create-data")
//    public void createData() {
//        createTestData();
//    }
//
//    @GetMapping("/backup-data")
//    public void housekeeping() {
//        housekeepingTest();
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void createTestData(){
//        // 建立N天前的資料
//        Integer backupDays = fiscProperties.getHouseKeeping().getBackupDays();
//        for (int i = 0; i < CREATE_RECORD_COUNT; i++) {
//            createTestData(getCreateMillis(backupDays));
//        }
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void housekeepingTest() {
//
//        boolean result = houseKeepingJobService.housekeeping("Test");
//        Assert.isTrue(result, "HouseKeeping test error.");
//
//    }
//
//    private long getCreateMillis(long minusDays) {
//        return LocalDate.now().minusDays(minusDays).atStartOfDay(ZoneId.of("UCT")).toInstant().toEpochMilli();
//    }
//
//    /** 建立housekeeping測試資料 */
//    private void createTestData(long createMillis) {
//
//        // 交易 - kernel
//        createThreeDSMethodLog(createMillis);
//        long authLogId = createAuthenticationLog(createMillis);
//        createSdkUiTypeLog(authLogId, createMillis);
//        createAuthenticationMeLog(authLogId, createMillis);
//
//        long resultLogId = createResultLog(createMillis);
//        createResultMeLog(resultLogId, createMillis);
//        createErrorMessageLog(createMillis);
//
//        long kernelTxLogId = createKernelTransactionLog(createMillis);
//        long challengeLogId = createChallengeLog(kernelTxLogId, createMillis);
//
//        createChallengeSelectInfoLog(challengeLogId, createMillis);
//        createChallengeMeLog(challengeLogId, createMillis);
//        createChallengeCodeLog(challengeLogId, createMillis);
//
//        // 交易 - integrator
//        long integratorTxLogId = createIntegratorTransactionLog(createMillis);
//        createOtpGeneratedLog(integratorTxLogId, createMillis);
//        //    createCavvLog(kernelTxLogId, createMillis);
//
//        // Portal
//        createAuditing(createMillis);
//        createAuditLog(createMillis);
//    }
//
//    private long getIdInitialValue() {
//        List<Long> queryResult =
//                acsJdbcTemplate.query(
//                        "select id from kernel_transaction_log where id < 1 order by id asc",
//                        (rs, rowNum) -> rs.getLong("id"));
//
//        return queryResult.stream().findFirst().map(id -> id - 1).orElse(-1L);
//    }
//
//    private void createThreeDSMethodLog(long createMillis) {
//        ThreeDSMethodLog e = new ThreeDSMethodLog();
//        e.setThreeDSMethodNotificationURL("ThreeDSMethodNotificationURL");
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        threeDSMethodLogRepo.save(e);
//    }
//
//    private long createAuthenticationLog(long createMillis) {
//        AuthenticationLog e = new AuthenticationLog();
//
//        e.setThreeDSRequestorID("ThreeDSRequestorID");
//        e.setThreeDSRequestorName("ThreeDSRequestorName");
//        e.setThreeDSRequestorURL("ThreeDSRequestorURL");
//        e.setThreeDSServerRefNumber("hreeDSServerRefNumber");
//        e.setDsReferenceNumber("DsReferenceNumber");
//        e.setMessageCategory("01");
//        e.setMessageVersion("2.1.0");
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        return authenticationLogRepo.save(e).getId();
//    }
//
//    private void createSdkUiTypeLog(long authLogId, long createMillis) {
//        SdkUiTypeLog e = new SdkUiTypeLog();
//        e.setAuthenticationLogID(authLogId);
//        e.setSdkUiType("01");
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        sdkUiTypeLogRepo.save(e);
//    }
//
//    private void createAuthenticationMeLog(long authLogId, long createMillis) {
//        AuthenticationMeLog e = new AuthenticationMeLog();
//        e.setAuthenticationLogId(authLogId);
//        e.setMessageType(MessageType.AReq.name());
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        authenticationMeLogRepo.save(e);
//    }
//
//    private long createResultLog(long createMillis) {
//        ResultLog e = new ResultLog();
//        e.setInteractionCounter("01");
//        e.setMessageCategory("01");
//        e.setMessageVersion("2.1.0");
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        return resultLogRepo.save(e).getId();
//    }
//
//    private void createResultMeLog(long resultLogId, long createMillis) {
//        ResultMeLog e = new ResultMeLog();
//        e.setResultLogId(resultLogId);
//        e.setMessageType(MessageType.RReq.name());
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        resultMeLogRepo.save(e);
//    }
//
//    private void createErrorMessageLog(long createMillis) {
//        ErrorMessageLog e = new ErrorMessageLog();
//        e.setErrorCode(ResultStatus.ACCESS_DENIED_INVALID_ENDPOINT.getCode().toString());
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        errorMessageLogRepo.save(e);
//    }
//
//    private long createKernelTransactionLog(long createMillis) {
//        TransactionLog e = new TransactionLog();
//        e.setAcsTransID(UUID.randomUUID().toString());
//        e.setChallengeVerifyCount(1);
//        e.setChallengeResendCount(1);
//        e.setIp("127.0.0.1");
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        e.setSdkSessionKey(AesUtils.createAes256Key());
//        return transactionLogRepo.save(e).getId();
//    }
//
//    private long createChallengeLog(long txLogId, long createMillis) {
//        ChallengeLog e = new ChallengeLog();
//        e.setTransactionLogID(txLogId);
//        e.setMessageVersion("2.1.0");
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        return challengeLogRepo.save(e).getId();
//    }
//
//    private void createChallengeSelectInfoLog(long challengeLogId, long createMillis) {
//        ChallengeSelectInfoLog e = new ChallengeSelectInfoLog();
//        e.setChallengeLogId(challengeLogId);
//        e.setKey("Key");
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        challengeSelectInfoLogRepo.save(e);
//    }
//
//    private void createChallengeMeLog(long challengeLogId, long createMillis) {
//        ChallengeMeLog e = new ChallengeMeLog();
//        e.setChallengeLogId(challengeLogId);
//        e.setMessageType(MessageType.CReq.name());
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        challengeMeLogRepo.save(e);
//    }
//
//    private void createChallengeCodeLog(long challengeLogId, long createMillis) {
//        ChallengeCodeLog e = new ChallengeCodeLog();
//        e.setChallengeLogID(challengeLogId);
//        e.setVerifyCode(UUID.randomUUID().toString().substring(0, 6));
//        e.setVerifyStatus(0);
//        e.setAuthID(UUID.randomUUID().toString().substring(0, 4));
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        challengeCodeLogRepo.save(e);
//    }
//
//    private void createAuditing(long createMillis) {
//        Auditing e = new Auditing();
//        e.setIssuerBankId(-1L);
//        e.setFunctionType(AuditFunctionType.BANK_FEE.getTypeSymbol());
//        e.setActionType("A");
//        e.setAuditStatus("P");
//        e.setCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        e.setDeleteFlag(false);
//        auditingRepo.save(e);
//    }
//
//    private void createAuditLog(long createMillis) {
//        AuditLog e = new AuditLog();
//
//        e.setIssuerBankId(-1L);
//        e.setIp("127.0.0.1");
//        e.setMethodName("methodName");
//        e.setAction("0");
//        e.setErrorCode("200");
//        e.setSysCreator(CREATOR);
//        e.setCreateMillis(createMillis);
//        auditLogRepo.save(e);
//    }
//
//    private long createIntegratorTransactionLog(long createMillis) {
//        String insertSql =
//                "insert into integrator_transaction_log (id, integrator_id, acs_kernel_trans_id, bank_code, encrypt_otp_phone_number, encrypt_card_number, bincode, last_four, card_type, is_valid, create_millis, update_millis)"
//                        + " values (seq_integrator_transaction_log.nextVal, :integrator_id, :acs_kernel_trans_id, :bank_code, :encrypt_otp_phone_number, :encrypt_card_number, :bincode, :last_four, :card_type, :is_valid, :create_millis, :update_millis)";
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("integrator_id", UUID.randomUUID().toString());
//        params.addValue("acs_kernel_trans_id", UUID.randomUUID().toString());
//        params.addValue("bank_code", "999");
//        params.addValue("encrypt_otp_phone_number", "encrypt_otp_phone_number");
//        params.addValue("encrypt_card_number", "encrypt_card_number");
//        params.addValue("bincode", UUID.randomUUID().toString().substring(0, 6));
//        params.addValue("last_four", UUID.randomUUID().toString().substring(0, 4));
//        params.addValue("card_type", "CREDIT");
//        params.addValue("is_valid", 0);
//        params.addValue("create_millis", createMillis);
//        params.addValue("update_millis", createMillis);
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        acsJdbcTemplate.update(insertSql, params, keyHolder, new String[] {"id"});
//        return keyHolder.getKey().longValue();
//    }
//
//    private void createOtpGeneratedLog(long integratorId, long createMillis) {
//        String insertSql =
//                "insert into otp_generated_log (id, integrator_transaction_log_id, auth_id, auth_code, sending_mode, create_millis)"
//                        + " values (seq_otp_generated_log.nextVal, :integrator_transaction_log_id, :auth_id, :auth_code, :sending_mode, :create_millis)";
//        Map<String, Object> params = new HashMap<>();
//        params.put("integrator_transaction_log_id", integratorId);
//        params.put("auth_id", UUID.randomUUID().toString().substring(0, 8));
//        params.put("auth_code", UUID.randomUUID().toString().substring(0, 6));
//        params.put("sending_mode", "sms");
//        params.put("create_millis", createMillis);
//        acsJdbcTemplate.update(insertSql, params);
//    }
//
//    private void createCavvLog(long kernelTxLogId, long createMillis) {
//        String insertSql =
//                "insert into cavv_log (id, kernel_transaction_log_id, card_brand, cav_output, cavv, create_millis)"
//                        + " values (seq_cavv_log.nextVal, :kernel_transaction_log_id, :card_brand, :cav_output, :cavv, :create_millis)";
//        Map<String, Object> params = new HashMap<>();
//        params.put("kernel_transaction_log_id", kernelTxLogId);
//        params.put("card_brand", "VISA");
//        params.put(
//                "cav_output",
//                Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes()).substring(0, 5));
//        params.put(
//                "cavv",
//                Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes()).substring(0, 30));
//        params.put("create_millis", createMillis);
//        acsJdbcTemplate.update(insertSql, params);
//    }
//
//}
