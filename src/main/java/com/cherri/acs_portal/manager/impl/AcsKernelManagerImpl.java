package com.cherri.acs_portal.manager.impl;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.report.SystemHealthResponseDTO;
import com.cherri.acs_portal.manager.AcsKernelManager;
import com.cherri.acs_portal.util.OkHttpConnector;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AcsKernelManagerImpl implements AcsKernelManager {

  private final OkHttpConnector kernelHttpConnector;

  @Autowired
  public AcsKernelManagerImpl(OkHttpConnector kernelHttpConnector) {
    this.kernelHttpConnector = kernelHttpConnector;
  }

  @Override
  public SystemHealthResponseDTO.KernelHealth.ApServerStatus[] ping() {
    String[] kernelUrlAry = EnvironmentConstants.getAcsKernelList();
    SystemHealthResponseDTO.KernelHealth.ApServerStatus[] apServerStatusAry =
        new SystemHealthResponseDTO.KernelHealth.ApServerStatus[kernelUrlAry.length];
    int idx = 0;
    for (String kernelUrl : kernelUrlAry) {
      String pingUrl = kernelUrl + "/ping";

      try {
        Response response = kernelHttpConnector.get(pingUrl);
        if (response.isSuccessful()) {
          apServerStatusAry[idx] =
              new SystemHealthResponseDTO.KernelHealth.ApServerStatus(kernelUrl, true);
        } else {
          apServerStatusAry[idx] =
              new SystemHealthResponseDTO.KernelHealth.ApServerStatus(kernelUrl, false);
        }
      } catch (Exception e) {
        log.error("[ping] unknown exception, kernelUrl={}", kernelUrl, e);
        apServerStatusAry[idx] =
            new SystemHealthResponseDTO.KernelHealth.ApServerStatus(kernelUrl, false);
      }
      idx++;
    }
    return apServerStatusAry;
  }
}
