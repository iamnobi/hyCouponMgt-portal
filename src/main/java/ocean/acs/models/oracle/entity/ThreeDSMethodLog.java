package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.ThreeDSMethodLogDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_THREE_D_S_METHOD_LOG)
public class ThreeDSMethodLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "three_d_s_method_log_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "THREE_D_S_METHOD_LOG_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "three_d_s_method_log_seq_generator")
    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_THREE_D_S_METHOD_NOTIFY_URL)
    private String threeDSMethodNotificationURL;

    @Lob
    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_BROWSER_ACCEPT_HEADER)
    private String BrowserAcceptHeader;

    @Lob
    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_BROWSER_ACCEPT_AGENT)
    private String browserUserAgent;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_BROWSER_JAVA_ENABLED)
    private Boolean browserJavaEnabled;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_BROWSER_LANGUAGE)
    private String browserLanguage;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_BROWSER_COLOR_DEPTH)
    private String browserColorDepth;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_BROWSER_SCREEN_HEIGHT)
    private String browserScreenHeight;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_BROWSER_SCREEN_WIDTH)
    private String browserScreenWidth;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_BROWSER_T_Z)
    private String browserTZ;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_BROWSER_PRIVATE_MODE)
    private Boolean browserPrivateMode;

    @NonNull
    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_THREE_D_S_METHOD_LOG_UPDATE_MILLIS)
    private Long updateMillis;

    public static ThreeDSMethodLog valueOf(ThreeDSMethodLogDO d) {
        return new ThreeDSMethodLog(d.getId(), d.getThreeDSMethodNotificationURL(),
                d.getBrowserAcceptHeader(), d.getBrowserUserAgent(), d.getBrowserJavaEnabled(),
                d.getBrowserLanguage(), d.getBrowserColorDepth(), d.getBrowserScreenHeight(),
                d.getBrowserScreenWidth(), d.getBrowserTZ(), d.getBrowserPrivateMode(),
                d.getSysCreator(), d.getCreateMillis(), d.getSysUpdater(), d.getUpdateMillis());
    }

}
