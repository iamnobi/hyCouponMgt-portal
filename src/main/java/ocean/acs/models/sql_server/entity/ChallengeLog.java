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
import ocean.acs.models.data_object.entity.ChallengeLogDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CHALLENGE_LOG)
public class ChallengeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_CHALLENGE_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_CHALLENGE_LOG_KERNEL_TRANSACTION_LOG_ID)
    private Long transactionLogID;

    @Column(name = DBKey.COL_CHALLENGE_LOG_CHALLENGE_CANCEL)
    private String challengeCancel;

    @Column(name = DBKey.COL_CHALLENGE_LOG_CHALLENGE_NO_ENTRY)
    private String challengeNoEntry;

    @Column(name = DBKey.COL_CHALLENGE_LOG_CHALLENGE_WINDOW_SIZE)
    private String challengeWindowSize;

    @Column(name = DBKey.COL_CHALLENGE_LOG_MESSAGE_VERSION)
    private String messageVersion;

    @Column(name = DBKey.COL_CHALLENGE_LOG_TRANS_STATUS)
    private String transStatus;

    @Column(name = DBKey.COL_CHALLENGE_LOG_THREE_D_S_REQUESTOR_APP_URL)
    private String threeDSRequestorAppURL;

    @Column(name = DBKey.COL_CHALLENGE_LOG_OOB_CONTINUE)
    private Boolean oobContinue;

    @Column(name = DBKey.COL_CHALLENGE_LOG_OOB_APP_LABEL)
    private String oobAppLabel;

    @Column(name = DBKey.COL_CHALLENGE_LOG_OOB_APP_URL)
    private String oobAppURL;

    @Column(name = DBKey.COL_CHALLENGE_LOG_OOB_CONTINUE_LABEL)
    private String oobContinueLabel;

    @Column(name = DBKey.COL_CHALLENGE_LOG_RESEND_CHALLENGE)
    private String resendChallenge;

    @Column(name = DBKey.COL_CHALLENGE_LOG_ACS_COUNTER_A_TO_S)
    private String acsCounterAtoS;

    @Column(name = DBKey.COL_CHALLENGE_LOG_SDK_COUNTER_S_TO_A)
    private String sdkCounterStoA;

    @Lob
    @Column(name = DBKey.COL_CHALLENGE_LOG_ACS_HTML)
    private String acsHTML;

    @Column(name = DBKey.COL_CHALLENGE_LOG_ACS_UI_TYPE)
    private String acsUiType;

    @Column(name = DBKey.COL_CHALLENGE_LOG_CHALLENGE_ADD_INFO)
    private String challengeAddInfo;

    @Column(name = DBKey.COL_CHALLENGE_LOG_CHALLENGE_COMPLETION_IND)
    private String challengeCompletionInd;

    @Column(name = DBKey.COL_CHALLENGE_LOG_CHALLENGE_INFO_HEADER)
    private String challengeInfoHeader;

    @Column(name = DBKey.COL_CHALLENGE_LOG_CHALLENGE_INFO_LABEL)
    private String challengeInfoLabel;

    @Column(name = DBKey.COL_CHALLENGE_LOG_CHALLENGE_INFO_TEXT)
    private String challengeInfoText;

    @Column(name = DBKey.COL_CHALLENGE_LOG_CHALLENGE_INFO_TEXT_INDICATOR)
    private String challengeInfoTextIndicator;

    @Column(name = DBKey.COL_CHALLENGE_LOG_EXPAND_INFO_LABEL)
    private String expandInfoLabel;

    @Column(name = DBKey.COL_CHALLENGE_LOG_EXPAND_INFO_TEXT)
    private String expandInfoText;

    // issuerImage
    @Column(name = DBKey.COL_CHALLENGE_LOG_II_EXTRA_HIGH)
    private String iiExtraHigh;

    @Column(name = DBKey.COL_CHALLENGE_LOG_II_HIGH)
    private String iiHigh;

    @Column(name = DBKey.COL_CHALLENGE_LOG_II_MEDIUM)
    private String iiMedium;
    // issuerImage_end

    // psImage
    @Column(name = DBKey.COL_CHALLENGE_LOG_PI_EXTRA_HIGH)
    private String piExtraHigh;

    @Column(name = DBKey.COL_CHALLENGE_LOG_PI_HIGH)
    private String piHigh;

    @Column(name = DBKey.COL_CHALLENGE_LOG_PI_MEDIUM)
    private String piMedium;
    // psImage_end

    @Column(name = DBKey.COL_CHALLENGE_LOG_RESEND_INFORMATION_LABEL)
    private String resendInformationLabel;

    @Column(name = DBKey.COL_CHALLENGE_LOG_SUBMIT_AUTHENTICATION_LABEL)
    private String submitAuthenticationLabel;

    @Column(name = DBKey.COL_CHALLENGE_LOG_WHY_INFO_LABEL)
    private String whyInfoLabel;

    @Column(name = DBKey.COL_CHALLENGE_LOG_WHY_INFO_TEXT)
    private String whyInfoText;

    @Column(name = DBKey.COL_CHALLENGE_LOG_WHITELISTING_DATA_ENTRY)
    private String whitelistingDataEntry;

    @Column(name = DBKey.COL_CHALLENGE_LOG_WHITELISTING_INFO_TEXT)
    private String whitelistingInfoText;

    // CRes
    @Column(name = DBKey.COL_CHALLENGE_LOG_CRES_MESSAGE_VERSION)
    private String cresMessageVersion;

    @NonNull
    @Column(name = DBKey.COL_CHALLENGE_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_CHALLENGE_LOG_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_CHALLENGE_LOG_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_CHALLENGE_LOG_UPDATE_MILLIS)
    private Long updateMillis;

    public static ChallengeLog valueOf(ChallengeLogDO d) {
        return new ChallengeLog(d.getId(), d.getTransactionLogID(), d.getChallengeCancel(),
                d.getChallengeNoEntry(), d.getChallengeWindowSize(), d.getMessageVersion(),
                d.getTransStatus(), d.getThreeDSRequestorAppURL(), d.getOobContinue(),
                d.getOobAppLabel(), d.getOobAppURL(), d.getOobContinueLabel(),
                d.getResendChallenge(), d.getAcsCounterAtoS(), d.getSdkCounterStoA(),
                d.getAcsHTML(), d.getAcsUiType(), d.getChallengeAddInfo(),
                d.getChallengeCompletionInd(), d.getChallengeInfoHeader(),
                d.getChallengeInfoLabel(), d.getChallengeInfoText(),
                d.getChallengeInfoTextIndicator(), d.getExpandInfoLabel(), d.getExpandInfoText(),
                d.getIiExtraHigh(), d.getIiHigh(), d.getIiMedium(), d.getPiExtraHigh(),
                d.getPiHigh(), d.getPiMedium(), d.getResendInformationLabel(),
                d.getSubmitAuthenticationLabel(), d.getWhyInfoLabel(), d.getWhyInfoText(),
                d.getWhitelistingDataEntry(), d.getWhitelistingInfoText(),
                d.getCresMessageVersion(), d.getSysCreator(), d.getCreateMillis(),
                d.getSysUpdater(), d.getUpdateMillis());
    }
}
