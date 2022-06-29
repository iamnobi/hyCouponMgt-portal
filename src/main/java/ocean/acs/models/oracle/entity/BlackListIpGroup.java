package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.BlackListIpGroupDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_BLACK_LIST_IP_GROUP)
public class BlackListIpGroup extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "black_list_ip_group_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "BLACK_LIST_IP_GROUP_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "black_list_ip_group_seq_generator")
    @Column(name = DBKey.COL_BLACK_LIST_IP_GROUP_ID)
    private Long id;

    @Column(name = DBKey.COL_BLACK_LIST_IP_GROUP_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_IP_GROUP_ORIGIN_VERSION)
    private Integer originVersion;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_IP_GROUP_IP)
    private String ip;

    @Column(name = DBKey.COL_BLACK_LIST_IP_GROUP_CIDR)
    private Integer cidr;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_IP_GROUP_TRANS_STATUS)
    private String transStatus;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public BlackListIpGroup(Long id, Long issuerBankId, Integer originVersion, String ip,
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

    public static BlackListIpGroup valueOf(BlackListIpGroupDO d) {
        return new BlackListIpGroup(d.getId(), d.getIssuerBankId(), d.getOriginVersion(),
                d.getIp(), d.getCidr(), d.getTransStatus(), d.getAuditStatus(), d.getCreator(),
                d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(),
                d.getDeleter(), d.getDeleteMillis());
    }
    
}
