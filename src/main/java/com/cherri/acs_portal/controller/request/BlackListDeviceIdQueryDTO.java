package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.TimePageQueryDTO;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.DeviceChannel;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BlackListDeviceIdQueryDTO extends TimePageQueryDTO {

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;
    private String deviceChannels;

    public static void valid(BlackListDeviceIdQueryDTO dto) {
        if (null != dto.getDeviceChannels() && !dto.getDeviceChannels().isEmpty()) {
            if (!DeviceChannel.contains(dto.getDeviceChannels())) {
                throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
                  "illegal argument 'deviceChannels'");
            }
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

}
