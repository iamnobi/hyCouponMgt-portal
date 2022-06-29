package ocean.acs.models.oracle.entity;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.MimaPolicyDO;
import ocean.acs.models.entity.DBKey;

/**
 * Mima Policy Entity
 *
 * @author Alan Chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_MIMA_POLICY, indexes = {
  @Index(name = "IDX_MIMA_POLICY_1", columnList = "ISSUER_BANK_ID")
})
public class MimaPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SQ_MIMA_POLICY", sequenceName = "MIMA_POLICY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_MIMA_POLICY")
    @Column(name = DBKey.COL_MIMA_POLICY_ID)
    private Long id;

    @Column(name = DBKey.COL_MIMA_POLICY_ISSUER_BANK_ID)
    private Long issuerBankId;
    /**
     * 登入重試次數
     */
    @Column(name = DBKey.COL_MIMA_POLICY_LOGIN_RETRY_COUNT)
    private Integer loginRetryCount;
    /**
     * 更新週期天數
     */
    @Column(name = DBKey.COL_MIMA_POLICY_MIMA_FRESH_INTERVAL)
    private Integer mimaFreshInterval;
    /**
     * 最長未使用期間天數
     */
    @Column(name = DBKey.COL_MIMA_POLICY_ACCOUNT_MAX_IDLE_DAY)
    private Integer accountMaxIdleDay;
    /**
     * 最長長度
     */
    @Column(name = DBKey.COL_MIMA_POLICY_MIMA_MAX_LENGTH)
    private Integer mimaMaxLength;
    /**
     * 最短長度
     */
    @Column(name = DBKey.COL_MIMA_POLICY_MIMA_MIN_LENGTH)
    private Integer mimaMinLength;
    /**
     * 最少英文字元數量
     */
    @Column(name = DBKey.COL_MIMA_POLICY_MIMA_ALPHABET_COUNT)
    private Integer mimaAlphabetCount;
    /**
     * 最少小寫英文數量
     */
    @Column(name = DBKey.COL_MIMA_POLICY_MIMA_MIN_LOWER_CASE)
    private Integer mimaMinLowerCase;
    /**
     * 最少大寫英文數量
     */
    @Column(name = DBKey.COL_MIMA_POLICY_MIMA_MIN_UPPER_CASE)
    private Integer mimaMinUpperCase;
    /**
     * 最少數字數量
     */
    @Column(name = DBKey.COL_MIMA_POLICY_MIMA_MIN_NUMERIC)
    private Integer mimaMinNumeric;
    /**
     * 最少特殊字元數量
     */
    @Column(name = DBKey.COL_MIMA_POLICY_MIMA_MIN_SPECIAL_CHAR)
    private Integer mimaMinSpecialChar;
    /**
     * 不可重複代數
     */
    @Column(name = DBKey.COL_MIMA_POLICY_MIMA_HISTORY_DUP_COUNT)
    private Integer mimaHistoryDupCount;

    @Column(name = DBKey.COL_MIMA_POLICY_CREATE_TIME)
    private long createTime;

    @Column(name = DBKey.COL_MIMA_POLICY_UPDATE_TIME)
    private long updateTime;

    public static MimaPolicy createDefault(long issuerBankId) {
        MimaPolicy policy = new MimaPolicy();
        policy.setIssuerBankId(issuerBankId);
        policy.setLoginRetryCount(10);
        policy.setMimaFreshInterval(30);
        policy.setAccountMaxIdleDay(60);
        policy.setMimaMaxLength(16);
        policy.setMimaMinLength(8);
        policy.setMimaAlphabetCount(3);
        policy.setMimaMinLowerCase(1);
        policy.setMimaMinUpperCase(1);
        policy.setMimaMinNumeric(1);
        policy.setMimaMinSpecialChar(1);
        policy.setMimaHistoryDupCount(4);
        long now = System.currentTimeMillis();
        policy.setCreateTime(now);
        policy.setUpdateTime(now);
        return policy;
    }

    /**
     * Dto to Entity
     */
    public static MimaPolicy valueOf(MimaPolicyDO dto) {
        MimaPolicy mimaPolicy = new MimaPolicy();
        mimaPolicy.setId(dto.getId());
        mimaPolicy.setIssuerBankId(dto.getIssuerBankId());
        mimaPolicy.setLoginRetryCount(dto.getLoginRetryCount());
        mimaPolicy.setMimaFreshInterval(dto.getMimaFreshInterval());
        mimaPolicy.setAccountMaxIdleDay(dto.getAccountMaxIdleDay());
        mimaPolicy.setMimaMinLength(dto.getMimaMinLength());
        mimaPolicy.setMimaMaxLength(dto.getMimaMaxLength());
        mimaPolicy.setMimaAlphabetCount(dto.getMimaAlphabetCount());
        mimaPolicy.setMimaMinUpperCase(dto.getMimaMinUpperCase());
        mimaPolicy.setMimaMinLowerCase(dto.getMimaMinLowerCase());
        mimaPolicy.setMimaMinNumeric(dto.getMimaMinNumeric());
        mimaPolicy.setMimaMinSpecialChar(dto.getMimaMinSpecialChar());
        mimaPolicy.setMimaHistoryDupCount(dto.getMimaHistoryDupCount());
        long now = System.currentTimeMillis();
        if (mimaPolicy.getId() == null) {
            mimaPolicy.setCreateTime(now);
        }
        mimaPolicy.setUpdateTime(now);
        return mimaPolicy;
    }
}
