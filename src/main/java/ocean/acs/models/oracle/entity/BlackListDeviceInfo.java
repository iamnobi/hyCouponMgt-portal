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
import ocean.acs.models.data_object.entity.BlackListDeviceInfoDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_BLACK_LIST_DEVICE_INFO)
public class BlackListDeviceInfo extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "black_list_device_info_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "BLACK_LIST_DEVICE_INFO_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "black_list_device_info_seq_generator")
    @Column(name = DBKey.COL_BLACK_LIST_DEVICE_INFO_ID)
    private Long id;

    @Column(name = DBKey.COL_BLACK_LIST_DEVICE_INFO_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_DEVICE_INFO_DEVICE_ID)
    private String deviceID;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_DEVICE_INFO_DEVICE_CHANNEL)
    private String deviceChannel;

    @Column(name = DBKey.COL_BLACK_LIST_DEVICE_INFO_BROWSER_USER_AGENT)
    private String browserUserAgent;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_DEVICE_INFO_IP)
    private String ip;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_DEVICE_INFO_TRANS_STATUS)
    private String transStatus;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public BlackListDeviceInfo(Long id, Long issuerBankId, String deviceID, String deviceChannel,
            String browserUserAgent, String ip, String transStatus, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.deviceID = deviceID;
        this.deviceChannel = deviceChannel;
        this.browserUserAgent = browserUserAgent;
        this.ip = ip;
        this.transStatus = transStatus;
        this.auditStatus = auditStatus;
    }

    public static BlackListDeviceInfo valueOf(BlackListDeviceInfoDO d) {
        return new BlackListDeviceInfo(d.getId(), d.getIssuerBankId(), d.getDeviceID(),
                d.getDeviceChannel(), d.getBrowserUserAgent(), d.getIp(), d.getTransStatus(),
                d.getAuditStatus(), d.getCreator(), d.getCreateMillis(), d.getUpdater(),
                d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

}
