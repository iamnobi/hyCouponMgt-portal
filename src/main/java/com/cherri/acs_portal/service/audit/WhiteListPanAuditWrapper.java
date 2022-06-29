package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.input.WhiteListPanCreateDTO;
import com.cherri.acs_portal.service.WhiteListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 新增白名單新增審核Wrapper
 */
@Service
public class WhiteListPanAuditWrapper implements AuditServiceWrapper<WhiteListPanCreateDTO> {

    private final WhiteListService whiteListService;
    private final ObjectMapper objectMapper;

    @Autowired
    public WhiteListPanAuditWrapper(WhiteListService whiteListService, ObjectMapper objectMapper) {
        this.whiteListService = whiteListService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<WhiteListPanCreateDTO> add(WhiteListPanCreateDTO draftData) {
        return Optional.of(whiteListService.addWhiteListPan(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        whiteListService.deleteById(draftData);
        return true;
    }

    @Override
    public Optional<WhiteListPanCreateDTO> update(WhiteListPanCreateDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<WhiteListPanCreateDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, WhiteListPanCreateDTO.class));
    }

    @Override
    public Optional<WhiteListPanCreateDTO> getOriginalDataDTO(AuditableDTO draftData) {
        return whiteListService.getWhiteListPanDtoById(draftData.getId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(WhiteListPanCreateDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(WhiteListPanCreateDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(WhiteListPanCreateDTO originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<WhiteListPanCreateDTO> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            return false;
        }

        WhiteListPanCreateDTO originData = originDataOpt.get();
        return originData.getAuditStatus() == AuditStatus.PUBLISHED;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        Optional<WhiteListPanCreateDTO> originDataOpt =
          whiteListService.getWhiteListPanDtoById(draftData.getId());
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update White List Pan with id: "
                + draftData.getId()
                + ", issuerBankId: "
                + draftData.getIssuerBankId());
        }

        WhiteListPanCreateDTO originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.AUDITING);
        whiteListService.updateWhiteListPan(originData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        Optional<WhiteListPanCreateDTO> originDataOpt =
          whiteListService.getWhiteListPanDtoById(draftData.getId());
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update White List Pan with id: "
                + draftData.getId()
                + ", issuerBankId: "
                + draftData.getIssuerBankId());
        }

        WhiteListPanCreateDTO originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.PUBLISHED);
        whiteListService.updateWhiteListPan(originData);
        return true;
    }
}
