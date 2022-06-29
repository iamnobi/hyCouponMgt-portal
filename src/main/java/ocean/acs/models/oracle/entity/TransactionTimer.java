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
import ocean.acs.models.data_object.entity.TransactionTimerDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_TRANSACTION_TIMER)
public class TransactionTimer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "transaction_timer_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "TRANSACTION_TIMER_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "transaction_timer_seq_generator")
    @Column(name = DBKey.COL_TRANSACTION_TIMER_ID)
    private Long id;

    @Column(name = DBKey.COL_TRANSACTION_TIMER_TIMER_TYPE)
    private Integer timerType;

    @Column(name = DBKey.COL_TRANSACTION_TIMER_KERNEL_TRANSACTION_LOG_ID)
    private Long transactionLogID;

    @Column(name = DBKey.COL_TRANSACTION_TIMER_EXPIRE_MILLIS)
    private Long expireMillis;

    @NonNull
    @Column(name = DBKey.COL_TRANSACTION_TIMER_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_TRANSACTION_TIMER_CREATE_MILLIS)
    private Long createMillis = System.currentTimeMillis();

    public static TransactionTimer valueOf(TransactionTimerDO d) {
        return new TransactionTimer(d.getId(), d.getTimerType().getCode(), d.getTransactionLogID(),
                d.getExpireMillis(), d.getSysCreator(), d.getCreateMillis());
    }

}
