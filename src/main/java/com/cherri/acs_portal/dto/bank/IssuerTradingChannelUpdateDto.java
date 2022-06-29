package com.cherri.acs_portal.dto.bank;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.IssuerTradingChannelDO;
import ocean.acs.models.oracle.entity.IssuerTradingChannel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IssuerTradingChannelUpdateDto extends AuditableDTO {

    private Long issuerBankId;

    private List<TradingChannel> tradingChannelList = new ArrayList<>();

    private String user;
    private AuditStatus auditStatus;

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BANK_CHANNEL;
    }

    public static IssuerTradingChannelUpdateDto valueOf(IssuerTradingChannel entity,
      List<String> cardBrandList) {
        IssuerTradingChannelUpdateDto dto = new IssuerTradingChannelUpdateDto();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());

        List<String> enabledChannelList = null;
        if (StringUtils.isNotEmpty(entity.getEnabledChannelList())) {
            String[] channelArray = entity.getEnabledChannelList().split(",");
            enabledChannelList = Arrays.asList(channelArray);
        }

        List<String> finalEnabledChannelList = enabledChannelList;
        List<TradingChannel> channelList =
          cardBrandList.stream()
            .map(
              cardBrand -> {
                  if (finalEnabledChannelList == null) {
                      return new TradingChannel(cardBrand, false);
                  } else {
                      return new TradingChannel(
                        cardBrand, finalEnabledChannelList.contains(cardBrand));
                  }
              })
            .collect(Collectors.toList());
        dto.setTradingChannelList(channelList);

        dto.setUser(entity.getUpdater());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }

    public static IssuerTradingChannelUpdateDto valueOf(IssuerTradingChannelDO entity,
      List<String> cardBrandList) {
        IssuerTradingChannelUpdateDto dto = new IssuerTradingChannelUpdateDto();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());

        List<String> enabledChannelList = null;
        if (CollectionUtils.isNotEmpty(entity.getEnabledChannelList())) {
            enabledChannelList = entity.getEnabledChannelList();
        }

        List<String> finalEnabledChannelList = enabledChannelList;
        List<TradingChannel> channelList =
          cardBrandList.stream()
            .map(
              cardBrand -> {
                  if (finalEnabledChannelList == null) {
                      return new TradingChannel(cardBrand, false);
                  } else {
                      return new TradingChannel(
                        cardBrand, finalEnabledChannelList.contains(cardBrand));
                  }
              })
            .collect(Collectors.toList());
        dto.setTradingChannelList(channelList);

        dto.setUser(entity.getUpdater());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
