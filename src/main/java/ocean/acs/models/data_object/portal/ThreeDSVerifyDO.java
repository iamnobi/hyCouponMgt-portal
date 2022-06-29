package ocean.acs.models.data_object.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.CardType;
import ocean.acs.models.oracle.entity.PanInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@SuperBuilder
public class ThreeDSVerifyDO extends AuditableDO {

    @NotNull(message = "{column.notempty}")
    private Long panId;

    @NotNull(message = "{column.notempty}")
    private Boolean verifyEnabled;

    private Boolean otpLock;

    private CardType cardType;

    private String cardNumber;

    private AuditStatus auditStatus;

    /** API操作者 */
    private String user;

    /** DB的值 */
    private String creator;
    /** DB的值 */
    private String updater;

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.THREE_DS_VERIFY_ENABLED;
    }

    public ThreeDSVerifyDO(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }

    public static ThreeDSVerifyDO valueOf(PanInfo panInfo) {
        ThreeDSVerifyDO dto = new ThreeDSVerifyDO();
        dto.setId(panInfo.getId());
        dto.setPanId(panInfo.getId());
        dto.setCardNumber(panInfo.getCardNumber());
        dto.setVerifyEnabled(panInfo.getThreeDSVerifyEnable());
        dto.setIssuerBankId(panInfo.getIssuerBankId());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(panInfo.getAuditStatus()));
        dto.setCreator(panInfo.getCreator());
        dto.setUpdater(panInfo.getUpdater());
        return dto;
    }

}
