package com.cherri.acs_portal.dto.attempt;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.acs_integrator.Get3ds1AttemptGrantedLogResDto;
import com.cherri.acs_portal.dto.whitelist.AttemptGrantedDTO;
import com.cherri.acs_portal.util.AcsPortalUtil;
import com.neovisionaries.i18n.CurrencyCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import ocean.acs.models.data_object.portal.GrantedTransactionLogDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class GrantedLogDTO {

    private Long authMillis;
    private Long triesPermitted;
    private Double maxMoney;
    /** Format:Numeric */
    private String currency;

    private String approved;
    private List<GrantedTransactionLogDTO> transactionLogList;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    public static GrantedLogDTO valueOf(AttemptGrantedDTO attemptGrantedDTO,
      List<GrantedTransactionLogDO> transactionLogList) {
        GrantedLogDTO dto = new GrantedLogDTO();
        dto.setApproved(attemptGrantedDTO.getCreator());
        dto.setMaxMoney(attemptGrantedDTO.getMaxMoney());

        CurrencyCode code = AcsPortalUtil
          .currencyCodeStrToCurrencyCode(attemptGrantedDTO.getCurrency());
        String currencyName = code == null ? "unknown" : code.name();
        dto.setCurrency(currencyName);

        dto.setTriesPermitted(attemptGrantedDTO.getTriesPermitted());
        dto.setAuthMillis(attemptGrantedDTO.getCreateMillis());
        dto.setTransactionLogList(transactionLogList.stream().map(
          GrantedTransactionLogDTO::valueOf
        ).collect(Collectors.toList()));

        return dto;
    }

  public static GrantedLogDTO valueOf(Get3ds1AttemptGrantedLogResDto resDto) {
    GrantedLogDTO dto = new GrantedLogDTO();
    dto.setAuthMillis(resDto.getAuthMillis());
    dto.setMaxMoney(resDto.getMaxMoney());
    dto.setCurrency(resDto.getCurrency());
    dto.setTriesPermitted(resDto.getTriesPermitted());
    dto.setApproved(resDto.getApproved());
    dto.setTransactionLogList(resDto.getTransactionLogList());
    return dto;
  }
}
