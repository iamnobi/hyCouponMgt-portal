
package com.cherri.acs_portal.dto.blackList.output;

import com.cherri.acs_portal.controller.request.BlackListIpGroupOperationReqDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.BlackListIpGroupDO;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BlackListIpGroupDTO extends AuditableDTO {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long issuerBankId;

    private Integer originVersion;
    private String ip;
    private Integer cidr;
    private Integer blockTimes;

    private AuditStatus auditStatus;
    private String creator;
    private Long createMillis;
    private String updater;

    public BlackListIpGroupDTO(BlackListIpGroupOperationReqDTO reqDto, String user) {
        this.id = reqDto.getId();
        this.issuerBankId = reqDto.getIssuerBankId();
        this.ip = reqDto.getIp();
        this.cidr = reqDto.getCidr();
        this.auditStatus = reqDto.getAuditStatus();
        this.creator = user;
    }

    public static BlackListIpGroupDTO valueOf(BlackListIpGroupDO entity) {
        BlackListIpGroupDTO dto = new BlackListIpGroupDTO();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setOriginVersion(entity.getOriginVersion());
        dto.setIp(entity.getIp());
        dto.setCidr(entity.getCidr());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setCreator(entity.getCreator());
        dto.setCreateMillis(entity.getCreateMillis());
        dto.setUpdater(entity.getUpdater());
        return dto;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_IP;
    }

}
