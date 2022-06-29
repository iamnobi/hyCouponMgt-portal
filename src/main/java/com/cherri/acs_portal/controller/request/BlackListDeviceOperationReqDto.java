package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.dto.audit.AuditableDTO;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BlackListDeviceOperationReqDto extends AuditableDTO {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;
    @NotNull(message = "{column.notempty}")
    private List<Long> ids;

    private String user;
    private AuditStatus auditStatus;

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_DEVICE;
    }

}
