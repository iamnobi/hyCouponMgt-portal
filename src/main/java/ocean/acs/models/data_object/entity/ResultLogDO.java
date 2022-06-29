package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultLogDO {

    private Long id;
    private String artAcsInterface;
    private String artAcsUiTemplate;
    private String authenticationMethod;
    private String authenticationType;
    private String authenticationValueEn;
    private String challengeCancel;
    private String eci;
    private String interactionCounter;
    private String messageCategory;
    private String messageVersion;
    private String transStatus;
    private String transStatusReason;
    private String resultsStatus;
    private String whiteListStatus;
    private String whiteListStatusSource;
    private String rresMessageVersion;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public static ResultLogDO valueOf(ocean.acs.models.oracle.entity.ResultLog e) {
        return new ResultLogDO(e.getId(), e.getArtAcsInterface(), e.getArtAcsUiTemplate(),
                e.getAuthenticationMethod(), e.getAuthenticationType(), e.getAuthenticationValueEn(),
                e.getChallengeCancel(), e.getEci(), e.getInteractionCounter(),
                e.getMessageCategory(), e.getMessageVersion(), e.getTransStatus(),
                e.getTransStatusReason(), e.getResultsStatus(), e.getWhiteListStatus(),
                e.getWhiteListStatusSource(), e.getRresMessageVersion(), e.getSysCreator(),
                e.getCreateMillis(), e.getSysUpdater(), e.getUpdateMillis());
    }
    
    public static ResultLogDO valueOf(ocean.acs.models.sql_server.entity.ResultLog e) {
        return new ResultLogDO(e.getId(), e.getArtAcsInterface(), e.getArtAcsUiTemplate(),
                e.getAuthenticationMethod(), e.getAuthenticationType(), e.getAuthenticationValueEn(),
                e.getChallengeCancel(), e.getEci(), e.getInteractionCounter(),
                e.getMessageCategory(), e.getMessageVersion(), e.getTransStatus(),
                e.getTransStatusReason(), e.getResultsStatus(), e.getWhiteListStatus(),
                e.getWhiteListStatusSource(), e.getRresMessageVersion(), e.getSysCreator(),
                e.getCreateMillis(), e.getSysUpdater(), e.getUpdateMillis());
    }

}
