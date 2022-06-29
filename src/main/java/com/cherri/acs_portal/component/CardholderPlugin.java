package com.cherri.acs_portal.component;

import com.cherri.acs_kernel.plugin.CardholderInterface;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderGetAuthChannelInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderGetCardListInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderGetInfoInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderInitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderVerifyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderGetAuthChannelResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderGetCardListResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderGetCardholderInfoResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderVerifyResultDTO;
import com.cherri.acs_portal.model.dao.IssuerRuntimePropertyDAOWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.PluginType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.stereotype.Component;

@Component("PortalCardholderPlugin")
@Log4j2
public class CardholderPlugin extends PluginBase {

  @Getter
  private final CardholderInterface pluginImpl;
  private final Map<Integer, IssuerPropertyDefinition> propertyDefinitionMap;
  private final IssuerRuntimePropertyDAOWrapper runtimePropertyDAO;

  private final HsmPlugin hsmPlugin;

  public CardholderPlugin(
    CardholderInterface pluginImpl,
    IssuerRuntimePropertyDAOWrapper runtimePropertyDAO,
    HsmPlugin hsmPlugin) {
    super(runtimePropertyDAO);

    this.pluginImpl = pluginImpl;
    this.runtimePropertyDAO = runtimePropertyDAO;
    this.hsmPlugin = hsmPlugin;
    List<IssuerPropertyDefinition> propertyDefinitionList =
      pluginImpl.getIssuerPropertyDefinitionList();

    this.propertyDefinitionMap = new HashMap<>();
    if (propertyDefinitionList != null && !propertyDefinitionList.isEmpty()) {
      for (IssuerPropertyDefinition propDef : propertyDefinitionList) {
        propertyDefinitionMap.put(propDef.getPropertyId(), propDef);
      }
    }

    this.pluginImpl.initialize(
      CardholderInitializeInvokeDTO.builder().systemPropertiesMap(new HashMap<>()).build());
  }

  public CardholderGetCardListResultDTO getCardList(
    Long issuerBankId, String bankCode, String cardType, String cardholderId) {
    Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
      getIssuerRuntimeProperty(issuerBankId);
    CardholderGetCardListInvokeDTO invokeDTO =
      CardholderGetCardListInvokeDTO.builder()
        .bankCode(bankCode)
        .cardType(cardType)
        .cardholderId(cardholderId)
        .issuerPropertiesMap(issuerRuntimeProp)
        .systemPropertiesMap(new HashMap<>())
        .build();
    CardholderGetCardListResultDTO resultDTO = pluginImpl.getCardList(invokeDTO);

    ensureInvokeSuccessOrThrow(resultDTO);

    return resultDTO;
  }

  public CardholderGetAuthChannelResultDTO getChallengeChannelInfo(
    Long issuerBankId,
    String bankCode,
    String cardNumber,
    String authChannelId,
    String integratorId) {
    Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
      getIssuerRuntimeProperty(issuerBankId);

    CardholderGetAuthChannelInvokeDTO invokeDTO =
      CardholderGetAuthChannelInvokeDTO.builder()
        .bankCode(bankCode)
        .authChannelId(authChannelId)
        .cardNumber(cardNumber)
        .integratorId(integratorId)
        .issuerPropertiesMap(issuerRuntimeProp)
        .systemPropertiesMap(new HashMap<>())
        .build();

    if (log.isDebugEnabled()) {
      log.debug("[getChallengeChannelInfo] invoke params={}", invokeDTO);
    }

    // Do plugin invoke
    CardholderGetAuthChannelResultDTO resultDTO = pluginImpl.getChallengeChannelInfo(invokeDTO);

    // Plugin response
    ensureInvokeSuccessOrThrow(resultDTO);

    return resultDTO;
  }

  public CardholderVerifyResultDTO verifyCard(
    Long issuerBankId, String bankCode, String cardNumber, String cardType, String expireDate) {
    Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
      getIssuerRuntimeProperty(issuerBankId);

    CardholderVerifyInvokeDTO invokeDTO =
      CardholderVerifyInvokeDTO.builder()
        .bankCode(bankCode)
        .cardNumber(cardNumber)
        .cardType(cardType)
        .expireDate(expireDate)
        .issuerPropertiesMap(issuerRuntimeProp)
        .systemPropertiesMap(new HashMap<>())
        .build();

    if (log.isDebugEnabled()) {
      log.debug("[verifyCard] invoke params={}", invokeDTO);
    }

    CardholderVerifyResultDTO resultDTO = pluginImpl.verifyCard(invokeDTO);

    ensureInvokeSuccessOrThrow(resultDTO);

    return resultDTO;
  }

  public CardholderGetCardholderInfoResultDTO getCardHolderInfo(
    Long issuerBankId, String bankCode, String cardholderId, String cardType, String cardNumber) {
    Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
      getIssuerRuntimeProperty(issuerBankId);
    CardholderGetInfoInvokeDTO invokeDTO =
      CardholderGetInfoInvokeDTO.builder()
        .bankCode(bankCode)
        .cardholderId(cardholderId)
        .cardType(cardType)
        .cardNumber(cardNumber)
        .issuerPropertiesMap(issuerRuntimeProp)
        .systemPropertiesMap(new HashMap<>())
        .build();
    CardholderGetCardholderInfoResultDTO resultDTO = pluginImpl.getCardHolderInfo(invokeDTO);

    ensureInvokeSuccessOrThrow(resultDTO);

    return resultDTO;
  }

  @Override
  public String getName() {
    return pluginImpl.getPluginName();
  }

  @Override
  public PluginType getPluginType() {
    return PluginType.CARDHOLDER;
  }

  @Override
  public List<IssuerPropertyDefinition> getPropertyDefinitionList() {
    return pluginImpl.getIssuerPropertyDefinitionList();
  }

  @Override
  public IssuerPropertyDefinition findPropertyDefinitionById(int propertyId) {
    return propertyDefinitionMap.get(propertyId);
  }

  private void ensureInvokeSuccessOrThrow(ResultDTO pluginResultDTO) {
    if (pluginResultDTO.isSuccess()) {
      return;
    }

    log.error("[CardholderPluginHelper] plugin error", pluginResultDTO.getException());
    throw new OceanException(
      ResultStatus.CARDHOLDER_PLUGIN_EXCEPTION,
      ResultStatus.CARDHOLDER_PLUGIN_EXCEPTION.toString());
  }
}
