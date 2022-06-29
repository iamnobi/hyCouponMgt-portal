package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BlackListIpGroupDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private Integer originVersion;
    private String ip;
    private Integer cidr;
    private String transStatus;
    private String auditStatus;
    private Long startTime;
    private Long endTime;

    public BlackListIpGroupDO(Long id, Long issuerBankId, Integer originVersion, String ip,
      Integer cidr, String transStatus, String auditStatus, String creator, Long createMillis,
      String updater, Long updateMillis, Boolean deleteFlag, String deleter,
      Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.originVersion = originVersion;
        this.ip = ip;
        this.cidr = cidr;
        this.transStatus = transStatus;
        this.auditStatus = auditStatus;
    }

    public static BlackListIpGroupDO valueOf(ocean.acs.models.oracle.entity.BlackListIpGroup e) {
        return new BlackListIpGroupDO(e.getId(), e.getIssuerBankId(), e.getOriginVersion(),
          e.getIp(), e.getCidr(), e.getTransStatus(), e.getAuditStatus(), e.getCreator(),
          e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
          e.getDeleter(), e.getDeleteMillis());
    }

    public static BlackListIpGroupDO valueOf(ocean.acs.models.sql_server.entity.BlackListIpGroup e) {
        return new BlackListIpGroupDO(e.getId(), e.getIssuerBankId(), e.getOriginVersion(),
          e.getIp(), e.getCidr(), e.getTransStatus(), e.getAuditStatus(), e.getCreator(),
          e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
          e.getDeleter(), e.getDeleteMillis());
    }

}
