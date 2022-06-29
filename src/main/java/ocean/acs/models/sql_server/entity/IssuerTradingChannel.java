package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.IssuerTradingChannelDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_ISSUER_TRADING_CHANNEL)
public class IssuerTradingChannel extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_ISSUER_TRADING_CHANNEL_ID)
    private Long id;

    @Column(name = DBKey.COL_ISSUER_TRADING_CHANNEL_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_ISSUER_TRADING_CHANNEL_ENABLED_CHANNEL_LIST)
    private String enabledChannelList;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public IssuerTradingChannel(
            Long id,
            Long issuerBankId,
            String enabledChannelList,
            String auditStatus,
            String creator,
            Long createMillis,
            String updater,
            Long updateMillis,
            Boolean deleteFlag,
            String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.enabledChannelList = enabledChannelList;
        this.auditStatus = auditStatus;
    }

    public static IssuerTradingChannel valueOf(IssuerTradingChannelDO d) {
        return new IssuerTradingChannel(
                d.getId(),
                d.getIssuerBankId(),
                String.join(",", d.getEnabledChannelList()),
                d.getAuditStatus(),
                d.getCreator(),
                d.getCreateMillis(),
                d.getUpdater(),
                d.getUpdateMillis(),
                d.getDeleteFlag(),
                d.getDeleter(),
                d.getDeleteMillis());
    }
}
