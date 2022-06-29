package com.cherri.acs_portal.controller.response;

import com.cherri.acs_portal.dto.blackList.output.BlackListIpGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BlackListIpGroupResDTO {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long issuerBankId;

    private Long createMillis;
    private String name;
    private Integer blockTimes;
    private AuditStatus auditStatus;

    public static BlackListIpGroupResDTO valueOf(BlackListIpGroupDTO serviceDto) {
        String name;
        if (null == serviceDto.getCidr()) {
            name = serviceDto.getIp();
        } else {
            name = String.format("%s/%d", serviceDto.getIp(), serviceDto.getCidr());
        }
        return new BlackListIpGroupResDTO(
          serviceDto.getId(),
          serviceDto.getIssuerBankId(),
          serviceDto.getCreateMillis(),
          name,
          serviceDto.getBlockTimes(),
          serviceDto.getAuditStatus());
    }
}
