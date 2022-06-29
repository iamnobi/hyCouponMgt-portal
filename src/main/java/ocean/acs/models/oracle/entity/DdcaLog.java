package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.DdcaLogDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_DDCA_LOG)
public class DdcaLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "ddca_log_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "DDCA_LOG_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ddca_log_seq_generator")
    @Column(name = DBKey.COL_DDCA_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_DDCA_LOG_TX_REPORT)
    private Long txReport;

    @Column(name = DBKey.COL_DDCA_LOG_DEVICE_ID)
    private String deviceId;

    @Column(name = DBKey.COL_DDCA_LOG_IS_IN_PRIVATE_BROWSING_MODE)
    private Integer isInPrivateBrowsingMode;

    @Column(name = DBKey.COL_DDCA_LOG_BROWSER_TIMEZONE_COUNTRY)
    private String browserTimezoneCountry;

    @Column(name = DBKey.COL_DDCA_LOG_IP_COUNTRY)
    private String ipCountry;

    @Column(name = DBKey.COL_DDCA_LOG_BROWSER_LANGUAGE_CODE)
    private String browserLanguageCode;

    @Column(name = DBKey.COL_DDCA_LOG_IS_VPN_ENABLED)
    private Integer isVpnEnabled;

    @Column(name = DBKey.COL_DDCA_LOG_IS_PROXY_ENABLED)
    private Integer isProxyEnabled;

    @Column(name = DBKey.COL_DDCA_LOG_ADDITIONAL_DATA)
    private String additionalData;

    @Column(name = DBKey.COL_DDCA_LOG_RESPONSE_TIME)
    private Long responseTime;

    @Column(name = DBKey.COL_DDCA_LOG_ERROR_STATUS)
    private String errorStatus;

    @Column(name = DBKey.COL_DDCA_LOG_ERROR_REASON)
    private String errorReason;

    @NotNull
    @Column(name = DBKey.COL_DDCA_LOG_SYS_CREATOR)
    private String sysCreator;

    @NotNull
    @Column(name = DBKey.COL_DDCA_LOG_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_DDCA_LOG_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_DDCA_LOG_UPDATE_MILLIS)
    private Long updateMillis;

    public static DdcaLog valueOf(DdcaLogDO d) {
        return new DdcaLog(d.getId(), d.getTxReport(), d.getDeviceId(),
                d.getIsInPrivateBrowsingMode(), d.getBrowserTimeZoneCountry(), d.getIpCountry(),
                d.getBrowserLanguageCode(), d.getIsVpnEnabled(), d.getIsProxyEnabled(),
                d.getAdditionalData(), d.getResponseTime(), d.getErrorStatus(), d.getErrorReason(),
                d.getSysCreator(), d.getCreateMillis(), d.getSysUpdater(), d.getUpdateMillis());
    }

}
