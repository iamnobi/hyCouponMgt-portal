package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class UserOperationReqDTO {

    /**
     * User Id
     */
    private Long id;
    /**
     * 銀行代碼
     */
    private Long issuerBankId;
    /**
     * 帳號
     */
    private String account;
    /**
     * 名稱
     */
    private String name;
    /**
     * 部門
     */
    private String department;
    /**
     * 手機
     */
    private String phone;
    /**
     * 分機
     */
    private String ext;
    /**
     * Email
     */
    private String email;
    private List<Long> groupIdList;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
