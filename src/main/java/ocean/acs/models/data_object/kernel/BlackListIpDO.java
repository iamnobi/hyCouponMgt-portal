package ocean.acs.models.data_object.kernel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class BlackListIpDO {

    private final Long blackListIpGroupID;
    private final String ip;
    private final Integer cidr;
    private final String transStatus;
}
