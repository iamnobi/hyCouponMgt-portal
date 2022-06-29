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
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CHALLENGE_CODE_LOG)
public class ChallengeCodeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_CHALLENGE_CODE_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_CHALLENGE_CODE_LOG_CHALLENGE_LOG_ID)
    private Long challengeLogID;

    @Column(name = DBKey.COL_CHALLENGE_CODE_LOG_VERIFY_CODE)
    private String verifyCode;

    @Column(name = DBKey.COL_CHALLENGE_CODE_LOG_VERIFY_STATUS)
    private Integer verifyStatus;

    @Column(name = DBKey.COL_CHALLENGE_CODE_LOG_AUTH_ID)
    private String authID;

    @NonNull
    @Column(name = DBKey.COL_CHALLENGE_CODE_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_CHALLENGE_CODE_LOG_CREATE_MILLIS)
    private Long createMillis = System.currentTimeMillis();

}
