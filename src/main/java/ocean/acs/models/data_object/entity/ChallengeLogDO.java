package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeLogDO {

    private Long id;
    private Long transactionLogID;
    private String challengeCancel;
    private String challengeNoEntry;
    private String challengeWindowSize;
    private String messageVersion;
    private String transStatus;
    private String threeDSRequestorAppURL;
    private Boolean oobContinue;
    private String oobAppLabel;
    private String oobAppURL;
    private String oobContinueLabel;
    private String resendChallenge;
    private String acsCounterAtoS;
    private String sdkCounterStoA;
    private String acsHTML;
    private String acsUiType;
    private String challengeAddInfo;
    private String challengeCompletionInd;
    private String challengeInfoHeader;
    private String challengeInfoLabel;
    private String challengeInfoText;
    private String challengeInfoTextIndicator;
    private String expandInfoLabel;
    private String expandInfoText;
    private String iiExtraHigh;
    private String iiHigh;
    private String iiMedium;
    private String piExtraHigh;
    private String piHigh;
    private String piMedium;
    private String resendInformationLabel;
    private String submitAuthenticationLabel;
    private String whyInfoLabel;
    private String whyInfoText;
    private String whitelistingDataEntry;
    private String whitelistingInfoText;
    private String cresMessageVersion;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public static ChallengeLogDO valueOf(ocean.acs.models.oracle.entity.ChallengeLog e) {
        return new ChallengeLogDO(e.getId(), e.getTransactionLogID(), e.getChallengeCancel(),
                e.getChallengeNoEntry(), e.getChallengeWindowSize(), e.getMessageVersion(),
                e.getTransStatus(), e.getThreeDSRequestorAppURL(), e.getOobContinue(),
                e.getOobAppLabel(), e.getOobAppURL(), e.getOobContinueLabel(),
                e.getResendChallenge(), e.getAcsCounterAtoS(), e.getSdkCounterStoA(),
                e.getAcsHTML(), e.getAcsUiType(), e.getChallengeAddInfo(),
                e.getChallengeCompletionInd(), e.getChallengeInfoHeader(),
                e.getChallengeInfoLabel(), e.getChallengeInfoText(),
                e.getChallengeInfoTextIndicator(), e.getExpandInfoLabel(), e.getExpandInfoText(),
                e.getIiExtraHigh(), e.getIiHigh(), e.getIiMedium(), e.getPiExtraHigh(),
                e.getPiHigh(), e.getPiMedium(), e.getResendInformationLabel(),
                e.getSubmitAuthenticationLabel(), e.getWhyInfoLabel(), e.getWhyInfoText(),
                e.getWhitelistingDataEntry(), e.getWhitelistingInfoText(),
                e.getCresMessageVersion(), e.getSysCreator(), e.getCreateMillis(),
                e.getSysUpdater(), e.getUpdateMillis());
    }

    public static ChallengeLogDO valueOf(ocean.acs.models.sql_server.entity.ChallengeLog e) {
        return new ChallengeLogDO(e.getId(), e.getTransactionLogID(), e.getChallengeCancel(),
                e.getChallengeNoEntry(), e.getChallengeWindowSize(), e.getMessageVersion(),
                e.getTransStatus(), e.getThreeDSRequestorAppURL(), e.getOobContinue(),
                e.getOobAppLabel(), e.getOobAppURL(), e.getOobContinueLabel(),
                e.getResendChallenge(), e.getAcsCounterAtoS(), e.getSdkCounterStoA(),
                e.getAcsHTML(), e.getAcsUiType(), e.getChallengeAddInfo(),
                e.getChallengeCompletionInd(), e.getChallengeInfoHeader(),
                e.getChallengeInfoLabel(), e.getChallengeInfoText(),
                e.getChallengeInfoTextIndicator(), e.getExpandInfoLabel(), e.getExpandInfoText(),
                e.getIiExtraHigh(), e.getIiHigh(), e.getIiMedium(), e.getPiExtraHigh(),
                e.getPiHigh(), e.getPiMedium(), e.getResendInformationLabel(),
                e.getSubmitAuthenticationLabel(), e.getWhyInfoLabel(), e.getWhyInfoText(),
                e.getWhitelistingDataEntry(), e.getWhitelistingInfoText(),
                e.getCresMessageVersion(), e.getSysCreator(), e.getCreateMillis(),
                e.getSysUpdater(), e.getUpdateMillis());
    }

}
