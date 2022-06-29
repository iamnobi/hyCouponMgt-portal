package com.cherri.acs_portal.dto.blackList.output;

import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.blackList.input.BlackListAuthStatusUpdateDTO;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.BlackListAuthStatusCategory;
import ocean.acs.models.data_object.entity.BlackListAuthStatusDO;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
public class BlackListAuthStatusDTO extends AuditableDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long issuerBankId;
    private String category;
    private String authStatus;

    private AuditStatus auditStatus;
    private String user;

    public BlackListAuthStatusDTO(BlackListAuthStatusUpdateDTO reqDto, String user) {
        this.id = reqDto.getId();
        this.issuerBankId = reqDto.getIssuerBankId();
        this.authStatus = reqDto.getAuthStatus();
        this.user = user;
    }

    public static BlackListAuthStatusDTO valueOf(BlackListAuthStatusDO entity) {
        BlackListAuthStatusDTO dto = new BlackListAuthStatusDTO();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setAuthStatus(entity.getTransStatus());
        BlackListAuthStatusCategory category =
          BlackListAuthStatusCategory.getByCode(entity.getCategory());
        dto.setCategory(MessageConstants.get(category.getDesc()));
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_AUTH_STATUS;
    }
}
