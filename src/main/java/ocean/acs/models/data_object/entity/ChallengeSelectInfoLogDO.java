package ocean.acs.models.data_object.entity;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeSelectInfoLogDO {

    private Long id;
    private Long challengeLogId;
    private String key;
    private String value;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static ChallengeSelectInfoLogDO valueOf(
            ocean.acs.models.oracle.entity.ChallengeSelectInfoLog e) {
        return new ChallengeSelectInfoLogDO(e.getId(), e.getChallengeLogId(), e.getKey(),
                e.getValue(), e.getSysCreator(), e.getCreateMillis());
    }
    
    public static ChallengeSelectInfoLogDO valueOf(
            ocean.acs.models.sql_server.entity.ChallengeSelectInfoLog e) {
        return new ChallengeSelectInfoLogDO(e.getId(), e.getChallengeLogId(), e.getKey(),
                e.getValue(), e.getSysCreator(), e.getCreateMillis());
    }

    public static ChallengeSelectInfoLogDO newInstance(Map<String, String> challengeSelectInfoMap) {
        ChallengeSelectInfoLogDO challengeSelectInfoLogDO = new ChallengeSelectInfoLogDO();
        for (Map.Entry<String, String> challengeSelectInfo : challengeSelectInfoMap.entrySet()) {
            challengeSelectInfoLogDO.setKey(challengeSelectInfo.getKey());
            challengeSelectInfoLogDO.setValue(challengeSelectInfo.getValue());
        }
        challengeSelectInfoLogDO.setSysCreator(MessageType.CRes.name());
        challengeSelectInfoLogDO.setCreateMillis(System.currentTimeMillis());
        return challengeSelectInfoLogDO;

    }
}
