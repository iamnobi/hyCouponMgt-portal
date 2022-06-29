package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.system.CardBrandLogoUpdateDTO;
import com.cherri.acs_portal.service.SystemSettingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.models.dao.CardBrandLogoDAO;
import ocean.acs.models.data_object.entity.CardBrandLogoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardBrandLogoWrapper implements AuditServiceWrapper<CardBrandLogoUpdateDTO> {

    @Autowired
    private SystemSettingService systemSettingService;
    @Autowired
    private CardBrandLogoDAO cardBrandLogoDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<CardBrandLogoUpdateDTO> add(CardBrandLogoUpdateDTO draftData) {
        return Optional.of(systemSettingService.createCardBrandLogo(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<CardBrandLogoUpdateDTO> update(CardBrandLogoUpdateDTO draftData) {
        return Optional.of(systemSettingService.updateCardBrandLogo(draftData));
    }

    @Override
    public Optional<CardBrandLogoUpdateDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, CardBrandLogoUpdateDTO.class));
    }

    @Override
    public Optional<CardBrandLogoUpdateDTO> getOriginalDataDTO(AuditableDTO draftData) {
        Optional<CardBrandLogoDO> cardBrandLogoOpt = cardBrandLogoDao.findById(draftData.getId());
        if (!cardBrandLogoOpt.isPresent()) {
            throw new NoSuchDataException("Id:" + draftData.getId() + " not found.");
        } else {
            return Optional.of(CardBrandLogoUpdateDTO.valueOf(cardBrandLogoOpt.get()));
        }
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(CardBrandLogoUpdateDTO draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(CardBrandLogoUpdateDTO draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public void filterForAuditUsed(CardBrandLogoUpdateDTO originalData) {
        originalData.setAuditStatus(null);
        originalData.setUser(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<CardBrandLogoUpdateDTO> originalDataOpt = this.getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        CardBrandLogoDO cardBrandLogo = cardBrandLogoDao.findById(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("mark", "cardBrandLogo", draftData));
        cardBrandLogo.setAuditStatus(AuditStatus.AUDITING.getSymbol());
        cardBrandLogoDao.save(cardBrandLogo);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        CardBrandLogoDO cardBrandLogo = cardBrandLogoDao.findById(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("mark", "cardBrandLogo", draftData));
        cardBrandLogo.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        cardBrandLogoDao.save(cardBrandLogo);
        return true;
    }

}
