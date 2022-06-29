package com.cherri.acs_portal.service;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.acs_integrator.HealthCheckDTO;
import com.cherri.acs_portal.dto.report.SystemHealthResponseDTO;
import com.cherri.acs_portal.dto.system.CardBrandDTO;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.manager.AcsKernelManager;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.AuthenticationLogDAO;
import ocean.acs.models.dao.SystemSettingDAO;
import ocean.acs.models.dao.TransactionLogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SystemHealthService {

    private final AuthenticationLogDAO authLogDao;
    private final TransactionLogDAO txLogDao;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;
    private final SystemSettingDAO systemSettingDAO;

  private final AcsKernelManager acsKernelManager;
  private final AcsIntegratorManager integratorManager;

    @Autowired
    public SystemHealthService(
            AuthenticationLogDAO authLogDao,
            TransactionLogDAO txLogDao,
            CardBrandConfigurationHelper cardBrandConfigurationHelper,
            SystemSettingDAO systemSettingDAO,
            AcsKernelManager acsKernelManager,
            AcsIntegratorManager integratorManager) {
        this.authLogDao = authLogDao;
        this.txLogDao = txLogDao;
        this.cardBrandConfigurationHelper = cardBrandConfigurationHelper;
        this.systemSettingDAO = systemSettingDAO;
        this.acsKernelManager = acsKernelManager;
        this.integratorManager = integratorManager;
    }

  public SystemHealthResponseDTO doSystemHealthCheck() {
    SystemHealthResponseDTO systemHealth = new SystemHealthResponseDTO();
    try {
      // Integrator health check
      integratorManager
          .healthCheck()
          .ifPresent(
              integratorHealthResult -> {
                HealthCheckDTO dto = new HealthCheckDTO();
                dto.setAcsDatabaseStatus(integratorHealthResult.getAcsDatabaseStatus());
                dto.setCcDcDatabaseStatus(integratorHealthResult.getCcDcDatabaseStatus());
                dto.setOtpDatabaseStatus(integratorHealthResult.getOtpDatabaseStatus());
                dto.setHsmStatus(integratorHealthResult.getHsmStatus());
                systemHealth.setAcsIntegrator(dto);
              });

      // Kernel health check
      doKernelSystemHealthCheck(systemHealth);
    } catch (Exception e) {
      log.error("[doSystemHealthCheck] unknown exception", e);
      systemHealth.setAcsIntegrator(new HealthCheckDTO());
    }
    return systemHealth;
  }

  private void doKernelSystemHealthCheck(SystemHealthResponseDTO systemHealth) {
    SystemHealthResponseDTO.KernelHealth kernelHealth = new SystemHealthResponseDTO.KernelHealth();
    try {
      // Ping ACS-Kernel
      SystemHealthResponseDTO.KernelHealth.ApServerStatus[] pingResult = acsKernelManager.ping();
      kernelHealth.setApServerStatus(pingResult);

      // DB health status
      kernelHealth.setDatabaseStatus(true);
      for (SystemHealthResponseDTO.KernelHealth.ApServerStatus apServerStatus : pingResult) {
        if (!apServerStatus.isStatus()) {
          kernelHealth.setDatabaseStatus(false);
          break;
        }
      }

      // If DB error
      if(!kernelHealth.getDatabaseStatus()){
        return;
      }

      // Transaction count
      final LocalDateTime now = LocalDateTime.now();
      final LocalDateTime lastHour = now.minusHours(1);
      final long startTimeMillis =
          lastHour.atZone(EnvironmentConstants.ACS_TIMEZONE_ID).toInstant().toEpochMilli();
      final long endTimeMillis =
          now.atZone(EnvironmentConstants.ACS_TIMEZONE_ID).toInstant().toEpochMilli();
      log.debug(
          "[doKernelSystemHealthCheck] transaction count, startTimeMillis={}, endTimeMillis={}",
          startTimeMillis,
          endTimeMillis);
      final Long transactionTotal = authLogDao.countByTimeMillis(startTimeMillis, endTimeMillis);
      kernelHealth.setTransactionTotal(transactionTotal);

        List<CardBrandDTO> cardBrandList = cardBrandConfigurationHelper.findCardBrandList();
      // Card brand response time
        SystemHealthResponseDTO.KernelHealth.CardBrandStatus[] cardBrandStatuses =
          new SystemHealthResponseDTO.KernelHealth.CardBrandStatus[cardBrandList.size() - 1];
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
      int idx = 0;
        for (CardBrandDTO cardBrand : cardBrandList) {
            Double responseTime =
              txLogDao
                .statisticsRResResponseTime(cardBrand.getName(), startTimeMillis, endTimeMillis);
            responseTime = Double.parseDouble(decimalFormat.format(responseTime));
            cardBrandStatuses[idx] =
              new SystemHealthResponseDTO.KernelHealth.CardBrandStatus(
                cardBrand.name(), responseTime);
            idx++;
        }
      kernelHealth.setCardBrands(cardBrandStatuses);
    } catch (Exception e) {
      log.error("[doKernelSystemHealthCheck] unknown exception", e);
    } finally {
      systemHealth.setAcsKernel(kernelHealth);
    }
  }

  public Boolean checkDatabaseStatus() {
      try {
        systemSettingDAO.findByKey("AMERICAN_EXPRESS.connect.timeout.rreq");
        return true;
      } catch (Exception e) {
        log.error("[checkDatabaseStatus] unable to connect to database", e);
        return false;
      }
  }
}
