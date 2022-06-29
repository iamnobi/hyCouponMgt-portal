package com.cherri.acs_kernel.plugin.hsm.impl;

import com.cherri.acs_kernel.plugin.HSMInterface;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.hsm.impl.config.CloudHsmPluginConfig;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * When 3DS Authentication occurred, the ACS will send the issuer properties (defined in
 * 'issuerPropertyDefinitionList') to this plugin.
 *
 * <p>Step1. Define the issuerPropertyDefinitionList and all property definitions will show in ACS
 * Portal let user to configure.
 *
 * <p>Step2. Implement the plugin functions
 *
 * <p>Step3. Test this plugin by unit test
 */
@Log4j2
public abstract class HSMImplBase implements HSMInterface {

  // plugin def
  /** Name of this plugin */
  // this will show in ACS Portal
  private String pluginName;

  @Autowired
  public final void setPluginName(CloudHsmPluginConfig cloudHsmPluginConfig) {
    this.pluginName = cloudHsmPluginConfig.getPluginName();
  }

  // IssuerPropertyDefinition
  //  private final IssuerPropertyDefinition PROP_1 =
  //      IssuerPropertyDefinition.newInstance(
  //          "issuerPhoneNumber", false, "An AES Key for encryption and decryption");
  //  private final IssuerPropertyDefinition PROP_2 =
  //      IssuerPropertyDefinition.newInstance("issuerPublicKey", false, "A public Key for
  // encryption");
  //  private final List<IssuerPropertyDefinition> issuerPropertyDefinitionList =
  //      Arrays.asList(PROP_1, PROP_2);
  private final List<IssuerPropertyDefinition> issuerPropertyDefinitionList = new ArrayList<>();

  // do not modify this
  @Override
  public final String getPluginName() {
    return pluginName;
  }

  // do not modify this
  @Override
  public final List<IssuerPropertyDefinition> getIssuerPropertyDefinitionList() {
    return issuerPropertyDefinitionList;
  }
}
