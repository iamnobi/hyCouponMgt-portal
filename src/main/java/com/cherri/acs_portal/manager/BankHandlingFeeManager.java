package com.cherri.acs_portal.manager;

import com.cherri.acs_portal.dto.bank.CardBrandTypeDto;
import java.util.List;
import java.util.Map;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.entity.IssuerHandingFeeDO;

public interface BankHandlingFeeManager {

  /** CardBrand + CardType List */
  List<CardBrandTypeDto> getCardBrandTypeList();

    /** issuerHandingFeeMap : key - Issuer Bank Id value - IssuerHandingFee */
    Map<Long, IssuerHandingFeeDO> getIssuerHandingFeeMap(List<IssuerBankDO> issuerBankList);

    /**
     * query total numbers of card from Integrator <br> 1. query bin range(start, end) by
     * issuerBankId and cardBrand and cardType<br> 2. query Integrator total numbers of card by bin
     * range.<br> 3. Map<String, Long> totalCardsMap : key = cardBrand(first letter) +
     * cardTypeName(first letter) + issuerBankId, value=total cards <br>
     */
    Map<String, Long> getIssuerTotalCardsMap(
      List<IssuerBankDO> issuerBankList, List<CardBrandTypeDto> cardBrandTypeDtoList);

    /**
     * 建立手續費檔案內容，example:<br> VCF6223950004 1I000000001000000000007X 000000000000000 <br>
     * VCF6223950050 1I000000220000000022000X 000000000000000 <br> VCF6223950006
     * 1I000000009990000000999X 000000000000000 <br> VCF6223950700 1I000000550000000055000X
     * 000000000000000 <br> VCF6223950053 1I000000000010000000001X 000000000000000 <br>
     * VCF6223950005 1I000000009990000000999X 000000000000000 <br>
     */
    String createHandlingFeeContent(
      List<IssuerBankDO> issuerBankList,
      List<CardBrandTypeDto> cardBrandTypeDtoList,
      Map<Long, IssuerHandingFeeDO> issuerHandingFeeMap,
      Map<String, Long> totalCardsMap);
}
