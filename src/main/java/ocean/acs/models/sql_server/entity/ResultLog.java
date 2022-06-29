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
import ocean.acs.models.data_object.entity.ResultLogDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_RESULT_LOG)
public class ResultLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_RESULT_LOG_ID)
    private Long id;

    // acsRenderingType
    @Column(name = DBKey.COL_RESULT_LOG_ART_ACS_INTERFACE)
    private String artAcsInterface;

    @Column(name = DBKey.COL_RESULT_LOG_ART_ACS_UI_TEMPLATE)
    private String artAcsUiTemplate;
    // acsRenderingType_end

    @Column(name = DBKey.COL_RESULT_LOG_AUTHENTICATION_METHOD)
    private String authenticationMethod;

    @Column(name = DBKey.COL_RESULT_LOG_AUTHENTICATION_TYPE)
    private String authenticationType;

    @Column(name = DBKey.COL_RESULT_LOG_AUTHENTICATION_VALUE_EN)
    private String authenticationValueEn;

    @Column(name = DBKey.COL_RESULT_LOG_CHALLENGE_CANCEL)
    private String challengeCancel;

    @Column(name = DBKey.COL_RESULT_LOG_ECI)
    private String eci;

    @Column(name = DBKey.COL_RESULT_LOG_INTERACTION_COUNTER)
    private String interactionCounter;

    @Column(name = DBKey.COL_RESULT_LOG_MESSAGE_CATEGORY)
    private String messageCategory;

    @Column(name = DBKey.COL_RESULT_LOG_MESSAGE_VERSION)
    private String messageVersion;

    @Column(name = DBKey.COL_RESULT_LOG_TRANS_STATUS)
    private String transStatus;

    @Column(name = DBKey.COL_RESULT_LOG_TRANS_STATUS_REASON)
    private String transStatusReason;

    @Column(name = DBKey.COL_RESULT_LOG_RESULTS_STATUS)
    private String resultsStatus;

    @Column(name = DBKey.COL_RESULT_LOG_WHITE_LIST_STATUS)
    private String whiteListStatus;

    @Column(name = DBKey.COL_RESULT_LOG_WHITE_LIST_STATUS_SOURCE)
    private String whiteListStatusSource;

    // RRes
    @Column(name = DBKey.COL_RESULT_LOG_RRES_MESSAGE_VERSION)
    private String rresMessageVersion;

    @NonNull
    @Column(name = DBKey.COL_RESULT_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_RESULT_LOG_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_RESULT_LOG_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_RESULT_LOG_UPDATE_MILLIS)
    private Long updateMillis;

    public static ResultLog valueOf(ResultLogDO d) {
        return new ResultLog(d.getId(), d.getArtAcsInterface(), d.getArtAcsUiTemplate(),
                d.getAuthenticationMethod(), d.getAuthenticationType(), d.getAuthenticationValueEn(),
                d.getChallengeCancel(), d.getEci(), d.getInteractionCounter(),
                d.getMessageCategory(), d.getMessageVersion(), d.getTransStatus(),
                d.getTransStatusReason(), d.getResultsStatus(), d.getWhiteListStatus(),
                d.getWhiteListStatusSource(), d.getRresMessageVersion(), d.getSysCreator(),
                d.getCreateMillis(), d.getSysUpdater(), d.getUpdateMillis());
    }

}
