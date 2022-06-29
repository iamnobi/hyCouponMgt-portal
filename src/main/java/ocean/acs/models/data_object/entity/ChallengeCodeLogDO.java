package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChallengeCodeLogDO {

    private Long id;
    private Long challengeLogID;
    private String verifyCode;
    private Integer verifyStatus;
    private String authID;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static ChallengeCodeLogDO valueOf(ocean.acs.models.oracle.entity.ChallengeCodeLog e) {
        return new ChallengeCodeLogDO(e.getId(), e.getChallengeLogID(), e.getVerifyCode(),
                e.getVerifyStatus(), e.getAuthID(), e.getSysCreator(), e.getCreateMillis());
    }
    
    public static ChallengeCodeLogDO valueOf(ocean.acs.models.sql_server.entity.ChallengeCodeLog e) {
        return new ChallengeCodeLogDO(e.getId(), e.getChallengeLogID(), e.getVerifyCode(),
                e.getVerifyStatus(), e.getAuthID(), e.getSysCreator(), e.getCreateMillis());
    }

}
