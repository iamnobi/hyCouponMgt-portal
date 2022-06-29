package com.cherri.acs_portal.component;

import com.cherri.acs_kernel.plugin.OTPInterface;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import com.cherri.acs_kernel.plugin.dto.otp.invoke.GenerateInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.otp.invoke.InitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.otp.invoke.VerifyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.otp.result.GenerateResultDTO;
import com.cherri.acs_kernel.plugin.dto.otp.result.VerifyResultDTO;
import com.cherri.acs_portal.model.dao.IssuerRuntimePropertyDAOWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.PluginType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("PortalOtpPlugin")
@Log4j2
public class OtpPlugin extends PluginBase {

    @Getter
    private OTPInterface pluginImpl;
    private final Map<Integer, IssuerPropertyDefinition> propertyDefinitionMap;
    private final IssuerRuntimePropertyDAOWrapper runtimePropertyDAO;

    @Autowired
    public OtpPlugin(OTPInterface pluginImpl, IssuerRuntimePropertyDAOWrapper runtimePropertyDAO) {
        super(runtimePropertyDAO);

        this.pluginImpl = pluginImpl;
        this.runtimePropertyDAO = runtimePropertyDAO;
        List<IssuerPropertyDefinition> propertyDefinitionList =
          pluginImpl.getIssuerPropertyDefinitionList();

        this.propertyDefinitionMap = new HashMap<>();
        if (propertyDefinitionList != null && !propertyDefinitionList.isEmpty()) {
            for (IssuerPropertyDefinition propDef : propertyDefinitionList) {
                propertyDefinitionMap.put(propDef.getPropertyId(), propDef);
            }
        }

        this.pluginImpl.initialize(
          InitializeInvokeDTO.builder().systemPropertiesMap(new HashMap<>()).build());
    }

    // Params: acsTransId if for identify otp in transaction
    // Params: (deprecated) integratorId - for backward compatible
    public GenerateResultDTO generateOtp(Long issuerBankId, String acsTransID,
      String integratorId) {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
          getIssuerRuntimeProperty(issuerBankId);

        GenerateInvokeDTO invokeDTO =
          GenerateInvokeDTO.builder()
            .acsTransID(acsTransID)
            .integratorId(integratorId)
            .issuerPropertiesMap(issuerRuntimeProp)
            .systemPropertiesMap(new HashMap<>())
            .build();
        GenerateResultDTO resultDTO = pluginImpl.generate(invokeDTO);

        ensureInvokeSuccessOrThrow(resultDTO);

        return resultDTO;
    }

    public VerifyResultDTO verifyOtp(
      Long issuerBankId, String acsTransID, String integratorId, String authCode, String authId) {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
          getIssuerRuntimeProperty(issuerBankId);

        VerifyInvokeDTO invokeDTO =
          VerifyInvokeDTO.builder()
            .acsTransID(acsTransID)
            .integratorId(integratorId)
            .authCode(authCode)
            .authId(authId)
            .issuerPropertiesMap(issuerRuntimeProp)
            .systemPropertiesMap(new HashMap<>())
            .build();
        VerifyResultDTO resultDTO = pluginImpl.verify(invokeDTO);

        ensureInvokeSuccessOrThrow(resultDTO);

        return resultDTO;
    }

    @Override
    public String getName() {
        return pluginImpl.getPluginName();
    }

    @Override
    public PluginType getPluginType() {
        return PluginType.OTP;
    }

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

        log.error("[OTPPluginHelper] plugin error", pluginResultDTO.getException());
        throw new OceanException(
          ResultStatus.OTP_PLUGIN_EXCEPTION, ResultStatus.OTP_PLUGIN_EXCEPTION.toString());
    }
}
