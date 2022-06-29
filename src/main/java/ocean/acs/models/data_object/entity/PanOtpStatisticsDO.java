package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PanOtpStatisticsDO extends OperatorInfoDO {

    private Long id;
    private Long panInfoId;
    private Integer verifyOtpCount;
    private String auditStatus;

    public PanOtpStatisticsDO(Long id, Long panInfoId, Integer verifyOtpCount, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.panInfoId = panInfoId;
        this.verifyOtpCount = verifyOtpCount;
        this.auditStatus = auditStatus;
    }

    public static PanOtpStatisticsDO valueOf(ocean.acs.models.oracle.entity.PanOtpStatistics e) {
        return new PanOtpStatisticsDO(e.getId(), e.getPanInfoId(), e.getVerifyOtpCount(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }
    
    public static PanOtpStatisticsDO valueOf(ocean.acs.models.sql_server.entity.PanOtpStatistics e) {
        return new PanOtpStatisticsDO(e.getId(), e.getPanInfoId(), e.getVerifyOtpCount(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

    public static PanOtpStatisticsDO newInstance(Long panInfoId, String creator) {
        return PanOtpStatisticsDO.builder().panInfoId(panInfoId)
                .auditStatus(AuditStatus.PUBLISHED.getSymbol())
                .verifyOtpCount(0)
                .creator(creator)
                .createMillis(System.currentTimeMillis()).build();
    }
}
