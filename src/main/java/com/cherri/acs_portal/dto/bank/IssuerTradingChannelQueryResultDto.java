package com.cherri.acs_portal.dto.bank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.IssuerTradingChannelDO;
import ocean.acs.models.oracle.entity.IssuerTradingChannel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@ToString
public class IssuerTradingChannelQueryResultDto {

    private List<TradingChannel> tradingChannelList = new ArrayList<>();
    private AuditStatus auditStatus;

    public static IssuerTradingChannelQueryResultDto valueOf(
      IssuerTradingChannel issuerTradingChannel, List<String> cardBrandList) {
        IssuerTradingChannelQueryResultDto dto = new IssuerTradingChannelQueryResultDto();

        List<String> enabledChannelList = null;
        if (StringUtils.isNotEmpty(issuerTradingChannel.getEnabledChannelList())) {
            String[] channelArray = issuerTradingChannel.getEnabledChannelList().split(",");
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
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(issuerTradingChannel.getAuditStatus()));
        return dto;
    }

    public static IssuerTradingChannelQueryResultDto valueOf(
      IssuerTradingChannelDO issuerTradingChannel, List<String> cardBrandList) {
        IssuerTradingChannelQueryResultDto dto = new IssuerTradingChannelQueryResultDto();

        List<String> enabledChannelList = null;
        if (CollectionUtils.isNotEmpty(issuerTradingChannel.getEnabledChannelList())) {
            enabledChannelList = issuerTradingChannel.getEnabledChannelList();
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
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(issuerTradingChannel.getAuditStatus()));
        return dto;
    }

}
