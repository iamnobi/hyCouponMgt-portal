package com.cherri.acs_portal.dto.bank;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.portal.UserAccountDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class IssuerBankAdminDto extends AuditableDTO {

    /** 銀行代碼 issuer_bank.Id */
    @NotNull(message = "{column.notempty}")
    private Long bankId;

    /** 使用者帳號 */
    private String account;

    /** 使用者姓名 */
    @NotBlank(message = "{column.notempty}")
    private String userName;

    /** 部門類別 */
    private String department;

    /** 電子郵件 */
    private String email;
    /** 電話 */
    private String phone;
    /** 分機 */
    private String ext;

    // for AuditableDTO
    /** USER_ACCOUNT.ID */
    private AuditStatus auditStatus;

    private String user;

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BANK_ADMIN;
    }

    public static IssuerBankAdminDto valueOf(UserAccountDO entity) {
        IssuerBankAdminDto dto = new IssuerBankAdminDto();
        dto.id = entity.getId();

        // bankId
        dto.issuerBankId = entity.getIssuerBankId();
        dto.account = entity.getAccount();
        dto.userName = entity.getUsername();
        dto.department = entity.getDepartment();
        dto.email = entity.getEmail();
        dto.phone = entity.getPhone();
        dto.ext = entity.getExt();
        dto.auditStatus = AuditStatus.getStatusBySymbol(entity.getAuditStatus());
        return dto;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
