package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.response.BankDataKeyResDTO;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.controller.response.OtpSendingSettingResDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerBankAdminDto;
import com.cherri.acs_portal.dto.bank.IssuerBankAdminListDto;
import com.cherri.acs_portal.dto.bank.IssuerBankDto;
import com.cherri.acs_portal.dto.bank.IssuerHandingFeeQueryResultDto;
import com.cherri.acs_portal.dto.bank.IssuerHandingFeeUpdateDto;
import com.cherri.acs_portal.dto.bank.IssuerLogoUpdateDto;
import com.cherri.acs_portal.dto.bank.IssuerQueryDto;
import com.cherri.acs_portal.dto.bank.IssuerTradingChannelQueryResultDto;
import com.cherri.acs_portal.dto.bank.IssuerTradingChannelUpdateDto;
import com.cherri.acs_portal.dto.bank.OtpSendingKeyUploadDto;
import com.cherri.acs_portal.dto.bank.OtpSendingSettingDto;
import com.cherri.acs_portal.service.AuditService;
import com.cherri.acs_portal.service.BankDataKeyService;
import com.cherri.acs_portal.service.BankManagementService;
import com.cherri.acs_portal.service.UserManagementService;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.data_object.entity.OtpSendingSettingDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankManagementFacade {

    private final BankManagementService bankService;
    private final UserManagementService bankAdminService;
    private final BankDataKeyService bankDataKeyService;

    private final AuditService auditService;

    @Autowired
    public BankManagementFacade(
      BankManagementService bankService,
      UserManagementService bankAdminService,
      BankDataKeyService bankDataKeyService,
      AuditService auditService) {
        this.bankService = bankService;
        this.bankAdminService = bankAdminService;
        this.bankDataKeyService = bankDataKeyService;
        this.auditService = auditService;
    }


    public List<IssuerBankDto> getIssuerBankAndOrgList() {
        return bankService.findAll();
    }

    /**
     * 查詢銀行（分頁）
     */
    public List<IssuerBankDto> getIssuerBankList() {
        return bankService.getIssuerBankList();
    }

    /**
     * 查詢銀行 by id
     */
    public Optional<IssuerBankDto> getIssuerBankById(Long id) {
        return bankService.getIssuerBankById(id);
    }

    //00003 allow control asc/3ds oper id by user
    public Optional<IssuerBankDto> getAcsOperatorId(String acsOperatorId) throws DatabaseException {

        return bankService.getAcsOperatorId(acsOperatorId);
    }

    /**
     * 建立會員銀行
     */
    public DataEditResultDTO createIssuerBank(IssuerBankDto issuerBankDto) {
        try {
            if (bankService.isBankCodeExisted(issuerBankDto.getCode())) {
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
                  "bank code is existed.");
            }

            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BANK_MANAGE);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.BANK_MANAGE, AuditActionType.ADD, issuerBankDto);
            } else {
                issuerBankDto.setAuditStatus(AuditStatus.PUBLISHED);
                IssuerBankDto result = bankService.createIssuerBank(issuerBankDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 修改會員銀行
     */
    public DataEditResultDTO updateIssuerBank(IssuerBankDto bankDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BANK_MANAGE);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.BANK_MANAGE, AuditActionType.UPDATE, bankDto);
            } else {
                bankDto.setAuditStatus(AuditStatus.PUBLISHED);
                IssuerBankDto result = bankService.updateIssuerBank(bankDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 刪除會員銀行
     */
    public DataEditResultDTO deleteIssuerBank(DeleteDataDTO deleteDataDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BANK_MANAGE);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.BANK_MANAGE, AuditActionType.DELETE,
                    deleteDataDto);
            } else {
                deleteDataDto.setAuditStatus(AuditStatus.PUBLISHED);
                DeleteDataDTO result = bankService.deleteIssuerBank(deleteDataDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 查詢銀行管理員（多筆）
     */
    public List<IssuerBankAdminListDto> queryIssuerBankAdminList(Long issuerBankId) {
        return bankAdminService.getIssuerBankAdminList(issuerBankId);
    }

    /**
     * 新增銀行管理員
     */
    public DataEditResultDTO createIssuerBankAdmin(IssuerBankAdminDto issuerBankAdminDto) {
        try {
            if (bankAdminService.isDuplicateAccount(issuerBankAdminDto.getIssuerBankId(),
              issuerBankAdminDto.getAccount())) {
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, "account is existed");
            }

            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BANK_MANAGE);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.BANK_ADMIN, AuditActionType.ADD,
                    issuerBankAdminDto);
            } else {
                issuerBankAdminDto.setAuditStatus(AuditStatus.PUBLISHED);
                IssuerBankAdminDto result = bankAdminService
                  .createIssuerBankAdmin(issuerBankAdminDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 修改銀行管理員
     */
    public DataEditResultDTO updateIssuerBankAdmin(IssuerBankAdminDto issuerBankAdminDto) {
        try {
            if (bankAdminService.isAccountExistedInOtherPeople(issuerBankAdminDto.getId(),
              issuerBankAdminDto.getAccount())) {
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, "account is existed");
            }

            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BANK_MANAGE);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.BANK_ADMIN, AuditActionType.UPDATE,
                    issuerBankAdminDto);
            } else {
                issuerBankAdminDto.setAuditStatus(AuditStatus.PUBLISHED);
                IssuerBankAdminDto result = bankAdminService
                  .updateIssuerBankAdmin(issuerBankAdminDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 刪除銀行管理員
     */
    public DataEditResultDTO deleteIssuerBankAdmin(DeleteDataDTO deleteDataDTO) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BANK_MANAGE);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.BANK_ADMIN, AuditActionType.DELETE,
                    deleteDataDTO);
            } else {
                deleteDataDTO.setAuditStatus(AuditStatus.PUBLISHED);
                DeleteDataDTO result = bankAdminService.deleteIssuerBankAdmin(deleteDataDTO);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }


    public IssuerTradingChannelQueryResultDto queryByIssuerBankId(IssuerQueryDto issuerQueryDto) {
        return bankService.queryByIssuerBankId(issuerQueryDto);
    }

    public DataEditResultDTO updateIssuerTradingChannel(IssuerTradingChannelUpdateDto updateDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BANK_CHANNEL);
            if (isDemandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.BANK_CHANNEL, AuditActionType.UPDATE, updateDto);
            }
            updateDto.setAuditStatus(AuditStatus.PUBLISHED);
            updateDto = bankService.updateIssuerTradingChannel(updateDto);
            return new DataEditResultDTO(updateDto);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO updateLogo(IssuerLogoUpdateDto issuerLogoUpdateDto) {
        try {
            boolean isDemandAuditing = auditService.isAuditingOnDemand(AuditFunctionType.BANK_LOGO);
            if (isDemandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.BANK_LOGO, AuditActionType.UPDATE, issuerLogoUpdateDto);
            }
            issuerLogoUpdateDto.setAuditStatus(AuditStatus.PUBLISHED);
            IssuerLogoUpdateDto result = bankService.updateIssuerBrandLogo(issuerLogoUpdateDto);
            return new DataEditResultDTO(result);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public AbstractMap.SimpleEntry<AuditStatus, byte[]> queryIssuerLogoByIssuerBankId(
      IssuerQueryDto issuerQueryDto) {
        return bankService.queryIssuerLogoByIssuerBankId(issuerQueryDto);
    }

    public IssuerHandingFeeQueryResultDto queryIssuerHandingFeeByIssuerBankId(
      IssuerQueryDto issuerQueryDto) {
        return bankService.queryIssuerHandingFeeByIssuerBankId(issuerQueryDto);
    }

    public DataEditResultDTO updateIssuerHandingFee(
      IssuerHandingFeeUpdateDto issuerHandingFeeUpdateDto) {
        try {
            boolean isDemandAuditing = auditService.isAuditingOnDemand(AuditFunctionType.BANK_FEE);
            if (isDemandAuditing) {
                return auditService.requestAudit(AuditFunctionType.BANK_FEE, AuditActionType.UPDATE,
                  issuerHandingFeeUpdateDto);
            } else {
                issuerHandingFeeUpdateDto.setAuditStatus(AuditStatus.PUBLISHED);
                IssuerHandingFeeUpdateDto result = bankService
                  .updateIssuerHandingFee(issuerHandingFeeUpdateDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public OtpSendingSettingResDTO getOtpSendingSetting(Long issuerBankId) {
        return bankService.getOtpSendingSetting(issuerBankId);
    }

    public DataEditResultDTO updateOtpSendingSetting(OtpSendingSettingDto otpSendingSettingDto) {
        try {
            OtpSendingSettingDO setting =
              bankService
                .getOteSendingSettingByIssuerBankId(otpSendingSettingDto.getIssuerBankId());
            otpSendingSettingDto.setId(setting.getId());

            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.BANK_OTP_SENDING);
            if (isDemandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.BANK_OTP_SENDING, AuditActionType.UPDATE, otpSendingSettingDto);
            } else {
                otpSendingSettingDto
                  .setAuditStatus(ocean.acs.commons.enumerator.AuditStatus.PUBLISHED);
                OtpSendingSettingDto result = bankService
                  .updateOtpSendingSetting(otpSendingSettingDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO uploadJweOrJwsKey(OtpSendingKeyUploadDto otpSendingKeyUploadDto) {
        try {
            OtpSendingSettingDO setting =
              bankService
                .getOteSendingSettingByIssuerBankId(otpSendingKeyUploadDto.getIssuerBankId());
            otpSendingKeyUploadDto.setId(setting.getId());

            boolean isDemandAuditing =
              auditService.isAuditingOnDemand(AuditFunctionType.BANK_OTP_KEY_UPLOAD);
            if (isDemandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.BANK_OTP_KEY_UPLOAD, AuditActionType.UPDATE,
                  otpSendingKeyUploadDto);
            } else {
                otpSendingKeyUploadDto
                  .setAuditStatus(ocean.acs.commons.enumerator.AuditStatus.PUBLISHED);
                OtpSendingKeyUploadDto result = bankService
                  .uploadJweOrJwsKey(otpSendingKeyUploadDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public BankDataKeyResDTO queryBankDataKey(Long issuerBankId) {
        return bankDataKeyService.queryBankDataKey(issuerBankId);
    }

    public BankDataKeyResDTO createBankDataKey(Long issuerBankId, String creator) {
        return bankDataKeyService.createBankDataKey(issuerBankId, creator);
    }
}
