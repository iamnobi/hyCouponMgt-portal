package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.attempt.GrantedLogDTO;
import com.cherri.acs_portal.dto.attempt.GrantedLogQueryDTO;
import com.cherri.acs_portal.dto.cardholder.HolderIdQueryDTO;
import com.cherri.acs_portal.dto.cardholder.HolderQueryDTO;
import com.cherri.acs_portal.dto.cardholder.HolderSummaryDTO;
import com.cherri.acs_portal.dto.cardholder.ThreeDSVerifiedLogDTO;
import com.cherri.acs_portal.dto.cardholder.ThreeDSVerifyDTO;
import com.cherri.acs_portal.dto.cardholder.UnlockOtpVerifyDTO;
import com.cherri.acs_portal.dto.whitelist.AttemptGrantedDTO;
import com.cherri.acs_portal.dto.whitelist.AttemptSettingLimitationDTO;
import com.cherri.acs_portal.service.AuditService;
import com.cherri.acs_portal.service.CardholderService;
import com.cherri.acs_portal.service.OperationLogService;
import com.cherri.acs_portal.service.PanInfoService;
import com.cherri.acs_portal.service.WhiteListAttemptService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.data_object.entity.OtpOperationLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CardHolderFacade {

    private final CardholderService cardholderService;
    private final WhiteListAttemptService whiteListAttemptService;
    private final OperationLogService operationLogService;
    private final PanInfoService panInfoService;
    private final AuditService auditService;
    private final HttpServletResponse response;

    @Autowired
    public CardHolderFacade(
      CardholderService cardholderService,
      WhiteListAttemptService whiteListAttemptService,
      OperationLogService operationLogService,
      PanInfoService panInfoService,
      AuditService auditService,
      HttpServletResponse response) {
        this.cardholderService = cardholderService;
        this.whiteListAttemptService = whiteListAttemptService;
        this.operationLogService = operationLogService;
        this.panInfoService = panInfoService;
        this.auditService = auditService;
        this.response = response;
    }

    public HolderSummaryDTO getHolderSummary(HolderQueryDTO queryDto) {
        return cardholderService.getCardholderSummary(queryDto);
    }

    /**
     * 更新3DS驗證啟用狀態
     */
    public DataEditResultDTO update3DSVerifyStatus(ThreeDSVerifyDTO verifyDTO) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.THREE_DS_VERIFY_ENABLED);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.THREE_DS_VERIFY_ENABLED, AuditActionType.UPDATE,
                    verifyDTO);
            } else {
                verifyDTO.setAuditStatus(AuditStatus.PUBLISHED);
                ThreeDSVerifyDTO result = panInfoService.update3DSVerifyStatus(verifyDTO);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * OTP解鎖
     */
    public DataEditResultDTO unlockOtp(UnlockOtpVerifyDTO unlockOtpVerifyDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.UNLOCK_OTP);
            if (isDemandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.UNLOCK_OTP, AuditActionType.UPDATE, unlockOtpVerifyDto);
            }
            unlockOtpVerifyDto.setAuditStatus(AuditStatus.PUBLISHED);
            UnlockOtpVerifyDTO result = panInfoService.unlockOtp(unlockOtpVerifyDto);
            return new DataEditResultDTO(result);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public AttemptSettingLimitationDTO getAttemptAuthorizeSetting(Long issuerBankId) {
        return whiteListAttemptService.getAttemptAuthorizeSetting(issuerBankId);
    }

    public PagingResultDTO<GrantedLogDTO> getAttemptGrantedLog(GrantedLogQueryDTO logQueryDTO) {
        try {
            return whiteListAttemptService.getAttemptGrantedLog(logQueryDTO);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO grantAttemptSetting(AttemptGrantedDTO attemptGrantedDto) {
        try {
            boolean isDemandAuditing = auditService.isAuditingOnDemand(AuditFunctionType.ATTEMPT);
            if (isDemandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.ATTEMPT, AuditActionType.ADD, attemptGrantedDto);
            }
            attemptGrantedDto.setAuditStatus(AuditStatus.PUBLISHED);
            AttemptGrantedDTO result =
              whiteListAttemptService.addAttemptGrantedSetting(attemptGrantedDto);
            return new DataEditResultDTO(result);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public void exportAttemptLogToCSV(GrantedLogQueryDTO logQueryDTO)
      throws IOException {
        PagingResultDTO<GrantedLogDTO> dataPage = getAttemptGrantedLog(logQueryDTO);
        List<GrantedLogDTO> dataList =
          dataPage.isEmpty() ? Collections.emptyList() : dataPage.getData();
        whiteListAttemptService.exportAttemptLogCSV(dataList, response);
    }

    public PagingResultDTO<ThreeDSVerifiedLogDTO> getThreeDSVerifiedLogByPanId(
      HolderIdQueryDTO queryDTO) {
        return operationLogService.getThreeDSVerifiedLogByCardHolderId(queryDTO);
    }

    public void exportThreeDSVerifiedLog(HolderIdQueryDTO queryDTO) throws IOException {
        operationLogService.exportThreeDSVerifiedLog(queryDTO, response);
    }

    public PagingResultDTO<OtpOperationLogDO> getOtpLogByPanId(HolderIdQueryDTO queryDTO) {
        return operationLogService.getOtpLogByPanId(queryDTO);
    }

    public void exportOtpOperationLog(HolderIdQueryDTO queryDTO) throws IOException {
        operationLogService.exportOtpOperationLog(queryDTO, response);
    }
}
