package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.request.PluginRuntimePropertyCollectionDTO;
import com.cherri.acs_portal.controller.response.PluginIssuerRuntimeResDTO;
import com.cherri.acs_portal.service.PluginService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PluginFacade {

    private final PluginService pluginService;

    @Autowired
    public PluginFacade(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    public List<PluginIssuerRuntimeResDTO> getAllPluginIssuerRuntimeProperty(Long issuerBankId) {
        return pluginService.getAllPluginRuntimeIssuerProperty(issuerBankId);
    }

    public void updateAllPluginIssuerRuntimeProperty(Long issuerBankId, String user,
      List<PluginRuntimePropertyCollectionDTO> propertyCollectionDTOList) {
        pluginService
          .updateAllPluginRuntimeIssuerProperty(issuerBankId, user, propertyCollectionDTOList);
    }
}
