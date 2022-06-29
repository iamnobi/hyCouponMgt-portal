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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
