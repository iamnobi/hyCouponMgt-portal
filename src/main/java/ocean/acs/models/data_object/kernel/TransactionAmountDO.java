package ocean.acs.models.data_object.kernel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionAmountDO {

    private Long id;
    private Long panInfoID;
    private String purchaseAmount;
    private String purchaseCurrency;
    private String purchaseExponent;
    private String sysCreator;
    @Builder.Default private Long createMillis = System.currentTimeMillis();

    public static TransactionAmountDO valueOf(ocean.acs.models.oracle.entity.TransactionAmount e) {
        return TransactionAmountDO.builder()
            .id(e.getId())
            .panInfoID(e.getPanInfoID())
            .purchaseAmount(e.getPurchaseAmount())
            .purchaseCurrency(e.getPurchaseCurrency())
            .purchaseExponent(e.getPurchaseExponent())
            .sysCreator(e.getSysCreator())
            .createMillis(e.getCreateMillis())
            .build();
    }

    public static TransactionAmountDO valueOf(ocean.acs.models.sql_server.entity.TransactionAmount e) {
        return TransactionAmountDO.builder()
            .id(e.getId())
            .panInfoID(e.getPanInfoID())
            .purchaseAmount(e.getPurchaseAmount())
            .purchaseCurrency(e.getPurchaseCurrency())
            .purchaseExponent(e.getPurchaseExponent())
            .sysCreator(e.getSysCreator())
            .createMillis(e.getCreateMillis())
            .build();
    }
}
