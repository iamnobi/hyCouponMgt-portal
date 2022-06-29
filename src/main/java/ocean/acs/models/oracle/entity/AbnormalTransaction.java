package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.AbnormalTransactionDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_ABNORMAL_TRANSACTION)
public class AbnormalTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "abnormal_transaction_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "ABNORMAL_TRANSACTION_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "abnormal_transaction_seq_generator")
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_YEAR)
    private Integer year;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_MONTH)
    private Integer month;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_DAY_OF_MONTH)
    private Integer dayOfMonth;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_MERCHANT_ID)
    private String merchantId;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_MERCHANT_NAME)
    private String merchantName;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_U_RATE)
    private Double uRate;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_N_RATE)
    private Double nRate;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_U_COUNT)
    private Long uCount;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_N_COUNT)
    private Long nCount;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_TOTAL_COUNT)
    private Long totalCount;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_ABNORMAL_TRANSACTION_UPDATE_MILLIS)
    private Long updateMillis;

    public static AbnormalTransaction valueOf(AbnormalTransactionDO d) {
        return new AbnormalTransaction(d.getId(), d.getIssuerBankId(), d.getYear(), d.getMonth(),
                d.getDayOfMonth(), d.getMerchantId(), d.getMerchantName(), d.getURate(),
                d.getNRate(), d.getUCount(), d.getNCount(), d.getTotalCount(), d.getSysCreator(), d.getCreateMillis(), d.getSysUpdater(),
                d.getUpdateMillis());
    }

}
