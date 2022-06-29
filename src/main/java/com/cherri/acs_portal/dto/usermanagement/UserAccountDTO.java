package com.cherri.acs_portal.dto.usermanagement;

import com.cherri.acs_portal.controller.request.UnlockUserOperationReqDTO;
import com.cherri.acs_portal.controller.request.UserOperationReqDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.UserGroupDO;
import ocean.acs.models.data_object.portal.UserAccountDO;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAccountDTO extends AuditableDTO {

    /**
     * 銀行代碼
     */
    private Long issuerBankId;
    /**
     * 使用者ID
     */
    private Long id;
    /**
     * 帳號
     */
    private String account;
    /**
     * 使用者所屬群組(多個群組使用逗號分隔
     */
    private String group;
    /**
     * 使用者所屬群組
     */
    private List<UserGroupDO> userGroupDOList;
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

    /**
     * 重試次數
     */
    private Integer tryFailCount;

    private AuditStatus auditStatus;
    /**
     * 建立者
     */
    private String creator;
    /**
     * 更新者
     */
    private String updater;

    private String accountLockReason;

    public UserAccountDTO(UserOperationReqDTO reqDto) {
        this.id = reqDto.getId();
        this.issuerBankId = reqDto.getIssuerBankId();
        this.account = reqDto.getAccount();
        this.department = reqDto.getDepartment();
        this.name = reqDto.getName();
        this.email = reqDto.getEmail();
        this.phone = reqDto.getPhone();
        this.ext = reqDto.getExt();
    }

    public UserAccountDTO(UnlockUserOperationReqDTO reqDto) {
        this.id = reqDto.getId();
        this.issuerBankId = reqDto.getIssuerBankId();
    }

    public static UserAccountDTO valueOf(UserAccountDO entity) {
        UserAccountDTO dto = new UserAccountDTO();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setAccount(entity.getAccount());
        dto.setDepartment(entity.getDepartment());
        dto.setName(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setExt(entity.getExt());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setCreator(entity.getCreator());
        dto.setUpdater(entity.getUpdater());
        return dto;
    }
    public UserAccountDO toUserAccountDTO() {
        UserAccountDO entity = new UserAccountDO();
        entity.setId(getId());
        entity.setIssuerBankId(getIssuerBankId());
        entity.setAccount(getAccount());
        entity.setDepartment(getDepartment());
        entity.setUsername(getName());
        entity.setEmail(getEmail());
        entity.setPhone(getPhone());
        entity.setExt(getExt());
        entity.setAuditStatus(getAuditStatus().getSymbol());
        entity.setCreator(getCreator());
        entity.setUpdater(getUpdater());
        return entity;
    }

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.USER_GROUP;
    }
}
