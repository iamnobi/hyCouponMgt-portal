package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.controller.request.ClassicRbaSettingUpdateReqDto;
import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.service.ClassicRbaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClassicRbaSettingAuditWrapper implements
  AuditServiceWrapper<ClassicRbaSettingUpdateReqDto> {

    private final ClassicRbaService classicRbaService;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<ClassicRbaSettingUpdateReqDto> add(ClassicRbaSettingUpdateReqDto draftData) {
        return Optional.empty();
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<ClassicRbaSettingUpdateReqDto> update(ClassicRbaSettingUpdateReqDto draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        return Optional.of(classicRbaService.updateClassicRbaSetting(draftData));
    }

    @Override
    public Optional<ClassicRbaSettingUpdateReqDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional
          .of(objectMapper.readValue(draftInJson, ClassicRbaSettingUpdateReqDto.class));
    }

    @Override
    public Optional<ClassicRbaSettingUpdateReqDto> getOriginalDataDTO(AuditableDTO draftData) {
        return classicRbaService.getClassicRbaSetting(draftData.getIssuerBankId())
          .map(ClassicRbaSettingUpdateReqDto::valueOf);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(ClassicRbaSettingUpdateReqDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(ClassicRbaSettingUpdateReqDto draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(ClassicRbaSettingUpdateReqDto originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<ClassicRbaSettingUpdateReqDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            return false;
        }

        return originDataOpt.get().getAuditStatus() == AuditStatus.PUBLISHED;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        Optional<ClassicRbaSettingUpdateReqDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update classic rba setting with id: " + draftData
                .getId());
        }
        ClassicRbaSettingUpdateReqDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.AUDITING);
        classicRbaService.updateClassicRbaSetting(originData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        Optional<ClassicRbaSettingUpdateReqDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update classic rba setting with id: " + draftData
                .getId());
        }
        ClassicRbaSettingUpdateReqDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.PUBLISHED);
        classicRbaService.updateClassicRbaSetting(originData);
        return true;
    }
}
