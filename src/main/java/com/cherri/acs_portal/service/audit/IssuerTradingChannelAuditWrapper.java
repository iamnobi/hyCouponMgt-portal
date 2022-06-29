package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerTradingChannelUpdateDto;
import com.cherri.acs_portal.service.BankManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 銀行交易管道設定
 */
@Service
public class IssuerTradingChannelAuditWrapper
  implements AuditServiceWrapper<IssuerTradingChannelUpdateDto> {

    @Autowired
    BankManagementService bankManagementService;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Optional<IssuerTradingChannelUpdateDto> add(IssuerTradingChannelUpdateDto draftData) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public Optional<IssuerTradingChannelUpdateDto> update(IssuerTradingChannelUpdateDto draftData) {
        return Optional.of(bankManagementService.updateIssuerTradingChannel(draftData));
    }

    @Override
    public Optional<IssuerTradingChannelUpdateDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional
          .of(objectMapper.readValue(draftInJson, IssuerTradingChannelUpdateDto.class));
    }

    @Override
    public Optional<IssuerTradingChannelUpdateDto> getOriginalDataDTO(AuditableDTO draftData)
      throws OceanException {
        IssuerTradingChannelUpdateDto dto =
          bankManagementService
            .findOneIssuerTradingChannel(draftData.getIssuerBankId())
            .orElseThrow(
              () ->
                new NoSuchDataException(
                  "[IssuerTradingChannel] IssuerBankId:"
                    + draftData.getIssuerBankId()
                    + " not found."));
        return Optional.of(dto);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(IssuerTradingChannelUpdateDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(IssuerTradingChannelUpdateDto draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(IssuerTradingChannelUpdateDto originalData) {
        originalData.setAuditStatus(null);
        originalData.setUser(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<IssuerTradingChannelUpdateDto> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        IssuerTradingChannelUpdateDto originalData =
          bankManagementService
            .findOneIssuerTradingChannel(draftData.getIssuerBankId())
            .orElseThrow(() -> getMarkAuditingException("mark", "issuerTradingChannel", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        bankManagementService.updateIssuerTradingChannel(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        IssuerTradingChannelUpdateDto originalData =
          bankManagementService
            .findOneIssuerTradingChannel(draftData.getIssuerBankId())
            .orElseThrow(
              () -> getMarkAuditingException("unmark", "issuerTradingChannel", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        bankManagementService.updateIssuerTradingChannel(originalData);
        return true;
    }
}
