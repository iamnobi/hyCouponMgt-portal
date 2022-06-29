package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.config.ClassicRbaProperties;
import com.cherri.acs_portal.config.ClassicRbaProperties.EnabledModules;
import com.cherri.acs_portal.controller.request.ClassicRbaReportReqDto;
import com.cherri.acs_portal.controller.request.ClassicRbaSettingUpdateReqDto;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.dto.rba.ClassicRbaReportDto;
import com.cherri.acs_portal.dto.rba.ClassicRbaSettingDto;
import com.cherri.acs_portal.dto.rba.ClassicRbaStatusDto;
import com.cherri.acs_portal.service.AuditService;
import com.cherri.acs_portal.service.ClassicRbaService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class ClassicRbaFacade {

    private final ClassicRbaService classicRbaService;
    private final AuditService auditService;
    private final ClassicRbaProperties classicRbaProperties;

    public Optional<ClassicRbaStatusDto> getClassicRbaStatus() {
        return classicRbaService.getClassicRbaStatus();
    }

    public Optional<ClassicRbaSettingDto> getClassicRbaSetting(long issuerBankId) {
        return classicRbaService.getClassicRbaSetting(issuerBankId);
    }

    public DataEditResultDTO updateClassicRbaSetting(ClassicRbaSettingUpdateReqDto updateDto) {
        try {
            // 沒有 enable 的 RBA 模組前端不會帶其設定上來，
            // 而有啟用的模組設定則必帶
            validateClassicRbaSettingUpdateReqDtoByEnabledModules(updateDto);

            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.CLASSIC_RBA);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.CLASSIC_RBA, AuditActionType.UPDATE, updateDto);
            } else {
                updateDto.setAuditStatus(AuditStatus.PUBLISHED);
                ClassicRbaSettingUpdateReqDto result = classicRbaService
                  .updateClassicRbaSetting(updateDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    private void validateClassicRbaSettingUpdateReqDtoByEnabledModules(
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        EnabledModules enabledModules = classicRbaProperties.getEnabledModules();
        if (enabledModules.isAPT() && updateReqDto.getPurchaseAmount() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing purchaseAmount argument");
        }
        if (enabledModules.isCDC() && updateReqDto.getCardholderData() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing cardholderData argument");
        }
        if (enabledModules.isCAC() && updateReqDto.getCumulativeAmount() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing cumulativeAmount argument");
        }
        if (enabledModules.isCTF() && updateReqDto.getCumulativeTransaction() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing cumulativeTransaction argument");
        }
        if (enabledModules.isLCC() && updateReqDto.getLocationConsistency() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing locationConsistency argument");
        }
        if (enabledModules.isBLC() && updateReqDto.getBrowserLanguage() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing browserLanguage argument");
        }
        if (enabledModules.isVPN() && updateReqDto.getVpn() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "missing vpn argument");
        }
        if (enabledModules.isPXY() && updateReqDto.getProxy() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "missing proxy argument");
        }
        if (enabledModules.isPBC() && updateReqDto.getPrivateBrowsing() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing privateBrowsing argument");
        }
        if (enabledModules.isDVC() && updateReqDto.getDeviceVariation() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing deviceVariation argument");
        }
        if (enabledModules.isMCC() && updateReqDto.getMcc() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "missing mcc argument");
        }
        if (enabledModules.isRPR() && updateReqDto.getRecurringPayment() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing recurringPayment argument");
        }
    }

    public Optional<ClassicRbaReportDto> getClassicRbaReport(ClassicRbaReportReqDto queryDto) {
        return classicRbaService.getClassicRbaReport(queryDto);
    }

    public void exportClassicRbaReport(ClassicRbaReportReqDto queryDto) {
        classicRbaService.exportClassicRbaReport(queryDto);
    }

}
