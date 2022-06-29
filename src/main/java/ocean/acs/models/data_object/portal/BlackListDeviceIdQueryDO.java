package ocean.acs.models.data_object.portal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.enumerator.DeviceChannel;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BlackListDeviceIdQueryDO extends TimePageQueryDO {

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;
    private String deviceChannels;

    public static void valid(BlackListDeviceIdQueryDO dto) {
        if (null != dto.getDeviceChannels() && !dto.getDeviceChannels().isEmpty()) {
            if (!DeviceChannel.contains(dto.getDeviceChannels())) {
                throw new OceanExceptionForPortal(ResultStatus.ILLEGAL_ARGUMENT,
                        "illegal argument 'deviceChannels'");
            }
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
