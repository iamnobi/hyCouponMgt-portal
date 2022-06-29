package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.cardholder.ThreeDSVerifyDTO;
import com.cherri.acs_portal.service.PanInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 3DS驗證啟用狀態審核Wrapper
 */
@Service
public class ThreeDSVerifyAuditWrapper implements AuditServiceWrapper<ThreeDSVerifyDTO> {

    private final PanInfoService panInfoService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ThreeDSVerifyAuditWrapper(
      PanInfoService panInfoService,
      ObjectMapper objectMapper) {
        this.panInfoService = panInfoService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<ThreeDSVerifyDTO> add(ThreeDSVerifyDTO draftData) {
        return Optional.empty();
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<ThreeDSVerifyDTO> update(ThreeDSVerifyDTO draftData) {
        return Optional.of(panInfoService.update3DSVerifyStatus(draftData));
    }

    @Override
    public Optional<ThreeDSVerifyDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, ThreeDSVerifyDTO.class));
    }

    @Override
    public Optional<ThreeDSVerifyDTO> getOriginalDataDTO(AuditableDTO draftData)
      throws DatabaseException {
        ThreeDSVerifyDTO dto = (ThreeDSVerifyDTO) draftData;
        return panInfoService.getThreeDSVerifyDTOByPanInfoId(dto.getPanId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(ThreeDSVerifyDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(ThreeDSVerifyDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(ThreeDSVerifyDTO originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) throws DatabaseException {

        Optional<ThreeDSVerifyDTO> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            return false;
        }

        ThreeDSVerifyDTO originData = originDataOpt.get();
        boolean isPublished = AuditStatus.PUBLISHED == originData.getAuditStatus();
        /*
        因資料來源可能沒有填AuditStatus=P，所以這邊也判斷Unknown狀態
         */
        boolean isUnknown = AuditStatus.UNKNOWN == originData.getAuditStatus();
        return isPublished || isUnknown;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) throws DatabaseException {
        ThreeDSVerifyDTO dto = (ThreeDSVerifyDTO) draftData;
        Optional<ThreeDSVerifyDTO> originDataOpt = panInfoService
          .getThreeDSVerifyDTOByPanInfoId(dto.getPanId());
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update pan info with id: " + draftData.getId()
                + ", issuerBankId: " + draftData.getIssuerBankId());
        }

        ThreeDSVerifyDTO originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.AUDITING);
        panInfoService.updateAudit3DSVerifyStatus(originData);

        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) throws DatabaseException {
        ThreeDSVerifyDTO dto = (ThreeDSVerifyDTO) draftData;
        Optional<ThreeDSVerifyDTO> originDataOpt = panInfoService
          .getThreeDSVerifyDTOByPanInfoId(dto.getPanId());
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update pan info with id: " + draftData.getId()
                + ", issuerBankId: " + draftData.getIssuerBankId());
        }
        ThreeDSVerifyDTO originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.PUBLISHED);
        panInfoService.updateAudit3DSVerifyStatus(originData);

        return true;

    }
}
