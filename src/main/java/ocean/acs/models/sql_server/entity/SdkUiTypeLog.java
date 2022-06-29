package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.SdkUiTypeLogDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_SDK_UI_TYPE_LOG)
public class SdkUiTypeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_SDK_UI_TYPE_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_SDK_UI_TYPE_LOG_AUTHENTICATION_LOG_ID)
    private Long authenticationLogID;

    @Column(name = DBKey.COL_SDK_UI_TYPE_LOG_SDK_UI_TYPE)
    private String sdkUiType;

    @NonNull
    @Column(name = DBKey.COL_SDK_UI_TYPE_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_SDK_UI_TYPE_LOG_CREATE_MILLIS)
    private Long createMillis;

    public static SdkUiTypeLog valueOf(SdkUiTypeLogDO d) {
        return new SdkUiTypeLog(d.getId(), d.getAuthenticationLogID(), d.getSdkUiType(),
                d.getSysCreator(), d.getCreateMillis());
    }

}
