package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.kernel.TransactionAmountDO;
import ocean.acs.models.entity.DBKey;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_TRANSACTION_AMOUNT)
public class TransactionAmount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(
            name = "transaction_amount_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "sequence_name",
                        value = "TRANSACTION_AMOUNT_ID_SEQ"),
                @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            })
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_amount_seq_generator")
    @Column(name = DBKey.COL_TRANSACTION_AMOUNT_ID)
    private Long id;

    @Column(name = DBKey.COL_TRANSACTION_AMOUNT_PAN_INFO_ID)
    private Long panInfoID;

    @Column(name = DBKey.COL_TRANSACTION_AMOUNT_PURCHASE_AMOUNT)
    private String purchaseAmount;

    @Column(name = DBKey.COL_TRANSACTION_AMOUNT_PURCHASE_CURRENCY)
    private String purchaseCurrency;

    @Column(name = DBKey.COL_TRANSACTION_AMOUNT_PURCHASE_EXPONENT)
    private String purchaseExponent;

    @NonNull
    @Column(name = DBKey.COL_TRANSACTION_AMOUNT_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_TRANSACTION_AMOUNT_CREATE_MILLIS)
    private Long createMillis;

    public static TransactionAmount valueOf(TransactionAmountDO transactionAmountDO) {
        return TransactionAmount.builder()
                .id(transactionAmountDO.getId())
                .panInfoID(transactionAmountDO.getPanInfoID())
                .purchaseAmount(transactionAmountDO.getPurchaseAmount())
                .purchaseCurrency(transactionAmountDO.getPurchaseCurrency())
                .purchaseExponent(transactionAmountDO.getPurchaseExponent())
                .sysCreator(transactionAmountDO.getSysCreator())
                .createMillis(transactionAmountDO.getCreateMillis())
                .build();
    }
}
