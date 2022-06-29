package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * MimaRecordDO
 *
 * @author Alan Chen
 */
@Data
@Builder
@AllArgsConstructor
public class MimaRecordDO {

    private Long id;
    private Long issuerBankId;
    private String account;
    private String encryptedMima;
    private Long createTime;

    public static MimaRecordDO valueOf(ocean.acs.models.oracle.entity.MimaRecord e) {
        return MimaRecordDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .account(e.getAccount())
          .encryptedMima(e.getMima())
          .createTime(e.getCreateTime())
          .build();
    }
    
    public static MimaRecordDO valueOf(ocean.acs.models.sql_server.entity.MimaRecord e) {
        return MimaRecordDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .account(e.getAccount())
          .encryptedMima(e.getMima())
          .createTime(e.getCreateTime())
          .build();
    }

}
