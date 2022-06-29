package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.oracle.entity.BlackListIpGroup;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PortalBlackListIpGroupDO extends AuditableDO {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long issuerBankId;

    private Integer originVersion;
    private String ip;
    private Integer cidr;
    private Integer blockTimes;

    private AuditStatus auditStatus;
    private String creator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String updater;

    public PortalBlackListIpGroupDO(BlackListIpGroupOperationReqDO reqDto, String user) {
        this.id = reqDto.getId();
        this.issuerBankId = reqDto.getIssuerBankId();
        this.ip = reqDto.getIp();
        this.cidr = reqDto.getCidr();
        this.auditStatus = reqDto.getAuditStatus();
        this.creator = user;
    }

    public static PortalBlackListIpGroupDO valueOf(BlackListIpGroup entity) {
        PortalBlackListIpGroupDO dto = new PortalBlackListIpGroupDO();
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setId(entity.getId());
        dto.setOriginVersion(entity.getOriginVersion());
        dto.setIp(entity.getIp());
        dto.setCidr(entity.getCidr());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_IP;
    }

}
