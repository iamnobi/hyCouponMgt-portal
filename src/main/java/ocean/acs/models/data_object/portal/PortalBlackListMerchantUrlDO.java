package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PortalBlackListMerchantUrlDO extends AuditableDO {

    private static final long serialVersionUID = 1L;

    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String merchantUrl;
    private AuditStatus auditStatus;

    private String creator;
    private String updater;

    public PortalBlackListMerchantUrlDO(BlackListMerchantUrlOperationReqDO d, String user) {
        this.id = d.getId();
        this.issuerBankId = d.getIssuerBankId();
        this.createMillis = System.currentTimeMillis();
        this.merchantUrl = d.getMerchantUrl();
        this.creator = user;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_MERCHANT_URL;
    }

}
