package ocean.acs.models.data_object.kernel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WhiteListAttemptPanAuthStatusDO {

    private Long id;
    private Integer triesPermitted;
    private Integer triesQuota;
    private Long attemptExpireTime;
    private String currency;
    private Double amount;

    public void minusTriesQuota(int minusValue) {
        if (null == triesQuota) {
            this.triesQuota = 0;
        } else {
            this.triesQuota = this.triesQuota - minusValue;
        }
    }
}
