package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.OtpSendingSettingDto;
import com.cherri.acs_portal.service.BankManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.models.dao.OtpSendingSettingDAO;
import ocean.acs.models.data_object.entity.OtpSendingSettingDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpSendingSettingWrapper implements AuditServiceWrapper<OtpSendingSettingDto> {

    @Autowired
    private BankManagementService bankManagementService;
    @Autowired
    private OtpSendingSettingDAO otpSendingSettingDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<OtpSendingSettingDto> add(OtpSendingSettingDto draftData) {
        return Optional.empty(); // not need add function currently.
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false; // not need delete function currently.
    }

    @Override
    public Optional<OtpSendingSettingDto> update(OtpSendingSettingDto draftData) {
        return Optional.of(bankManagementService.updateOtpSendingSetting(draftData));
    }

    @Override
    public Optional<OtpSendingSettingDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, OtpSendingSettingDto.class));
    }

    @Override
    public Optional<OtpSendingSettingDto> getOriginalDataDTO(AuditableDTO draftData) {
        Optional<OtpSendingSettingDO> otpSendingSettingOpt = otpSendingSettingDao
          .findByIdAndNotDelete(draftData.getId());
        if (!otpSendingSettingOpt.isPresent()) {
            throw new NoSuchDataException(
              "OtpSendingSetting Id:" + draftData.getId() + " not found.");
        } else {
            OtpSendingSettingDto dto = OtpSendingSettingDto.valueOf(otpSendingSettingOpt.get());
            return Optional.of(dto);
        }
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(OtpSendingSettingDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(OtpSendingSettingDto draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(OtpSendingSettingDto originalData) {
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<OtpSendingSettingDto> originalDataOpt = this.getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        OtpSendingSettingDO otpSendingSetting = otpSendingSettingDao
          .findByIdAndNotDelete(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("mark", "otpSendingSetting", draftData));
        otpSendingSetting.setAuditStatus(AuditStatus.AUDITING.getSymbol());
        otpSendingSettingDao.save(otpSendingSetting);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        OtpSendingSettingDO otpSendingSetting = otpSendingSettingDao
          .findByIdAndNotDelete(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("unmark", "otpSendingSetting", draftData));
        otpSendingSetting.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        otpSendingSettingDao.save(otpSendingSetting);
        return true;
    }

}
