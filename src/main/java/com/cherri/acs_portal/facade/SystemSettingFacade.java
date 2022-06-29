package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.request.KeyImportReqDTO;
import com.cherri.acs_portal.controller.request.KeyImportVerifyReqDTO;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.controller.response.GeneralSettingDTO;
import com.cherri.acs_portal.controller.response.KeyImportVerifyResDTO;
import com.cherri.acs_portal.controller.response.LanguageResDTO;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.system.*;
import com.cherri.acs_portal.service.*;
import com.cherri.acs_portal.util.CsvUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.data_object.entity.SystemSettingDO;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Slf4j
@Service
public class SystemSettingFacade {

    private final MccManagerService mccManagerService;
    private final SystemSettingService systemSettingService;
    private final CavvKeyImportService cavvKeyImportService;
    private final AuditService auditService;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;

    public ApiResponse<Boolean> importMccFromCsv(
        MultipartFile mccCsvFile,
        String creator
    ) {
        byte[] mccFileBytes;
        List<String> mccList;
        Set<String> mccSet;

        try {
            mccFileBytes = IOUtils.toByteArray(mccCsvFile.getInputStream());
            mccList = CsvUtil.simpleCSVReader(mccFileBytes);
            mccSet = new HashSet<>(mccList);

            mccSet.remove("");
            mccSet.remove(null);

            boolean hasInvalidFormat = mccSet.stream().anyMatch(
                mcc -> mcc.length() != 4
            );

            /* Verify Mcc is match 4 digit */
            if (hasInvalidFormat) {
                log.warn("[importMccFromCsv] Mcc must be to 4 digit.");
                return new ApiResponse<>(
                    ResultStatus.INVALID_FORMAT,
                    "Mcc must be to 4 digit."
                );
            }

            return new ApiResponse<>(
                mccManagerService.saveMcc(mccSet, creator)
            );
        } catch (IOException e) {
            log.error("[importMccFromCsv] IOException happened.", e);
            return new ApiResponse<>(
                ResultStatus.INVALID_FORMAT,
                "Mcc Csv invalid format."
            );
        } catch (OceanException e) {
            return new ApiResponse<>(
                e.getResultStatus(),
                e.getMessage()
            );
        } catch (Exception e) {
            log.error("[importMccFromCsv] Exception happened.", e);
            return new ApiResponse<>(
                ResultStatus.SERVER_ERROR,
                "Exception happened."
            );
        }
    }

    public ApiResponse<List<String>> queryAllMcc() {
        try {
            return new ApiResponse<>(mccManagerService.queryAllMcc());
        } catch (OceanException e) {
            return new ApiResponse<>(
                e.getResultStatus(),
                e.getMessage()
            );
        }
    }

    public PagingResultDTO<BinRangeDTO> queryBinRange(BinRangeQueryDTO queryDto) {
        return systemSettingService.queryBinRange(queryDto);
    }

    public DataEditResultDTO addBinRange(BinRangeDTO binDto) throws OceanException {
        try {
            // Check binRange
            systemSettingService.validateBinRange(binDto);

            // 進入審核流程
            boolean demandAuditing =
                    auditService.isAuditingOnDemand(AuditFunctionType.SYS_BIN_RANGE);
            BinRangeDTO result;

            if (demandAuditing) {
                return auditService.requestAudit(
                        AuditFunctionType.SYS_BIN_RANGE, AuditActionType.ADD, binDto);
            } else {
                binDto.setAuditStatus(AuditStatus.PUBLISHED);
                result = systemSettingService.addBinRange(binDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO updateBinRange(BinRangeDTO binDto) throws OceanException {
        try {
            // Check binRange
            systemSettingService.validateBinRange(binDto);

            // 進入審核流程
            boolean demandAuditing =
                    auditService.isAuditingOnDemand(AuditFunctionType.SYS_BIN_RANGE);

            if (demandAuditing) {
                return auditService.requestAudit(
                        AuditFunctionType.SYS_BIN_RANGE, AuditActionType.UPDATE, binDto);
            } else {
                binDto.setAuditStatus(AuditStatus.PUBLISHED);
                BinRangeDTO result = systemSettingService.updateBinRange(binDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO deleteBinRange(DeleteDataDTO deleteDataDTO) {
        try {
            boolean demandAuditing =
                    auditService.isAuditingOnDemand(AuditFunctionType.SYS_BIN_RANGE);

            if (demandAuditing) {
                return auditService.requestAudit(
                        AuditFunctionType.SYS_BIN_RANGE, AuditActionType.DELETE, deleteDataDTO);
            }
            deleteDataDTO.setAuditStatus(AuditStatus.PUBLISHED);
            systemSettingService.deleteBinRange(deleteDataDTO);
            return new DataEditResultDTO(deleteDataDTO);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /** 查詢基本設定 */
    public GeneralSettingDTO getGeneralSetting() {
        List<SystemSettingDO> generalSetting = systemSettingService.getGeneralSetting();
        return GeneralSettingDTO.valueOf(generalSetting);
    }

    /** 修改基本設定 */
    public void updateGeneralSetting(GeneralSettingUpdateDTO updateDto, String user) {
        updateDto.setUser(user);
        systemSettingService.updateGeneralSetting(updateDto);
    }

    /** 取得語系清單 */
    public List<LanguageResDTO> listLanguage() {
        return systemSettingService.listLanguage();
    }


    public List<ErrorIssueGroupDTO> getErrorIssueGroup() {
        return systemSettingService.getErrorIssueGroup();
    }

    public DataEditResultDTO updateErrorCodeMessageGroup(ErrorIssueGroupDTO errorCodeGroupDTO) {
        try {
            if (auditService.isAuditingOnDemand(AuditFunctionType.SYS_ERROR_CODE)) {
                return auditService.requestAudit(
                        AuditFunctionType.SYS_ERROR_CODE,
                        AuditActionType.UPDATE,
                        errorCodeGroupDTO);
            } else {
                errorCodeGroupDTO.setAuditStatus(AuditStatus.PUBLISHED);
                return new DataEditResultDTO(
                        systemSettingService.updateErrorCodeMessageGroup(errorCodeGroupDTO));
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public List<SecretKeyDTO> getKeyList(Long issuerBankId) throws OceanException {
        return systemSettingService.getKeyList(issuerBankId);
    }

    public DataEditResultDTO saveOrUpdateKey(SecretKeyDTO secretKeyDto) throws OceanException {
        try {
            if (auditService.isAuditingOnDemand(AuditFunctionType.SYS_KEY)) {
                return auditService.requestAudit(
                        AuditFunctionType.SYS_KEY, AuditActionType.UPDATE, secretKeyDto);
            } else {
                secretKeyDto.setAuditStatus(AuditStatus.PUBLISHED);
                SecretKeyDTO result = systemSettingService.saveOrUpdateKey(secretKeyDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }


    public void importKeyConfirm(KeyImportReqDTO reqDto, String updater) {
        // combine and import key to HSM
        String keyId = cavvKeyImportService.importKeyConfirm(reqDto);
        // Save keyId to DB
        systemSettingService.saveOrUpdateKeyOneKey(
                reqDto.getIssuerBankId(),
                reqDto.getCardBrand(),
                keyId,
                reqDto.getKeyType(),
                updater);
    }
}
