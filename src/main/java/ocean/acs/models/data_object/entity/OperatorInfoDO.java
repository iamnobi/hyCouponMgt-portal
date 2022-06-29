package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.models.entity.OperatorInfo;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OperatorInfoDO {

    private String creator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String updater;
    private Long updateMillis;
    @Builder.Default
    private Boolean deleteFlag = false;
    private String deleter;
    private Long deleteMillis;

    public void setOperatorInfo(OperatorInfo o) {
        creator = o.getCreator();
        createMillis = o.getCreateMillis();
        updater = o.getUpdater();
        updateMillis = o.getUpdateMillis();
        deleteFlag = o.getDeleteFlag();
        deleter = o.getDeleter();
        deleteMillis = o.getDeleteMillis();
    }
}
