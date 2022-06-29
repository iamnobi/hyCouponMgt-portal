package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class KeyStoreDO {

    private String id;
    private String keyName;
    private String keyBody;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static KeyStoreDO valueOf(ocean.acs.models.oracle.entity.KeyStore e) {
        return new KeyStoreDO(e.getId(), e.getKeyName(), e.getKeyBody(), e.getSysCreator(),
                e.getCreateMillis());
    }
    
    public static KeyStoreDO valueOf(ocean.acs.models.sql_server.entity.KeyStore e) {
        return new KeyStoreDO(e.getId(), e.getKeyName(), e.getKeyBody(), e.getSysCreator(),
                e.getCreateMillis());
    }

}
