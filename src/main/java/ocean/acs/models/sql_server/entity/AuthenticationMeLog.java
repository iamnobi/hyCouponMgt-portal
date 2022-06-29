package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.AuthenticationMeLogDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_AUTHENTICATION_ME_LOG)
public class AuthenticationMeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_AUTHENTICATION_ME_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_AUTHENTICATION_ME_LOG_CRITICALITY_INDICATOR)
    private Boolean criticalityIndicator;

    @Column(name = DBKey.COL_AUTHENTICATION_ME_LOG_AUTHENTICATION_LOG_ID)
    private Long authenticationLogId;

    @Column(name = DBKey.COL_AUTHENTICATION_ME_LOG_MESSAGE_TYPE)
    private String messageType;

    @Lob
    @Column(name = DBKey.COL_AUTHENTICATION_ME_LOG_DATA_BODY)
    private String data;

    @Column(name = DBKey.COL_AUTHENTICATION_ME_LOG_ME_ID)
    private String meID;

    @Column(name = DBKey.COL_AUTHENTICATION_ME_LOG_NAME)
    private String name;

    @NonNull
    @Column(name = DBKey.COL_AUTHENTICATION_ME_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_AUTHENTICATION_ME_LOG_CREATE_MILLIS)
    private Long createMillis;

    public static AuthenticationMeLog valueOf(AuthenticationMeLogDO d) {
        return new AuthenticationMeLog(d.getId(), d.getCriticalityIndicator(), d.getAuthenticationLogId(), d.getMessageType(),
                d.getData(), d.getMeID(), d.getName(), d.getSysCreator(), d.getCreateMillis());
    }
}
