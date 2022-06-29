package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListPanDTO;
import com.cherri.acs_portal.service.BlackListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 卡號黑名單 */
@Service
public class BlackListPanAuditWrapper implements AuditServiceWrapper<BlackListPanDTO> {

  @Autowired HttpSession httpSession;
  @Autowired BlackListService blackListService;
  @Autowired ObjectMapper objectMapper;

  @Override
  public Optional<BlackListPanDTO> add(BlackListPanDTO draftData) {
      draftData.setAuditStatus(AuditStatus.PUBLISHED);
      return Optional.of(blackListService.addBlackListPanByManual(draftData));
  }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        blackListService.deletePanById(draftData);
        return true;
    }

    @Override
    public Optional<BlackListPanDTO> update(BlackListPanDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<BlackListPanDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, BlackListPanDTO.class));
    }

    @Override
    public Optional<BlackListPanDTO> getOriginalDataDTO(AuditableDTO draftData)
      throws OceanException, DatabaseException {
        BlackListPanDTO dto =
          blackListService.findOneBlackListPan(draftData.getIssuerBankId(), draftData.getId())
            .orElseThrow(() -> new NoSuchDataException("Id:" + draftData.getId() + " not found."));
        return Optional.of(dto);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(BlackListPanDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(BlackListPanDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(BlackListPanDTO originalData) {
        originalData.setAuditStatus(null);
        originalData.setCreator(null);
        originalData.setUpdater(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) throws DatabaseException {
        Optional<BlackListPanDTO> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) throws DatabaseException {
        BlackListPanDTO originalData =
          blackListService.findOneBlackListPan(draftData.getIssuerBankId(), draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("mark", "blacklistPan", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        originalData.setUpdater(getUser());
        blackListService.updateBlackListPan(originalData);
        return true;
    }

    private String getUser() {
        return (String) httpSession.getAttribute(SessionAttributes.ACCOUNT);
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) throws DatabaseException {
        BlackListPanDTO originalData =
          blackListService.findOneBlackListPan(draftData.getIssuerBankId(), draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("unmark", "blacklistPan", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        originalData.setUpdater(getUser());
        blackListService.updateBlackListPan(originalData);
        return true;
    }
}
