package ocean.acs.models.data_object.kernel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlackListPanAuthStatusDO {

    private Long id;
    private String transStatus;
    private Long blackListPanCreateMillis;
    private Long blackListPanBatchUpdateMillis;
}
