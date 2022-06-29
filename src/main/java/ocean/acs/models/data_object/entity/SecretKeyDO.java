package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class SecretKeyDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private String cardBrand;
    private String keyA;
    private String KeyB;
    private String auditStatus;

    public SecretKeyDO(
            Long id,
            Long issuerBankId,
            String cardBrand,
            String keyA,
            String keyB,
            String auditStatus,
            String creator,
            Long createMillis,
            String updater,
            Long updateMillis,
            Boolean deleteFlag,
            String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.cardBrand = cardBrand;
        this.keyA = keyA;
        this.KeyB = keyB;
        this.auditStatus = auditStatus;
    }

    public static SecretKeyDO valueOf(ocean.acs.models.oracle.entity.SecretKey e) {
        return new SecretKeyDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getCardBrand(),
                e.getKeyA(),
                e.getKeyB(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static SecretKeyDO valueOf(ocean.acs.models.sql_server.entity.SecretKey e) {
        return new SecretKeyDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getCardBrand(),
                e.getKeyA(),
                e.getKeyB(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }

    public static SecretKeyDO newInstance(
            Long id, Long issuerBankId, String cardBrand, String keyA, String keyB) {
        SecretKeyDO entity =
                SecretKeyDO.builder()
                        .id(id)
                        .issuerBankId(issuerBankId)
                        .cardBrand(cardBrand)
                        .keyA(keyA)
                        .KeyB(keyB)
                        .build();
        return entity;
    }
}
