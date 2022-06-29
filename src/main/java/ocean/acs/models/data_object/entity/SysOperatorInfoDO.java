package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SysOperatorInfoDO {

    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;
}
