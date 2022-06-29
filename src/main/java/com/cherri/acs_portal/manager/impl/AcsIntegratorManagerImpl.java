package com.cherri.acs_portal.manager.impl;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.controller.response.SmsRecordDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.acs_integrator.Add3ds1AttemptGrantedSettingReqDto;
import com.cherri.acs_portal.dto.acs_integrator.Add3ds1AttemptGrantedSettingResDto;
import com.cherri.acs_portal.dto.acs_integrator.ApiPageResponse;
import com.cherri.acs_portal.dto.acs_integrator.BaseResDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1DeleteReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1GetAuditStatusResDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1ListReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1ListResDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1ModifyResDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1QueryReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1ResDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1SaveReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1UpdateAuditStatusReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1UpdateReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1ValidReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.CardHolderInfoResDTO;
import com.cherri.acs_portal.dto.acs_integrator.CardholderDetailReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.Check3ds1ReqDto;
import com.cherri.acs_portal.dto.acs_integrator.Check3ds1ResDto;
import com.cherri.acs_portal.dto.acs_integrator.CipherData;
import com.cherri.acs_portal.dto.acs_integrator.CipherReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.CipherResDTO;
import com.cherri.acs_portal.dto.acs_integrator.Get3ds1AttemptGrantedLogReqDto;
import com.cherri.acs_portal.dto.acs_integrator.Get3ds1AttemptGrantedLogResDto;
import com.cherri.acs_portal.dto.acs_integrator.Get3ds1AttemptGrantedOriginDataReqDto;
import com.cherri.acs_portal.dto.acs_integrator.Get3ds1AttemptGrantedOriginDataResDto;
import com.cherri.acs_portal.dto.acs_integrator.Get3ds1OtpLockOriginDataReqDto;
import com.cherri.acs_portal.dto.acs_integrator.Get3ds1OtpLockOriginDataResDto;
import com.cherri.acs_portal.dto.acs_integrator.GetOtpLockStatusReqDto;
import com.cherri.acs_portal.dto.acs_integrator.GetOtpLockStatusResDto;
import com.cherri.acs_portal.dto.acs_integrator.HealthCheckDTO;
import com.cherri.acs_portal.dto.acs_integrator.HsmTacReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.HsmTacResDTO;
import com.cherri.acs_portal.dto.acs_integrator.HttpRequestParameter;
import com.cherri.acs_portal.dto.acs_integrator.IdsQueryReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.SigningCertificateGenCsrReqDto;
import com.cherri.acs_portal.dto.acs_integrator.SigningCertificateGenCsrResDto;
import com.cherri.acs_portal.dto.acs_integrator.SmsReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.TransactionDetailV1ResDTO;
import com.cherri.acs_portal.dto.acs_integrator.TransactionRecordV1ReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.TransactionRecordV1ResDTO;
import com.cherri.acs_portal.dto.acs_integrator.TransactionReportV1ResDTO;
import com.cherri.acs_portal.dto.acs_integrator.TransactionSummaryV1ReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.TransactionSummaryV1ResDTO;
import com.cherri.acs_portal.dto.acs_integrator.Unlock3ds1OtpReqDto;
import com.cherri.acs_portal.dto.acs_integrator.Unlock3ds1OtpResDto;
import com.cherri.acs_portal.dto.acs_integrator.Update3ds1AttemptGrantedAuditStatusRequestDto;
import com.cherri.acs_portal.dto.acs_integrator.Update3ds1OtpLockAuditStatusRequestDto;
import com.cherri.acs_portal.dto.bank.IssuerTotalCardsQueryDto;
import com.cherri.acs_portal.dto.bank.IssuerTotalCardsQueryResultDto;
import com.cherri.acs_portal.dto.cardholder.HolderQueryDTO;
import com.cherri.acs_portal.dto.transactionLog.SmsRecordsResDTO;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.util.OkHttpConnector;
import com.cherri.acs_portal.util.StringCustomizedUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.CardType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.BinRangeDAO;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.data_object.entity.BinRangeDO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AcsIntegratorManagerImpl implements AcsIntegratorManager {

  private static final String CARD_NUMBER_CIPHER_KEY = "cardNumber";

  private final OkHttpConnector acsiHttpConnector;
  private final ObjectMapper objectMapper;

  private final IssuerBankDAO issuerBankDao;
  private final BinRangeDAO binRangeDao;

  @Autowired
  public AcsIntegratorManagerImpl(
      OkHttpConnector acsiHttpConnector,
      ObjectMapper objectMapper,
      IssuerBankDAO issuerBankDao,
      BinRangeDAO binRangeDao) {
    this.acsiHttpConnector = acsiHttpConnector;
    this.objectMapper = objectMapper;
    this.issuerBankDao = issuerBankDao;
    this.binRangeDao = binRangeDao;
  }

  @Override
  public CardHolderInfoResDTO getCardholder(HolderQueryDTO holderQuery) throws NoSuchDataException {
    String cardholderDetailUrl =
        EnvironmentConstants.ACS_INTEGRATOR_URL + "/card/holder/detail/get";
    CardholderDetailReqDTO queryReq = createGetCardholderInfoReqDto(holderQuery);

    String reqJson = parseToJson(queryReq);
    HttpRequestParameter params =
        new HttpRequestParameter(cardholderDetailUrl, reqJson, CardHolderInfoResDTO.class);

    return doPost(params);
  }

  @SuppressWarnings("unchecked")
  private <AcsiResponseDto extends BaseResDTO> AcsiResponseDto doPost(HttpRequestParameter params)
      throws OceanException {
    String result;
    try {
      Response response = acsiHttpConnector.post(params);
      result = response.body().string();

      log.debug("[doPost] request url={}, responseBody={}", params.getUrl(), result);

      boolean isSuccessful = response.isSuccessful();
      AcsiResponseDto resDto = null;

      if (isSuccessful && null != params.getResDtoClz()) {
        resDto = (AcsiResponseDto) objectMapper.readValue(result, params.getResDtoClz());
      } else if (isSuccessful && null != params.getResDtoTypeReference()) {
        resDto = (AcsiResponseDto) objectMapper.readValue(result, params.getResDtoTypeReference());
      }

      if (null != resDto && resDto.isSuccess()) {
        return resDto;
      }

      boolean isResponseEmptyData = null != resDto && resDto.isEmpty();
      if (isResponseEmptyData) {
        return resDto;
      }

      boolean isResponseDuplicate = null != resDto && resDto.isDuplicateData();
      if (isResponseDuplicate) {
        return resDto;
      }

      String errMsg = "";
      if (resDto != null) {
        errMsg = resDto.getMsg();
        log.error(
            "[doPost] response error, url={}, status={}, msg={}",
            params.getUrl(),
            resDto.getStatus(),
            resDto.getMsg());
      }
      throw new OceanException(ResultStatus.ACS_INTEGRATOR_RESPONSE_ERROR, errMsg);

    } catch (IOException e) {
      log.error("[doPost] get data from acs-integrator error", e);
      throw new OceanException(ResultStatus.ACS_INTEGRATOR_CONNECT_ERROR, e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  private <AcsiResponseDto extends BaseResDTO> AcsiResponseDto doGet(
      String apiUrl, Class<?> responseDtoClz) throws OceanException {
    String result;
    try {
      Response response = acsiHttpConnector.get(apiUrl);
      result = response.body().string();

      boolean isSuccessful = response.isSuccessful();
      if (isSuccessful) {
        return (AcsiResponseDto) objectMapper.readValue(result, responseDtoClz);
      }

      throw new IllegalStateException("Illegal response status from Integrator");

    } catch (IOException e) {
      log.error("[doGet] get data from acs-integrator error", e);
      throw new OceanException(ResultStatus.ACS_INTEGRATOR_CONNECT_ERROR, e.getMessage());
    }
  }

  private CardholderDetailReqDTO createGetCardholderInfoReqDto(HolderQueryDTO queryDto)
      throws NoSuchDataException {
    CardholderDetailReqDTO reqDto = new CardholderDetailReqDTO();
    Long issuerBankId = queryDto.getIssuerBankId();
    // 使用卡號查詢
    if (StringCustomizedUtils.isNotEmpty(queryDto.getRealCardNumber())) {
        BinRangeDO binRange =
          getBinRange(issuerBankId, queryDto.getRealCardNumber())
            .orElseThrow(
              () -> {
                  String msg = "The card is not in the bin-range.";
                  log.warn("[createGetCardholderInfoReqDto] {}", msg);
                  return new NoSuchDataException(msg);
              });

        log.debug("[createGetCardholderInfoReqDto] binRange={}", binRange);

        IssuerBankDO issuerBank =
          issuerBankDao
            .findById(issuerBankId)
            .orElseThrow(
              () -> {
                  log.error(
                    "[createGetCardholderInfoReqDto] issuerBank not found. bankId={}",
                    issuerBankId);
                  return new NoSuchDataException("IssuerBankId=" + issuerBankId + " not found.'");
              });

        CardType cardType = CardType.getByCode(binRange.getCardType());
        if (cardType == null) {
            String errMsg = "cardTypeCode is unknown";
            log.warn("[createGetCardholderInfoReqDto] {}. cardType={}",
              errMsg,
              binRange.getCardType());
            throw new IllegalArgumentException(errMsg);
        }

        log.debug("[createGetCardholderInfoReqDto] issuerBank={}", issuerBank);
        reqDto.setCardNumber(queryDto.getRealCardNumber());
        reqDto.setBankCode(issuerBank.getCode());
        reqDto.setCardType(cardType);
        return reqDto;
    }
      // 使用身分證查詢
      reqDto.setIdentityNumber(queryDto.getIdentityNumber());
      IssuerBankDO issuerBank =
        issuerBankDao
          .findById(issuerBankId)
          .orElseThrow(
            () -> {
                String errMsg = "issuerBank not found";
                log.warn(
                  "[createGetCardholderInfoReqDto] {}. bankId={}", errMsg, issuerBankId);
                return new NoSuchDataException(errMsg);
            });
      reqDto.setBankCode(issuerBank.getCode());
      return reqDto;
  }

    private Optional<BinRangeDO> getBinRange(Long issuerBankId, String realCardNumber)
      throws OceanException {
        try {
            BigInteger cardNumber = new BigInteger(realCardNumber);
            List<BinRangeDO> list = binRangeDao
              .findByStartBinBetweenEndBin(issuerBankId, cardNumber);
            if (list == null || list.isEmpty()) {
                return Optional.empty();
            }

            log.debug("[getBinRange] binRange list size={}", list.size());
            return Optional.of(list.get(0));
        } catch (DatabaseException e) {
      throw new OceanException(e.getResultStatus(), e.getMessage());
    }
  }

  @Override
  public Set<String> getCardList(HolderQueryDTO holderQuery) throws OceanException {
    return getCardholder(holderQuery).getCardEnrollStatusMap().keySet();
  }

  @Override
  public Optional<HealthCheckDTO> healthCheck() {
    String healthCheckUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/health/check";
    try {
      return Optional.of(doGet(healthCheckUrl, HealthCheckDTO.class));
    } catch (Exception e) {
      log.error("[healthCheck] unknown exception, url={}", healthCheckUrl, e);
      throw e;
    }
  }

  @Override
  public Optional<CipherResDTO> encrypt(CipherReqDTO cipherReqDto) {
      String encryptUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/encrypt";

      String reqJson = parseToJson(cipherReqDto);
      HttpRequestParameter params = new HttpRequestParameter(encryptUrl, reqJson,
        CipherResDTO.class);
      try {
          return Optional.of(doPost(params));
      } catch (Exception e) {
          log.error(
            "[encrypt] unknown exception, url={}, cipherReqDto={}", encryptUrl, cipherReqDto, e);
          throw new OceanException(ResultStatus.ACS_INTEGRATOR_RESPONSE_ERROR, e.getMessage());
      }
  }

  /** 加密卡號 */
  @Override
  public String encryptedCardNumber(String cardNumber) {
    CipherReqDTO cipherReq = new CipherReqDTO();
    cipherReq.addData(CARD_NUMBER_CIPHER_KEY, cardNumber);
    CipherResDTO encryptCardNumberRes = encrypt(cipherReq).orElse(new CipherResDTO());

    if (encryptCardNumberRes.isEmptyData()) {
      log.warn("[encryptedCardNumber] encrypt cardNumber error.");
        throw new OceanException(
          ResultStatus.ACS_INTEGRATOR_RESPONSE_ERROR, "Encrypt cardNumber error");
    }

      return encryptCardNumberRes
        .getCipherData(CARD_NUMBER_CIPHER_KEY)
        .map(CipherData::getValue)
        .orElseThrow(
          () -> {
              log.warn("[encryptedCardNumber] encrypt cardNumber error.");
              return new OceanException(
                ResultStatus.ACS_INTEGRATOR_RESPONSE_ERROR, "Encrypt cardNumber error");
          });
  }

  @Override
  public Optional<CipherResDTO> decrypt(CipherReqDTO cipherReqDto) {
      String decryptUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/decrypt";

      String reqJson = parseToJson(cipherReqDto);
      HttpRequestParameter params = new HttpRequestParameter(decryptUrl, reqJson,
        CipherResDTO.class);

      try {
          return Optional.of(doPost(params));
      } catch (Exception e) {
          log.error(
            "[decrypt] unknown exception, url={}, cipherReqDto={}", decryptUrl, cipherReqDto, e);
          throw new OceanException(ResultStatus.ACS_INTEGRATOR_RESPONSE_ERROR, "Decrypt error");
      }
  }

  @Override
  public Optional<String> getCardTacFromHSM(HsmTacReqDTO hsmTacReqDTO) throws OceanException {
    String getTacUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/tac/get";

    String reqJson = parseToJson(hsmTacReqDTO);
    HttpRequestParameter params = new HttpRequestParameter(getTacUrl, reqJson, CipherResDTO.class);

    HsmTacResDTO responseDto = doPost(params);
    if (responseDto.isFailed()) {
      return Optional.empty();
    }
    return Optional.ofNullable(responseDto.getTac());
  }

  @Override
  public IssuerTotalCardsQueryResultDto getTotalCards(
      IssuerTotalCardsQueryDto issuerTotalCardsQueryDto) throws OceanException {
    String getTotalCardsUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/bank/totalCards";

    String reqJson = parseToJson(issuerTotalCardsQueryDto);
    HttpRequestParameter params =
        new HttpRequestParameter(getTotalCardsUrl, reqJson, IssuerTotalCardsQueryResultDto.class);

    IssuerTotalCardsQueryResultDto responseDto = doPost(params);
    if (responseDto.isSuccess()) {
      return responseDto;
    }

    return null;
  }

  /** 從ACS Integrator查詢簡訊紀錄 */
  @Override
  public List<SmsRecordDTO> getSmsRecords(SmsReqDTO reqDto) {
    String getSmsRecordsUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/sms/get/records/";

    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(getSmsRecordsUrl, reqJson, SmsRecordsResDTO.class);

    SmsRecordsResDTO responseDto = doPost(params);
    if (responseDto.isFailed()) {
      return Collections.emptyList();
    }
    return responseDto.getData();
  }

  @Override
  public Optional<SigningCertificateGenCsrResDto> generateCsrKey(
      SigningCertificateGenCsrReqDto signingCertificateGenCsrReqDto) throws OceanException {
    String createCsrUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/certificate/csr/create";

    String reqJson = parseToJson(signingCertificateGenCsrReqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(createCsrUrl, reqJson, SigningCertificateGenCsrResDto.class);

    SigningCertificateGenCsrResDto responseDto = doPost(params);
    if (responseDto.isFailed()) {
      return Optional.empty();
    }
    return Optional.of(responseDto);
  }

  @Override
  public Optional<Get3ds1OtpLockOriginDataResDto> get3ds1OtpLockOriginData(
      Get3ds1OtpLockOriginDataReqDto reqDto) {
    String url = EnvironmentConstants.ACS_INTEGRATOR_URL + "/otp/3ds1/lock/origin";
    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(url, reqJson, Get3ds1OtpLockOriginDataResDto.class);

    Get3ds1OtpLockOriginDataResDto resDto = doPost(params);
    if (resDto.isFailed()) {
      return Optional.empty();
    }
    return Optional.of(resDto);
  }

  /** 向ACS Integrator查詢卡片是否為3DS 1.0版本 */
  @Override
  public boolean is3ds1(Check3ds1ReqDto reqDto) {
    String check3dsV1 = EnvironmentConstants.ACS_INTEGRATOR_URL + "/otp/3ds1/check";

    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(check3dsV1, reqJson, Check3ds1ResDto.class);

    Check3ds1ResDto responseDto = doPost(params);
    if (responseDto.isFailed()) {
      return false;
    }
    return responseDto.getIs3ds1();
  }

  @Override
  public Optional<GetOtpLockStatusResDto> get3ds1OtpLockStatus(GetOtpLockStatusReqDto reqDto) {
    String otpLockStatusUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/otp/3ds1/lock/status";
    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(otpLockStatusUrl, reqJson, GetOtpLockStatusResDto.class);

    return Optional.ofNullable(doPost(params));
  }

  @Override
  public Optional<Unlock3ds1OtpResDto> unlock3ds1Otp(Unlock3ds1OtpReqDto reqDto) {
    String otpLockStatusUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/otp/3ds1/unlock";
    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(otpLockStatusUrl, reqJson, Unlock3ds1OtpResDto.class);

    return Optional.ofNullable(doPost(params));
  }

  @Override
  public boolean update3ds1OtpLockAuditStatus(Update3ds1OtpLockAuditStatusRequestDto reqDto) {
    String url = EnvironmentConstants.ACS_INTEGRATOR_URL + "/otp/3ds1/lock/audit-status/update";
    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params = new HttpRequestParameter(url, reqJson, BaseResDTO.class);

    return doPost(params).isSuccess();
  }

  @Override
  public Optional<Get3ds1AttemptGrantedOriginDataResDto> get3ds1AttemptGrantedOriginData(
      Get3ds1AttemptGrantedOriginDataReqDto reqDto) {
    String url = EnvironmentConstants.ACS_INTEGRATOR_URL + "/attempt-auth/3ds1/origin";
    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(url, reqJson, Get3ds1AttemptGrantedOriginDataResDto.class);

    return Optional.ofNullable(doPost(params));
  }

  @Override
  public Optional<Add3ds1AttemptGrantedSettingResDto> addAttemptGrantedSetting(
      Add3ds1AttemptGrantedSettingReqDto reqDto) {
    String url = EnvironmentConstants.ACS_INTEGRATOR_URL + "/attempt-auth/3ds1/add";
    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(url, reqJson, Add3ds1AttemptGrantedSettingResDto.class);

    return Optional.ofNullable(doPost(params));
  }

  @Override
  public boolean update3ds1AttemptGrantedAuditStatus(
      Update3ds1AttemptGrantedAuditStatusRequestDto reqDto) {
    String url = EnvironmentConstants.ACS_INTEGRATOR_URL + "/attempt-auth/3ds1/audit-status/update";
    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params = new HttpRequestParameter(url, reqJson, BaseResDTO.class);

    return doPost(params).isSuccess();
  }

  @Override
  public Optional<Get3ds1AttemptGrantedLogResDto> get3ds1AttemptGrantedLog(
      Get3ds1AttemptGrantedLogReqDto reqDto) {
    String url = EnvironmentConstants.ACS_INTEGRATOR_URL + "/attempt-auth/3ds1/log";
    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(url, reqJson, Get3ds1AttemptGrantedLogResDto.class);

    return Optional.ofNullable(doPost(params));
  }

  @Override
  public PagingResultDTO<TransactionRecordV1ResDTO> query3dsV1TransactionRecords(
      TransactionRecordV1ReqDTO reqDto) {
    String txRecordV1Url = EnvironmentConstants.ACS_INTEGRATOR_URL + "/transaction/record/3ds1";

    String reqJson = parseToJson(reqDto);
    HttpRequestParameter params =
        new HttpRequestParameter(
            txRecordV1Url,
            reqJson,
            new TypeReference<ApiPageResponse<TransactionRecordV1ResDTO>>() {});

    ApiPageResponse responseDto = doPost(params);
    if (responseDto.isEmpty()) {
      return PagingResultDTO.empty();
    }
    return PagingResultDTO.valueOf(responseDto);
  }

  @Override
  public Optional<TransactionSummaryV1ResDTO> get3dsV1TransactionSummary(
      TransactionSummaryV1ReqDTO queryDto) {
    String txSummaryV1Url = EnvironmentConstants.ACS_INTEGRATOR_URL + "/transaction/summary/3ds1";

    String reqJson = parseToJson(queryDto);
    HttpRequestParameter params =
        new HttpRequestParameter(txSummaryV1Url, reqJson, TransactionSummaryV1ResDTO.class);

    TransactionSummaryV1ResDTO responseDto = doPost(params);
    if (responseDto.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(responseDto);
  }

  @Override
  public Optional<TransactionDetailV1ResDTO> get3dsV1TransactionDetail(String transactionLogId) {
    String txDetailUrl =
        EnvironmentConstants.ACS_INTEGRATOR_URL + "/transaction/detail/3ds1/" + transactionLogId;
    try {
      return Optional.of(doGet(txDetailUrl, TransactionDetailV1ResDTO.class));
    } catch (Exception e) {
      log.error("[get3dsV1TransactionDetail] unknown exception,, url={}", txDetailUrl);
      throw e;
    }
  }

  @Override
  public PagingResultDTO<TransactionReportV1ResDTO> query3dsV1TransactionRecordsReport(
      TransactionRecordV1ReqDTO queryDto) {
    String txReportUrl =
        EnvironmentConstants.ACS_INTEGRATOR_URL + "/transaction/record/report/3ds1";

    String reqJson = parseToJson(queryDto);
    HttpRequestParameter params =
        new HttpRequestParameter(
            txReportUrl,
            reqJson,
            new TypeReference<ApiPageResponse<TransactionReportV1ResDTO>>() {});

    ApiPageResponse responseDto = doPost(params);
    if (responseDto.isEmpty()) {
      return PagingResultDTO.empty();
    }
    return PagingResultDTO.valueOf(responseDto);
  }

  @Override
  public PagingResultDTO<TransactionReportV1ResDTO> query3dsV1TransactionRecordsReport(
      IdsQueryReqDTO queryDto) {
    String txReportUrl =
        EnvironmentConstants.ACS_INTEGRATOR_URL + "/transaction/record/report/ids/3ds1";

    String reqJson = parseToJson(queryDto);
    HttpRequestParameter params =
        new HttpRequestParameter(
            txReportUrl,
            reqJson,
            new TypeReference<ApiPageResponse<TransactionReportV1ResDTO>>() {});

    ApiPageResponse responseDto = doPost(params);
    if (responseDto.isEmpty()) {
      return PagingResultDTO.empty();
    }
    return PagingResultDTO.valueOf(responseDto);
  }

  @Override
  public Optional<BinRangeV1ResDTO> get3dsV1BinRange(BinRangeV1QueryReqDTO queryDto) {
    String binRangeGetUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/bin-range";

    String reqJson = parseToJson(queryDto);
    HttpRequestParameter params =
            new HttpRequestParameter(
                    binRangeGetUrl,
                    reqJson,
                    BinRangeV1ResDTO.class);

    BinRangeV1ResDTO responseDto = doPost(params);
    if (responseDto.isFailed()) {
      return Optional.empty();
    }
    return Optional.of(responseDto);
  }

  @Override
  public Optional<BinRangeV1ResDTO> get3dsV1BinRangeById(Long tCardnoSetupAuditStatusId) {
    String binRangeGetUrl =
        EnvironmentConstants.ACS_INTEGRATOR_URL + "/bin-range/" + tCardnoSetupAuditStatusId;

    BinRangeV1ResDTO responseDto = doGet(binRangeGetUrl, BinRangeV1ResDTO.class);
    if (responseDto.isFailed()) {
      return Optional.empty();
    }
    return Optional.of(responseDto);
  }

  @Override
  public PagingResultDTO<BinRangeV1ListResDTO> list3dsV1BinRange(BinRangeV1ListReqDTO queryDto) {
    String binRangeListUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/bin-range/list";

    String reqJson = parseToJson(queryDto);
    HttpRequestParameter params =
        new HttpRequestParameter(
            binRangeListUrl,
            reqJson,
            new TypeReference<ApiPageResponse<BinRangeV1ListResDTO>>() {});

    ApiPageResponse<BinRangeV1ListResDTO> responseDto = doPost(params);
    if (responseDto.isEmpty()) {
      return PagingResultDTO.empty();
    }
    return PagingResultDTO.valueOf(responseDto);
  }

  @Override
  public BinRangeV1ModifyResDTO save3dsV1BinRange(BinRangeV1SaveReqDTO createDto) {
    String binRangeSaveUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/bin-range/save";

    String reqJson = parseToJson(createDto);
    HttpRequestParameter params =
        new HttpRequestParameter(binRangeSaveUrl, reqJson, BinRangeV1ModifyResDTO.class);

    return doPost(params);
  }

  @Override
  public BinRangeV1ModifyResDTO update3dsV1BinRange(BinRangeV1UpdateReqDTO updateDto) {
    String binRangeUpdateUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/bin-range/update";

    String reqJson = parseToJson(updateDto);
    HttpRequestParameter params =
        new HttpRequestParameter(binRangeUpdateUrl, reqJson, BinRangeV1ModifyResDTO.class);

    return doPost(params);
  }

  @Override
  public boolean exiting3dsV1BinRangeConflict(BinRangeV1ValidReqDTO updateDto) {
    String binRangeValidUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/bin-range/valid";

    String reqJson = parseToJson(updateDto);
    HttpRequestParameter params =
        new HttpRequestParameter(binRangeValidUrl, reqJson, BaseResDTO.class);

    BaseResDTO responseDto = doPost(params);
    return responseDto.isDuplicateData();
  }

  @Override
  public AuditStatus get3dsV1BinRangeAuditStatusById(Long tCardnoSetupAuditStatusId) {
    String binRangeGetAuditStatusUrl =
        EnvironmentConstants.ACS_INTEGRATOR_URL
            + "/bin-range/audit-status/"
            + tCardnoSetupAuditStatusId;

    BinRangeV1GetAuditStatusResDTO responseDto =
        doGet(binRangeGetAuditStatusUrl, BinRangeV1GetAuditStatusResDTO.class);
    if (responseDto.isSuccess()) {
      return AuditStatus.getStatusBySymbol(responseDto.getAuditStatus());
    }
    return AuditStatus.UNKNOWN;
  }

  @Override
  public boolean update3dsV1BinRangeAuditStatus(BinRangeV1UpdateAuditStatusReqDTO updateDto) {
    String binRangeUpdateAuditStatusUrl =
        EnvironmentConstants.ACS_INTEGRATOR_URL + "/bin-range/update-audit-status";

    String reqJson = parseToJson(updateDto);
    HttpRequestParameter params =
        new HttpRequestParameter(
            binRangeUpdateAuditStatusUrl, reqJson, BinRangeV1ModifyResDTO.class);

    BinRangeV1ModifyResDTO responseDto = doPost(params);
    return responseDto.isSuccess();
  }

  @Override
  public boolean delete3dsV1BinRange(BinRangeV1DeleteReqDTO deleteDto) {
    String binRangeDeleteUrl = EnvironmentConstants.ACS_INTEGRATOR_URL + "/bin-range/delete";

    String reqJson = parseToJson(deleteDto);
    HttpRequestParameter params =
        new HttpRequestParameter(binRangeDeleteUrl, reqJson, BaseResDTO.class);

    BaseResDTO responseDto = doPost(params);
    return responseDto.isSuccess();
  }

  private String parseToJson(Object dto) {
    try {
      return objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      log.error(
          "[parseToJson] while parsing requestDto to JSON error, {}={}",
          dto.getClass().getSimpleName(),
          dto);
      throw new OceanException(
          ResultStatus.SERVER_ERROR, "An error occurred while parsing requestDto to JSON.");
    }
  }
}
