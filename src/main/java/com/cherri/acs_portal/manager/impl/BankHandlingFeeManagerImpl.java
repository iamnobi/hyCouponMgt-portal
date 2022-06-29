package com.cherri.acs_portal.manager.impl;

import com.cherri.acs_portal.dto.bank.CardBrandTypeDto;
import com.cherri.acs_portal.dto.bank.IssuerBinRangeQueryDto;
import com.cherri.acs_portal.dto.bank.IssuerTotalCardsQueryDto;
import com.cherri.acs_portal.dto.bank.IssuerTotalCardsQueryResultDto;
import com.cherri.acs_portal.dto.system.CardBrandDTO;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.manager.BankHandlingFeeManager;
import com.cherri.acs_portal.service.CardBrandConfigurationHelper;
import com.cherri.acs_portal.service.bo.BankHandlingFeeRawDataBO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.activation.UnsupportedDataTypeException;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.CardType;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.BinRangeDAO;
import ocean.acs.models.dao.IssuerHandingFeeDAO;
import ocean.acs.models.data_object.entity.BinRangeDO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.entity.IssuerHandingFeeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BankHandlingFeeManagerImpl implements BankHandlingFeeManager {

    private final AcsIntegratorManager acsIntegratorManager;
    private final IssuerHandingFeeDAO issuerHandingFeeDao;
    private final BinRangeDAO binRangeDAO;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;

    @Autowired
    public BankHandlingFeeManagerImpl(
      AcsIntegratorManager acsIntegratorManager,
      IssuerHandingFeeDAO issuerHandingFeeDao,
      BinRangeDAO binRangeDAO,
      CardBrandConfigurationHelper cardBrandConfigurationHelper) {
        this.acsIntegratorManager = acsIntegratorManager;
        this.issuerHandingFeeDao = issuerHandingFeeDao;
        this.binRangeDAO = binRangeDAO;
        this.cardBrandConfigurationHelper = cardBrandConfigurationHelper;
    }

    @Override
    public List<CardBrandTypeDto> getCardBrandTypeList() {
        List<CardBrandTypeDto> cardBrandTypeList = new ArrayList<>();

        List<CardBrandDTO> cardBrandList = cardBrandConfigurationHelper.findCardBrandList();

        for (CardBrandDTO cardBrand : cardBrandList) {
            cardBrandTypeList.add(new CardBrandTypeDto(cardBrand, CardType.CREDIT));
            cardBrandTypeList.add(new CardBrandTypeDto(cardBrand, CardType.DEBIT));
            cardBrandTypeList.add(new CardBrandTypeDto(cardBrand, CardType.HOST));
        }
        return cardBrandTypeList;
    }

    @Override
    public Map<Long, IssuerHandingFeeDO> getIssuerHandingFeeMap(List<IssuerBankDO> issuerBankList) {
        Map<Long, IssuerHandingFeeDO> issuerHandingFeeMap = new HashMap<>();
        if (issuerBankList.isEmpty()) {
            return issuerHandingFeeMap;
        }
        Set<Long> issuerBankIdSet =
          issuerBankList.stream().map(IssuerBankDO::getId).collect(Collectors.toSet());

        List<IssuerHandingFeeDO> queryResult = issuerHandingFeeDao
          .listByIssuerBankIdIn(issuerBankIdSet);
        if (queryResult.isEmpty()) {
            return issuerHandingFeeMap;
        }

        queryResult.forEach(fee -> issuerHandingFeeMap.put(fee.getIssuerBankId(), fee));
        return issuerHandingFeeMap;
    }

    @Override
    public Map<String, Long> getIssuerTotalCardsMap(
      List<IssuerBankDO> issuerBankList, List<CardBrandTypeDto> cardBrandTypeDtoList) {
        if (issuerBankList.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Long> totalCardsResult = new HashMap<>();

        try {

            for (IssuerBankDO issuerBank : issuerBankList) {
                // Create ACS-Integrator query params
                List<IssuerBinRangeQueryDto> issuerBinRangeQueryDtoList =
                  createIssuerBinRangeQueryDto(cardBrandTypeDtoList, issuerBank);

                // post to Integrator
                IssuerTotalCardsQueryResultDto issuerTotalCardsRes =
                  acsIntegratorManager.getTotalCards(
                    new IssuerTotalCardsQueryDto(issuerBank.getCode(), issuerBinRangeQueryDtoList));

                log.debug("[getIssuerTotalCardsMap] query total cards result={}",
                  issuerTotalCardsRes);
                if (!issuerTotalCardsRes.isSuccess()) {
                    throw new OceanException("Get total cards fail.");
                }

                totalCardsResult.putAll(
                  createBankTotalCardResult(
                    issuerTotalCardsRes.getTotalCardsArray(), issuerBank.getCode()));
            }

        } catch (Exception e) {
            log.error("[getIssuerTotalCardsMap] unknown exception", e);
            throw new OceanException("Get total cards fail.");
        }
        return totalCardsResult;
    }

    private List<IssuerBinRangeQueryDto> createIssuerBinRangeQueryDto(
      List<CardBrandTypeDto> cardBrandTypeDtoList, IssuerBankDO issuerBank) {
        List<IssuerBinRangeQueryDto> issuerBinRangeQueryDtoList = new ArrayList<>();

        List<BinRangeDO> binRangeList = binRangeDAO.listByIssuerBankId(issuerBank.getId());

        for (CardBrandTypeDto cardBrandTypeDto : cardBrandTypeDtoList) {
            issuerBinRangeQueryDtoList.add(
              new IssuerBinRangeQueryDto(
                cardBrandTypeDto.getCardBrand().name(),
                cardBrandTypeDto.getCardType().name(),
                appendBinRanges(listByCardBrandAndCardType(cardBrandTypeDto, binRangeList))));
        }
        return issuerBinRangeQueryDtoList;
    }

    /** Query BinRange by cardBrand & cardType */
    private List<BinRangeDO> listByCardBrandAndCardType(
      CardBrandTypeDto cardBrandTypeDto, List<BinRangeDO> binRangeList) {
        return binRangeList.stream()
          .filter(
            binEntity -> cardBrandTypeDto.getCardBrand().getName().equals(binEntity.getCardBrand()))
          .filter(binEntity -> cardBrandTypeDto.getCardType().getCode() == binEntity.getCardType())
          .collect(Collectors.toList());
    }

    private List<IssuerBinRangeQueryDto.BinRanges> appendBinRanges(List<BinRangeDO> binRangeList) {
        List<IssuerBinRangeQueryDto.BinRanges> queryBinRangesList = new ArrayList<>();
        for (BinRangeDO binRange : binRangeList) {
            queryBinRangesList.add(
              new IssuerBinRangeQueryDto.BinRanges(binRange.getStartBin(), binRange.getEndBin()));
        }
        return queryBinRangesList;
    }

    private Map<String, Long> createBankTotalCardResult(
      List<IssuerTotalCardsQueryResultDto.TotalCardsArray> totalCardsArrayList, String bankCode) {

        Map<String, Long> result = new HashMap<>();
        for (IssuerTotalCardsQueryResultDto.TotalCardsArray totalCards : totalCardsArrayList) {
            // 以中信 VISA Credit卡為例，輸出結果為：VC822
            String mapKey =
              totalCards.getCardBrand().substring(0, 1)
                + totalCards.getCardType().substring(0, 1)
                + bankCode;
            result.put(mapKey, totalCards.getTotalCards());
            log.debug(
              "[createBankTotalCardResult] totalCardsMap key={}, value={}",
              mapKey,
              totalCards.getTotalCards());
        }
        return result;
    }

    @Override
    public String createHandlingFeeContent(
      List<IssuerBankDO> issuerBankList,
      List<CardBrandTypeDto> cardBrandTypeDtoList,
      Map<Long, IssuerHandingFeeDO> issuerHandingFeeMap,
      Map<String, Long> totalCardsMap) {

        StringBuilder builder = new StringBuilder();
        for (CardBrandTypeDto cardBrandTypeDto : cardBrandTypeDtoList) {
            builder.append(
              createRawData(cardBrandTypeDto, issuerBankList, issuerHandingFeeMap, totalCardsMap));
        }
        return builder.toString();
    }

    private String createRawData(
      CardBrandTypeDto cardBrandTypeDto,
      List<IssuerBankDO> issuerBankList,
      Map<Long, IssuerHandingFeeDO> issuerHandingFeeMap,
      Map<String, Long> totalCardsMap) {

        StringBuilder builder = new StringBuilder();
        for (IssuerBankDO issuerBank : issuerBankList) {
            if (!issuerHandingFeeMap.containsKey(issuerBank.getId())) {
                continue;
            }
            try {
                IssuerHandingFeeDO issuerHandingFee = issuerHandingFeeMap.get(issuerBank.getId());
                BankHandlingFeeRawDataBO rowDataBo =
                  BankHandlingFeeRawDataBO.builder()
                    .cardBrandTypeDto(cardBrandTypeDto)
                    .issuerBankCode(issuerBank.getCode())
                    .totalCards(
                      getTotalCards(cardBrandTypeDto, issuerBank.getCode(), totalCardsMap))
                    .issuerHandlingFeeType(issuerHandingFee.getHandingFeeType())
                    .issuerHandlingFeePerCard(issuerHandingFee.getFeePerCard())
                    .issuerHandlingFeeThresholdFee(issuerHandingFee.getThresholdFee())
                    .issuerHandlingFeeMinimumFee(issuerHandingFee.getMinimumFee())
                    .issuerHandlingFeePerMonth(issuerHandingFee.getFeePerMonth())
                    .build();
                builder.append(rowDataBo.toString());
            } catch (UnsupportedDataTypeException e) {
                log.warn("[createRawData] unsupported data type", e);
                builder.append(e.getMessage());
            }
        }
        return builder.toString();
    }

    private Long getTotalCards(
      CardBrandTypeDto cardBrandTypeDto, String issuerBankCode, Map<String, Long> totalCardsMap) {
        String totalCardKey =
          createTotalCardKey(
            cardBrandTypeDto.getCardBrand().name(),
            cardBrandTypeDto.getCardType().name(),
            issuerBankCode);
        Long totalCards =
          totalCardsMap.get(totalCardKey) == null ? 0L : totalCardsMap.get(totalCardKey);
        log.debug("[getTotalCards] totalCardKey={}, totalCards={}", totalCardKey, totalCards);
        return totalCards;
    }

    private String createTotalCardKey(
      String cardBrandName, String cardTypeName, String issuerBankCode) {
        return cardBrandName.substring(0, 1) + cardTypeName.substring(0, 1) + issuerBankCode;
    }
}
