package ocean.acs.models.data_object.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class IssuerTradingChannelDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private List<String> enabledChannelList;
    private String auditStatus;

    public IssuerTradingChannelDO(
            Long id,
            Long issuerBankId,
            List<String> enabledChannelList,
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

    public static IssuerTradingChannelDO valueOf(
            ocean.acs.models.oracle.entity.IssuerTradingChannel e) {
        List<String> enabledChannelList = new ArrayList<>();
        if (StringUtils.isNotEmpty(e.getEnabledChannelList())) {
            String[] channelArray = e.getEnabledChannelList().split(",");
            enabledChannelList = Arrays.asList(channelArray);
        }

        return new IssuerTradingChannelDO(
                e.getId(),
                e.getIssuerBankId(),
                enabledChannelList,
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static IssuerTradingChannelDO valueOf(
            ocean.acs.models.sql_server.entity.IssuerTradingChannel e) {
        List<String> enabledChannelList = new ArrayList<>();
        if (StringUtils.isNotEmpty(e.getEnabledChannelList())) {
            String[] channelArray = e.getEnabledChannelList().split(",");
            enabledChannelList = Arrays.asList(channelArray);
        }

        return new IssuerTradingChannelDO(
                e.getId(),
                e.getIssuerBankId(),
                enabledChannelList,
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
}
