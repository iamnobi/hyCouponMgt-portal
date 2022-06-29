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
import ocean.acs.models.data_object.entity.ChallengeSelectInfoLogDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CHALLENGE_SELECT_INFO_LOG)
public class ChallengeSelectInfoLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_CHALLENGE_SELECT_INFO_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_CHALLENGE_SELECT_INFO_LOG_CHALLENGE_LOG_ID)
    private Long challengeLogId;

    @Column(name = DBKey.COL_CHALLENGE_SELECT_INFO_LOG_KY)
    private String key;

    @Column(name = DBKey.COL_CHALLENGE_SELECT_INFO_LOG_VAL)
    private String value;

    @NonNull
    @Column(name = DBKey.COL_CHALLENGE_SELECT_INFO_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_CHALLENGE_SELECT_INFO_LOG_CREATE_MILLIS)
    private Long createMillis;

    public static ChallengeSelectInfoLog valueOf(ChallengeSelectInfoLogDO d) {
        return new ChallengeSelectInfoLog(d.getId(), d.getChallengeLogId(), d.getKey(),
                d.getValue(), d.getSysCreator(), d.getCreateMillis());
    }

}
