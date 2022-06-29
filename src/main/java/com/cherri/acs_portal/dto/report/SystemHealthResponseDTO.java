package com.cherri.acs_portal.dto.report;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.acs_integrator.HealthCheckDTO;
import lombok.*;

@Data
@NoArgsConstructor
public class SystemHealthResponseDTO {

  private HealthCheckDTO acsIntegrator;

  private KernelHealth acsKernel;

  @Data
  @NoArgsConstructor
  public static class KernelHealth {

    private ApServerStatus[] apServerStatus = new ApServerStatus[0];

    private Boolean databaseStatus = false;

    private Long transactionTotal = 0L;

    private CardBrandStatus[] cardBrands = new CardBrandStatus[0];

    @ToString
    public static class CardBrandStatus {
      @Getter @Setter private String name;
      @Getter private Double delayTime;
      @Getter @Setter private Boolean status;

      public CardBrandStatus(String name, Double delayTime) {
        this.name = name;
        this.delayTime = delayTime;
        setDelayTime(delayTime);
      }

      private void setDelayTime(Double delayTime) {
        this.status = EnvironmentConstants.SYSTEM_HEALTH_NORMAL >= delayTime;
      }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApServerStatus {
      private String serverUrl = "";
      private boolean status = false;
    }
  }
}
