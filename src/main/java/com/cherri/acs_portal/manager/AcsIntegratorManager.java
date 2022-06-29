package com.cherri.acs_portal.manager;

import com.cherri.acs_portal.controller.response.SmsRecordDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.acs_integrator.Add3ds1AttemptGrantedSettingReqDto;
import com.cherri.acs_portal.dto.acs_integrator.Add3ds1AttemptGrantedSettingResDto;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1DeleteReqDTO;
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
import com.cherri.acs_portal.dto.acs_integrator.Check3ds1ReqDto;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.OceanException;

public interface AcsIntegratorManager {

    CardHolderInfoResDTO getCardholder(HolderQueryDTO query) throws OceanException;

    Set<String> getCardList(HolderQueryDTO query) throws OceanException;

    Optional<HealthCheckDTO> healthCheck() throws OceanException;

    Optional<CipherResDTO> encrypt(CipherReqDTO cipherReqDto) throws OceanException;

    String encryptedCardNumber(String cardNumber) throws OceanException;

    Optional<CipherResDTO> decrypt(CipherReqDTO cipherReqDto) throws OceanException;

    Optional<String> getCardTacFromHSM(HsmTacReqDTO hsmTacReqDTO) throws OceanException;

    IssuerTotalCardsQueryResultDto getTotalCards(IssuerTotalCardsQueryDto issuerTotalCardsQueryDto)
      throws OceanException;

    Optional<SigningCertificateGenCsrResDto> generateCsrKey(
      SigningCertificateGenCsrReqDto signingCertificateGenCsrReqDto) throws OceanException;

    List<SmsRecordDTO> getSmsRecords(SmsReqDTO reqDto) throws OceanException;

    Optional<Get3ds1OtpLockOriginDataResDto> get3ds1OtpLockOriginData(
      Get3ds1OtpLockOriginDataReqDto reqDto);

    boolean is3ds1(Check3ds1ReqDto reqDto);

    Optional<GetOtpLockStatusResDto> get3ds1OtpLockStatus(GetOtpLockStatusReqDto reqDto);

    Optional<Unlock3ds1OtpResDto> unlock3ds1Otp(Unlock3ds1OtpReqDto reqDto);

    boolean update3ds1OtpLockAuditStatus(Update3ds1OtpLockAuditStatusRequestDto reqDto);

    Optional<Get3ds1AttemptGrantedOriginDataResDto> get3ds1AttemptGrantedOriginData(
      Get3ds1AttemptGrantedOriginDataReqDto reqDto);

    Optional<Add3ds1AttemptGrantedSettingResDto> addAttemptGrantedSetting(
      Add3ds1AttemptGrantedSettingReqDto reqDto);

    boolean update3ds1AttemptGrantedAuditStatus(
      Update3ds1AttemptGrantedAuditStatusRequestDto reqDto);

    Optional<Get3ds1AttemptGrantedLogResDto> get3ds1AttemptGrantedLog(
      Get3ds1AttemptGrantedLogReqDto reqDto);

    /**
     * 查詢 3ds 1.0 交易紀錄
     */
    PagingResultDTO<TransactionRecordV1ResDTO> query3dsV1TransactionRecords(
      TransactionRecordV1ReqDTO queryDto);

    /**
     * 取得 3ds 1.0 交易紀錄摘要
     */
    Optional<TransactionSummaryV1ResDTO> get3dsV1TransactionSummary(
      TransactionSummaryV1ReqDTO queryDto);

    /**
     * 取得 3ds 1.0 交易紀錄詳細內容
     */
    Optional<TransactionDetailV1ResDTO> get3dsV1TransactionDetail(String transactionLogId);

    /**
     * 查詢 3ds 1.0 交易紀錄報表資料 - by 查詢條件
     */
    PagingResultDTO<TransactionReportV1ResDTO> query3dsV1TransactionRecordsReport(
      TransactionRecordV1ReqDTO queryDto);

    /**
     * 查詢 3ds 1.0 交易紀錄報表資料 - by ids
     */
    PagingResultDTO<TransactionReportV1ResDTO> query3dsV1TransactionRecordsReport(
      IdsQueryReqDTO queryDto);

    Optional<BinRangeV1ResDTO> get3dsV1BinRange(BinRangeV1QueryReqDTO queryDto);

    /**
     * 取得 3ds 1.0 BinRange
     */
    Optional<BinRangeV1ResDTO> get3dsV1BinRangeById(Long tCardnoSetupAuditStatusId);

    /**
     * 查詢 3ds 1.0 BinRange
     */
    PagingResultDTO<BinRangeV1ListResDTO> list3dsV1BinRange(BinRangeV1ListReqDTO queryDto);

    /**
     * 新增 3ds 1.0 BinRange
     */
    BinRangeV1ModifyResDTO save3dsV1BinRange(BinRangeV1SaveReqDTO createDto);

    /**
     * 更新 3ds 1.0 BinRange
     */
    BinRangeV1ModifyResDTO update3dsV1BinRange(BinRangeV1UpdateReqDTO updateDto);

    /**
     * 驗證重複資料 3ds 1.0 BinRange
     */
    boolean exiting3dsV1BinRangeConflict(BinRangeV1ValidReqDTO updateDto);

    /**
     * 取得審核狀態 3ds 1.0 BinRange
     */
    AuditStatus get3dsV1BinRangeAuditStatusById(Long tCardnoSetupAuditStatusId);

    /**
     * 更新審核狀態 3ds 1.0 BinRange
     */
    boolean update3dsV1BinRangeAuditStatus(BinRangeV1UpdateAuditStatusReqDTO updateDto);

    /**
     * 刪除 3ds 1.0 BinRange
     */
    boolean delete3dsV1BinRange(BinRangeV1DeleteReqDTO deleteDto);
}
