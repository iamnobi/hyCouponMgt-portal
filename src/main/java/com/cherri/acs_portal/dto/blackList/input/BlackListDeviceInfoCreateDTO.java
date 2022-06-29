package com.cherri.acs_portal.dto.blackList.input;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.blackList.output.BlackListDeviceInfoDTO;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.TransStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class BlackListDeviceInfoCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<BlackListDeviceInfoDTO> deviceInfoList;
    private TransStatus transStatus;
    private String creator;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
