package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.TimerType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionTimerDO {

    private Long id;
    private TimerType timerType;
    private Long transactionLogID;
    private Long expireMillis;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static TransactionTimerDO valueOf(ocean.acs.models.oracle.entity.TransactionTimer e) {
        return new TransactionTimerDO(e.getId(), TimerType.codeOf(e.getTimerType()), e.getTransactionLogID(),
                e.getExpireMillis(), e.getSysCreator(), e.getCreateMillis());
    }
    
    public static TransactionTimerDO valueOf(ocean.acs.models.sql_server.entity.TransactionTimer e) {
        return new TransactionTimerDO(e.getId(), TimerType.codeOf(e.getTimerType()), e.getTransactionLogID(),
                e.getExpireMillis(), e.getSysCreator(), e.getCreateMillis());
    }

}
