package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.WhiteListAttemptAuthorizeDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_WHITE_LIST_ATTEMPT_AUTHORIZE)
public class WhiteListAttemptAuthorize extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "white_list_attempt_authorize_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "W_L_A_A_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "white_list_attempt_authorize_seq_generator")
    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_AUTHORIZE_ID)
    private String id;

    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_AUTHORIZE_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_AUTHORIZE_LDAP_GROUP)
    private String ldapGroup;

    @NonNull
    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_AUTHORIZE_MAX_AMOUNT)
    private Long maxAmount;

    public WhiteListAttemptAuthorize(String id, Long issuerBankId, String ldapGroup, Long maxAmount,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.ldapGroup = ldapGroup;
        this.maxAmount = maxAmount;
    }

    public static WhiteListAttemptAuthorize valueOf(WhiteListAttemptAuthorizeDO d) {
        return new WhiteListAttemptAuthorize(d.getId(), d.getIssuerBankId(), d.getLdapGroup(),
                d.getMaxAmount(), d.getCreator(), d.getCreateMillis(), d.getUpdater(),
                d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

}
