package com.cherri.acs_portal.service;

import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.config.ClassicRbaProperties;
import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.controller.request.TxSummaryReqDTO;
import com.cherri.acs_portal.controller.response.SmsRecordDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.acs_integrator.CipherReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.SmsReqDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.dto.hsm.DecryptResultDTO;
import com.cherri.acs_portal.dto.transactionLog.AuthRequestDto;
import com.cherri.acs_portal.dto.transactionLog.AuthResponseDto;
import com.cherri.acs_portal.dto.transactionLog.ChallengeRequestDto;
import com.cherri.acs_portal.dto.transactionLog.ChallengeResponseDto;
import com.cherri.acs_portal.dto.transactionLog.ClassicRbaResultDto;
import com.cherri.acs_portal.dto.transactionLog.DdcaLogResultDto;
import com.cherri.acs_portal.dto.transactionLog.ErrorMessageDto;
import com.cherri.acs_portal.dto.transactionLog.ResultRequestDto;
import com.cherri.acs_portal.dto.transactionLog.ResultResponseDto;
import com.cherri.acs_portal.dto.transactionLog.ThreeDSMethodDto;
import com.cherri.acs_portal.dto.transactionLog.TransactionLogDetailDto;
import com.cherri.acs_portal.dto.transactionLog.TxLogDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogExportDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderQueryDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderQueryExportDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogSummaryDTO;
import com.cherri.acs_portal.dto.veLog.VELogQueryResultDTO;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.manager.TransactionLogManager;
import com.cherri.acs_portal.util.DateUtil;
import com.cherri.acs_portal.util.ExcelBuildUtils;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.AuthenticationLogDAO;
import ocean.acs.models.dao.AuthenticationMeLogDAO;
import ocean.acs.models.dao.ChallengeCodeLogDAO;
import ocean.acs.models.dao.ChallengeLogDAO;
import ocean.acs.models.dao.ChallengeMeLogDAO;
import ocean.acs.models.dao.ChallengeSelectInfoLogDAO;
import ocean.acs.models.dao.DdcaLogDAO;
import ocean.acs.models.dao.ErrorMessageLogDAO;
import ocean.acs.models.dao.PanInfoDAO;
import ocean.acs.models.dao.ResultLogDAO;
import ocean.acs.models.dao.ResultMeLogDAO;
import ocean.acs.models.dao.SdkUiTypeLogDAO;
import ocean.acs.models.dao.ThreeDSMethodLogDAO;
import ocean.acs.models.dao.TransactionLogDAO;
import ocean.acs.models.data_object.entity.AuthenticationMeLogDO;
import ocean.acs.models.data_object.entity.ChallengeCodeLogDO;
import ocean.acs.models.data_object.entity.ChallengeMeLogDO;
import ocean.acs.models.data_object.entity.ChallengeSelectInfoLogDO;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.entity.ResultMeLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class TransactionLogService {

    private final TransactionLogManager txLogManager;
    private final TransactionLogDAO txLogDao;
    private final PanInfoDAO panInfoDAO;

    private final ThreeDSMethodLogDAO methLogDao;
    private final AuthenticationLogDAO authLogDao;
    private final AuthenticationMeLogDAO authMeLogDao;
    private final SdkUiTypeLogDAO sdkUiTypeLogDao;

    private final ChallengeLogDAO challengeLogDao;
    private final ChallengeMeLogDAO challengeMeLogDao;
    private final ChallengeCodeLogDAO challengeCodeLogDao;
    private final ChallengeSelectInfoLogDAO challengeSelectInfoLogDao;

    private final ResultLogDAO resultLogDao;
    private final ResultMeLogDAO resultMeLogDao;

    private final ErrorMessageLogDAO errMsgLogDao;

    private final AcsIntegratorManager acsIntegratorManager;
    private final HsmPlugin hsmPlugin;
    private final DdcaLogDAO ddcaLogDao;
    private final ClassicRbaProperties classicRbaProperties;

    private List<String> getColumnNames() {
        return Arrays.asList(
            MessageConstants.get(MessageConstants.EXCEL_VE_LOG_ISSUER_NAME),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_AUTHENTICATION_DATE),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_PAN),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_AMOUNT),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_CURRENCY),
            "ARes",
            "RRes",
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_DEVICE_CHANNEL),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_PURCHASE_TIME),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_MERCHANT_NAME),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_MERCHANT_COUNTRY_CODE),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_CHALLENGE_RESULT),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_RBA_RESULT),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_USER_AGENT));
    }

    private List<String> getColumnNamesVelog() {
        return Arrays.asList(
            MessageConstants.get(MessageConstants.EXCEL_VE_LOG_ISSUER_NAME),
            MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG_PAN),
            MessageConstants.get(MessageConstants.EXCEL_VE_LOG_VEREQ_TIME),
            MessageConstants.get(MessageConstants.EXCEL_VE_LOG_VERES),
            MessageConstants.get(MessageConstants.EXCEL_VE_LOG_MERCHANT_ID),
            MessageConstants.get(MessageConstants.EXCEL_VE_LOG_PROXY_PAN),
            MessageConstants.get(MessageConstants.EXCEL_VE_LOG_IREQ_CODE),
            MessageConstants.get(MessageConstants.EXCEL_VE_LOG_IS_PAREQ_RECEIVED));
    }

    @Autowired
    public TransactionLogService(
        TransactionLogManager txLogManager,
        TransactionLogDAO txLogDao,
        PanInfoDAO panInfoDAO,
        ThreeDSMethodLogDAO methLogDao,
        AuthenticationLogDAO authLogDao,
        AuthenticationMeLogDAO authMeLogDao,
        SdkUiTypeLogDAO sdkUiTypeLogDao,
        ChallengeLogDAO challengeLogDao,
        ChallengeMeLogDAO challengeMeLogDao,
        ChallengeCodeLogDAO challengeCodeLogDao,
        ChallengeSelectInfoLogDAO challengeSelectInfoLogDao,
        ResultLogDAO resultLogDao,
        ResultMeLogDAO resultMeLogDao,
        ErrorMessageLogDAO errMsgLogDao,
        AcsIntegratorManager acsIntegratorManager,
        HsmPlugin hsmPlugin, DdcaLogDAO ddcaLogDao,
        ClassicRbaProperties classicRbaProperties) {
        this.txLogManager = txLogManager;
        this.txLogDao = txLogDao;
        this.panInfoDAO = panInfoDAO;
        this.methLogDao = methLogDao;
        this.authLogDao = authLogDao;
        this.authMeLogDao = authMeLogDao;
        this.sdkUiTypeLogDao = sdkUiTypeLogDao;
        this.challengeLogDao = challengeLogDao;
        this.challengeMeLogDao = challengeMeLogDao;
        this.challengeCodeLogDao = challengeCodeLogDao;
        this.challengeSelectInfoLogDao = challengeSelectInfoLogDao;
        this.resultLogDao = resultLogDao;
        this.resultMeLogDao = resultMeLogDao;
        this.errMsgLogDao = errMsgLogDao;
        this.acsIntegratorManager = acsIntegratorManager;
        this.hsmPlugin = hsmPlugin;
        this.ddcaLogDao = ddcaLogDao;
        this.classicRbaProperties = classicRbaProperties;
    }

    public List<TxLogDTO> findByIds(List<Long> ids) {
        List<TransactionLogDO> txLogs = txLogDao.findByIds(ids);
        if (txLogs == null || txLogs.isEmpty()) {
            return Collections.emptyList();
        }
        List<TxLogDTO> dtoList =
          txLogs.stream()
            .map(
              entity -> {
                  TxLogDTO dto = new TxLogDTO();
                  BeanUtils.copyProperties(entity, dto);
                  return dto;
              })
            .collect(Collectors.toList());
        return dtoList;
    }

    /** 取得交易紀錄 */
    public PagingResultDTO<TxLogHeaderDTO> getLogs(TxLogHeaderQueryDTO queryDto, boolean canSeePan)
      throws OceanException {
        PagingResultDTO<TxLogHeaderDTO> logResult = txLogManager.getLogs(queryDto, canSeePan);
        if (logResult == null || logResult.getData().isEmpty()) {
            return PagingResultDTO.empty();
        }
        return processMaskCardNumberOfLogResult(logResult, queryDto.getPan());
    }


    private PagingResultDTO<TxLogHeaderDTO> processMaskCardNumberOfLogResult(
      PagingResultDTO<TxLogHeaderDTO> logResult, String queryPan) {
        Objects.requireNonNull(logResult);
        Objects.requireNonNull(logResult.getData());

        return logResult; // masked cardNumber
    }

    private void unmaskCardNumberHack(List<TxLogHeaderDTO> logList, String queryPan) {
        if (logList == null || logList.size() < 1 || StringUtils.isBlank(queryPan)) {
            return;
        }
        // hack: using query card number to response the result
        for (TxLogHeaderDTO logDTO : logList) {
            logDTO.setPan(queryPan);
        }
    }

    private CipherReqDTO getCipherReqDTOForEncryptedCardNumbers(List<TxLogHeaderDTO> logList) {
        CipherReqDTO cipherReqDto = new CipherReqDTO();
        logList.stream()
          .filter(log -> StringUtils.isNotBlank(log.getEnCardNumber()))
          .forEach(log -> cipherReqDto.addData(log.getId().toString(), log.getEnCardNumber()));
        return cipherReqDto;
    }

    /** 取得交易紀錄摘要 */
    public Optional<TxLogSummaryDTO> getSummary(TxSummaryReqDTO queryDto) {
        return txLogManager.getSummary(queryDto);
    }

    public Optional<TransactionLogDetailDto> getTransactionLogDetail(Long transactionLogId) {
        try {
            TransactionLogDetailDto transactionLogDetailDto = new TransactionLogDetailDto();
            txLogDao.findById(transactionLogId)
              .ifPresent(
                txLog -> {
                    setThreeDsMethodLog(txLog, transactionLogDetailDto);
                    setAReqAndAres(txLog, transactionLogDetailDto);
                    setCReqAndCRes(txLog, transactionLogDetailDto);
                    setRReqAndRRes(txLog, transactionLogDetailDto);
                    setErrorMessage(txLog, transactionLogDetailDto);
                    setDdcaLogResult(txLog, transactionLogDetailDto);
                    setClassicRbaResult(txLog, transactionLogDetailDto);
                });
            return Optional.of(transactionLogDetailDto);
        } catch (Exception e) {
            log.error(
              "[getTransactionLogDetail] unknown exception, request params transactionLogId={}",
              transactionLogId,
              e);
        }
        return Optional.empty();
    }

    private void setThreeDsMethodLog(
      TransactionLogDO txLog, TransactionLogDetailDto transactionLogDetailDto) {
        if (txLog.getThreeDSMethodLogId() == null) {
            return;
        }
        methLogDao
          .findById(txLog.getThreeDSMethodLogId())
          .ifPresent(
            methodLog ->
              transactionLogDetailDto.setThreeDSMethod(
                ThreeDSMethodDto.valueOf(methodLog)));
    }

    private void setAReqAndAres(TransactionLogDO txLog, TransactionLogDetailDto txLogDetailDto) {
        if (txLog.getAuthenticationLogID() == null) {
            return;
        }

        try {
            authLogDao
              .findById(txLog.getAuthenticationLogID())
              .ifPresent(
                authLog -> {
                    String maskedAcctNumber;
                    try {
                        maskedAcctNumber = panInfoDAO
                            .findById(txLog.getPanInfoId())
                            .map(PanInfoDO::getCardNumber)
                            .orElse("");
                    } catch (DatabaseException e) {
                        log.error("[setAReqAndAres] find maskedAcctNumber error", e);
                        maskedAcctNumber = "";
                    }
                    List<AuthenticationMeLogDO> aReqAuthMeLogs =
                      authMeLogDao.findByAuthLogId(
                        authLog.getId(), MessageType.AReq);
                    List<AuthenticationMeLogDO> aResAuthMeLogs =
                      authMeLogDao.findByAuthLogId(
                        authLog.getId(), MessageType.ARes);
                    List<String> sdkUiTypeList =
                      sdkUiTypeLogDao.findByAuthLogId(authLog.getId());
                    AuthRequestDto aReq =
                      AuthRequestDto.valueOf(
                        txLog, authLog, aReqAuthMeLogs, sdkUiTypeList, maskedAcctNumber);
                    AuthResponseDto aRes =
                      AuthResponseDto.valueOf(authLog, aResAuthMeLogs, txLog);
                    // Decrypt AReq device info
                    if (StringUtils.isNotEmpty(authLog.getDeviceInfoEn())) {
                        DecryptResultDTO decryptResultDTO = null;
                        try {
                            decryptResultDTO = hsmPlugin
                                .decryptSensitiveDataWithIssuerBankId(authLog.getDeviceInfoEn(),
                                    authLog.getIssuerBankId());
                            aReq.setDeviceInfo(decryptResultDTO.getString());
                        } catch (Exception e) {
                            log.error(
                                "[setAReqAndAres] decrypt AReq device info failed. AuthenticationLog.ID={}",
                                authLog.getId(), e);
                            aReq.setDeviceInfo("Decrypt failed");
                        } finally {
                            if (decryptResultDTO != null) {
                                decryptResultDTO.clearPlainText();
                            }
                        }
                    }
                    // Decrypt ARes cavv
                    if (StringUtils.isNotEmpty(authLog.getAuthenticationValueEn())) {
                        DecryptResultDTO decryptResultDTO = null;
                        try {
                            decryptResultDTO = hsmPlugin
                                .decryptSensitiveDataWithIssuerBankId(authLog.getAuthenticationValueEn(),
                                    authLog.getIssuerBankId());
                            aRes.setAuthenticationValue(decryptResultDTO.getString());
                        } catch (Exception e) {
                            log.error(
                                "[setAReqAndAres] decrypt ARes cavv failed. AuthenticationLog.ID={}",
                                authLog.getId(), e);
                            aRes.setAuthenticationValue("Decrypt failed");
                        } finally {
                            if (decryptResultDTO != null) {
                                decryptResultDTO.clearPlainText();
                            }
                        }
                    }
                    txLogDetailDto.setAuthReq(aReq);
                    txLogDetailDto.setAuthRes(aRes);
                });
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void setRReqAndRRes(TransactionLogDO txLog, TransactionLogDetailDto txLogDetailDto) {
        if (txLog.getResultLogID() == null) {
            return;
        }

        resultLogDao
          .findById(txLog.getResultLogID())
          .ifPresent(
            resultLog -> {
                List<ResultMeLogDO> rReqMeLogs =
                  resultMeLogDao.findByResultLogId(
                    resultLog.getId(), MessageType.RReq);
                List<ResultMeLogDO> rResMeLogs =
                  resultMeLogDao.findByResultLogId(
                    resultLog.getId(), MessageType.RRes);
                ResultRequestDto resultRequestDto = ResultRequestDto
                    .valueOf(resultLog, rReqMeLogs, txLog);
                // decrypt RReq cavv
                if (StringUtils.isNotEmpty(resultLog.getAuthenticationValueEn())) {
                    DecryptResultDTO decryptResultDTO = null;
                    try {
                        decryptResultDTO = hsmPlugin.decryptSensitiveDataWithIssuerBankId(
                            resultLog.getAuthenticationValueEn(),
                            txLog.getIssuerBankId());
                        resultRequestDto.setAuthenticationValue(decryptResultDTO.getString());
                    } catch (Exception e) {
                        log.error(
                            "[setRReqAndRRes] decrypt RReq cavv failed. ResultLog.ID={}",
                            resultLog.getId(), e);
                        resultRequestDto.setAuthenticationValue("Decrypt failed");
                    } finally {
                       if (decryptResultDTO != null) {
                           decryptResultDTO.clearPlainText();
                       }
                    }
                }
                txLogDetailDto.setResultReq(resultRequestDto);
                txLogDetailDto.setResultRes(
                  ResultResponseDto.valueOf(resultLog, rResMeLogs, txLog));
            });
    }

    private void setCReqAndCRes(TransactionLogDO txLog, TransactionLogDetailDto txLogDetailDto) {
        List<ChallengeRequestDto> cReqList = new ArrayList<>();
        List<ChallengeResponseDto> cResList = new ArrayList<>();
        challengeLogDao.findByTxlogId(txLog.getId()).stream()
          .forEach(
            challengeLog -> {
                List<ChallengeCodeLogDO> challengeCodeLogList =
                  challengeCodeLogDao.findByChallengeLogId(challengeLog.getId());
                List<ChallengeMeLogDO> cReqMeLogs =
                  challengeMeLogDao.findByChallengeLogId(
                    challengeLog.getId(), MessageType.CReq);
                List<ChallengeMeLogDO> cResMeLogs =
                  challengeMeLogDao.findByChallengeLogId(
                    challengeLog.getId(), MessageType.CRes);
                ChallengeRequestDto cReq =
                  ChallengeRequestDto.valueOf(challengeLog, cReqMeLogs, txLog);
                List<ChallengeSelectInfoLogDO> selectInfoLogList =
                  challengeSelectInfoLogDao.findByChallengeLogId(
                    challengeLog.getId());
                ChallengeResponseDto cRes =
                  ChallengeResponseDto.valueOf(
                    challengeLog,
                    cResMeLogs,
                    selectInfoLogList,
                    challengeCodeLogList,
                    txLog);
                cReqList.add(cReq);
                cResList.add(cRes);
            });
        txLogDetailDto.setChallengeReq(cReqList);
        txLogDetailDto.setChallengeRes(cResList);
    }

    private void setErrorMessage(TransactionLogDO txLog, TransactionLogDetailDto txLogDetailDto) {
        if (txLog.getErrorMessageLogID() == null) {
            return;
        }

        errMsgLogDao
          .findById(txLog.getErrorMessageLogID())
          .ifPresent(
            errorMessageLog -> {
                ErrorMessageDto errorMessageDto =
                  ErrorMessageDto.valueOf(errorMessageLog);
                txLogDetailDto.setError(errorMessageDto);
            });
    }

    private void setClassicRbaResult(
      TransactionLogDO txLog, TransactionLogDetailDto txV2LogDetailDto) {
        txV2LogDetailDto.setClassicRbaResultDto(
          ClassicRbaResultDto.valueOf(txLog, classicRbaProperties.getEnabledModules()));
    }

    private void setDdcaLogResult(TransactionLogDO txLog,
      TransactionLogDetailDto txV2LogDetailDto) {
        if (txLog.getDdcaLogId() == null) {
            return;
        }
        ddcaLogDao
          .findById(txLog.getDdcaLogId())
          .ifPresent(
            ddcaLog -> {
                txV2LogDetailDto.setDdcaLogResultDto(DdcaLogResultDto.valueOf(ddcaLog));
            });
    }

    /** 匯出交易紀錄 - 全部匯出 */
    public void exportXlsByQuery(TxLogHeaderQueryExportDTO queryDto, HttpServletResponse rps, boolean canSeePan)
      throws IOException {
        List<TxLogExportDTO> dataList = txLogManager.reportQuery(queryDto, canSeePan);
        exportXls(dataList, rps);
    }

    /** 匯出交易紀錄 - 勾選 */
    public void exportXlsByIds(IdsQueryDTO queryDto, HttpServletResponse rps, boolean canSeePan) throws IOException {
        List<TxLogExportDTO> dataList = txLogManager.reportQueryByIds(queryDto, canSeePan);
        exportXls(dataList, rps);
    }

    /** 匯出xls */
    private void exportXls(List<TxLogExportDTO> dataList, HttpServletResponse rps)
      throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> columnNames = getColumnNames();
        ExcelBuildUtils.createHeader(workbook, sheet, columnNames);

        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            TxLogExportDTO txLogDto = dataList.get(rowNum - 1);
            row.createCell(0).setCellValue(txLogDto.getIssuerName());
            row.createCell(1).setCellValue(txLogDto.getTransTime());
            row.createCell(2).setCellValue(txLogDto.getPan());
            row.createCell(3).setCellValue(txLogDto.getAmount());
            row.createCell(4).setCellValue(txLogDto.getCurrencyCode());
            row.createCell(5).setCellValue(txLogDto.getAres());
            row.createCell(6).setCellValue(txLogDto.getRres());
            row.createCell(7).setCellValue(txLogDto.getDeviceChannel());
            row.createCell(8).setCellValue(txLogDto.getShoppingTime());
            row.createCell(9).setCellValue(txLogDto.getMerchantName());
            row.createCell(10).setCellValue(txLogDto.getMerchantCountryCode());
            row.createCell(11).setCellValue(txLogDto.getErrorReason());
            row.createCell(12).setCellValue(txLogDto.getChallengeReason());
            row.createCell(13).setCellValue(txLogDto.getUserAgent());
        }
        ExcelBuildUtils.resizeColums(sheet, columnNames.size());
        String fileName = ExcelBuildUtils.getFileNameFormat(MessageConstants.get(MessageConstants.EXCEL_TRANSACTION_LOG));
        ExcelBuildUtils.doExport(rps, fileName, workbook);
    }

    /** 匯出xls - VELog */
    private void exportXlsForVELog(List<VELogQueryResultDTO> dataList, String timeZone, HttpServletResponse rps)
        throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> columnNames = getColumnNamesVelog();
        ExcelBuildUtils.createHeader(workbook, sheet, columnNames);

        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            VELogQueryResultDTO txLogDto = dataList.get(rowNum - 1);
            row.createCell(0).setCellValue(txLogDto.getIssuerName());
            row.createCell(1).setCellValue(txLogDto.getCardNumber());
            row.createCell(2).setCellValue(
                DateUtil.millisToDateTimeStr(txLogDto.getVereqTimeMillis(), ZoneId.of(timeZone)));
            row.createCell(3).setCellValue(txLogDto.getVereqStatus());
            row.createCell(4).setCellValue(txLogDto.getMerchantId());
            row.createCell(5).setCellValue(txLogDto.getAcctId());
            row.createCell(6).setCellValue(txLogDto.getIreqCode());
            row.createCell(7).setCellValue(txLogDto.isPareqReceived() ? "Y" : "N");
        }
        ExcelBuildUtils.resizeColums(sheet, columnNames.size());
        String fileName = ExcelBuildUtils.getFileNameFormat(MessageConstants.get(MessageConstants.EXCEL_VE_LOG));
        ExcelBuildUtils.doExport(rps, fileName, workbook);
    }

    private CipherReqDTO getCipherReqDTOForEncryptedCardholderNames(List<TxLogExportDTO> logList) {
        CipherReqDTO cipherReqDto = new CipherReqDTO();
        logList.stream()
          .filter(log -> StringUtils.isNotBlank(log.getCardholderName()))
          .forEach(
            log ->
              cipherReqDto.addData(
                log.getKernelTransactionLogId().toString(),
                log.getCardholderName()));
        return cipherReqDto;
    }

    /** 取得簡訊紀錄 */
    public List<SmsRecordDTO> getSmsRecords(String acsTransId) {
        return acsIntegratorManager.getSmsRecords(new SmsReqDTO(acsTransId));
    }

}
