package ocean.acs.models.data_object.kernel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class KernelBlackListMerchantUrlDO {

    private String url;
    private String transStatus;
}
