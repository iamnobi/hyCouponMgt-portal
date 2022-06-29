package ocean.acs.models.data_object.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@NoArgsConstructor
public class BlackListQueryResultDO {

    private String panId;
    private String id;

    /** 加入時間 */
    private Long createMillis = System.currentTimeMillis();
    /** 卡組織 */
    private String cardBrand;
    /** 卡號 */
    private String cardNumber;
    /** 驗證狀態 */
    private String authStatus;
    /** 阻檔次數 */
    private Integer blockTimes = 0;

    @JsonIgnore
    private Long blackListPanBatchId;

    /** 供還原卡號用，還原後將原始卡號放入cardNumber變數 */
    @JsonIgnore
    private String enCardNumber;

    @JsonIgnore
    private String cardNumberHash;

    private AuditStatus auditStatus;

}
