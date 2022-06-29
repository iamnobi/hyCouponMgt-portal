package com.cherri.acs_portal.service.bo;

import com.cherri.acs_portal.dto.bank.CardBrandTypeDto;
import com.cherri.acs_portal.dto.system.CardBrandDTO;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.activation.UnsupportedDataTypeException;
import ocean.acs.commons.enumerator.CardType;
import ocean.acs.commons.enumerator.HandingFeeType;
import org.apache.commons.lang3.StringUtils;

/**
 * 銀行手續費檔 Example:<br> VCF6223950004 1I000000001000000000007X 000000000000000
 */
public class BankHandlingFeeRawDataBO {

    private static final int MAX_SETL_AMT_LENGTH = 15;
    private static final int MAX_SETL_TOT_CNT_LENGTH = 6;
    private static final ThreadLocal<NumberFormat> AMOUNT_FORMATTER =
      ThreadLocal.withInitial(() -> new DecimalFormat("#.0000"));

    /**
     * 卡組織:取第一個字母, V:visa, M:mastercard <br> position:01
     */
    private String cardPlan;

    /**
     * 卡別:取第一個字母, C:credit, D:debit, X:不分, A:支付寶, W:微信<br> position:02
     */
    private String cardType;

    /**
     * 檔案型態: F:FISC Fee, I:IRF Fee <br> position:03
     */
    private final String recType = "F";

    /**
     * 手續費代碼<br> position:04~07
     */
    private final String feeCode = "6223";

    /**
     * 入帳單位<br> position:08~10
     */
    private final String creditBank = "950";

    /**
     * 扣帳單位<br> position:11~13
     */
    private String debitBank;

    /**
     * 強制扣帳單位: 保留彈性(未使用者填空白)<br> position:14~16
     */
    private final String setlBank = "   ";

    /**
     * 交易區域: 0:跨國, 1:非跨國, X:不分<br> position:17
     */
    private final String setlFlag = "1";

    /**
     * 手續費所分類: A:收單業務, I:發卡業務<br> position:18
     */
    private final String setlType = "I";

    /**
     * 手續費金額: 9(11)V9999(小數4位) <br> position:19~33
     */
    private String setlAmt;

    /**
     * 交易累計筆數<br> position:34~39
     */
    private String setlTotCnt;

    /**
     * 手續費所屬業務: CASH:ATM交易, ORIG:非ATM交易, X:不分; (左靠右補空白) <br> position:40~44
     */
    private final String setlBiz = "X    ";

    /**
     * 交易累計金額: 9(13)V99(小數2位)<br> position:45~59
     */
    private final String setlTotAmt = "000000000000000";

    /**
     * 子類別: 左靠右補空白 E:悠遊卡, P:一卡通, I:ICASH, H:HappyCash, 01:Inbound, 90:O2O反掃交易, 91:O2O正掃交易, QR:
     * QRCODE交易 <br> position:60~61
     */
    private final String subCategory = "  ";

    /**
     * 保留欄位 <br> position:62~78
     */
    private final String resv = "                 ";

    /**
     * 斷行 <br> position:79~80
     */
    private final String newLine = System.lineSeparator();

    private BankHandlingFeeRawDataBO(Builder builder) throws UnsupportedDataTypeException {
        Double handlingFee =
          calculateHandingFee(
            builder.totalCards,
            builder.issuerHandlingFeeType,
            builder.issuerHandlingFeePerCard,
            builder.issuerHandlingFeeThresholdFee,
            builder.issuerHandlingFeeMinimumFee,
            builder.issuerHandlingFeePerMonth);

        String handingFeeAmount = handlingFeeAmountFormat(handlingFee);
        this.cardPlan = getCardPlanFirstLetter(builder.cardBrandTypeDto.getCardBrand());
        this.cardType = getCardTypeFirstLetter(builder.cardBrandTypeDto.getCardType());
        this.debitBank = builder.issuerBankCode;
        this.setlAmt = handingFeeAmount;
        this.setlTotCnt =
          setlTotCntFormat(builder.issuerHandlingFeeType, builder.totalCards,
            handlingFee.intValue());
    }

    public static Builder builder() {
        return new Builder();
    }

    private String getCardPlanFirstLetter(CardBrandDTO cardBrand)
      throws UnsupportedDataTypeException {
        return cardBrand.name().substring(0, 1);
    }

    private String getCardTypeFirstLetter(CardType cardType) throws UnsupportedDataTypeException {
        switch (cardType) {
            case DEBIT:
            case CREDIT:
                return cardType.name().substring(0, 1);
            case HOST:
                return "X"; // 不分
            default:
                throw new UnsupportedDataTypeException("Unsupported card type");
        }
    }

    /**
     * 手續費顯示：<br> 1. 顯示至小數後四位<br> 2. 不顯示小數點'.'<br> 3. 往左補0共顯示11位<br>
     */
    private String handlingFeeAmountFormat(Double handingFee) {
        // 小數後4位
        String returnStr = AMOUNT_FORMATTER.get().format(handingFee);

        // 不顯示小數點，往左補0到11位．．
        returnStr = returnStr.replaceAll("[.]", "");
        returnStr = StringUtils.leftPad(returnStr, MAX_SETL_AMT_LENGTH, '0');

        return returnStr;
    }

    /**
     * calculateHandingFee :<br> 1.by 卡數 ：手續費計算＝totalCards＊feePerCard， 如果totalCards＊feePerCard <
     * thresholdFee，則手續費為minimumFee．
     * <br>
     * 2.by 月費 : 直接取feePerMonth
     */
    private Double calculateHandingFee(
      Long totalCards,
      Integer handingFeeType,
      Double feePerCard,
      Double thresholdFee,
      Double minimumFee,
      Double feePerMonth) {

        Double handingFee;
        if (HandingFeeType.CARD.getCode() == handingFeeType) { // by card
            if (totalCards == null) {
                handingFee = 0.0;
            } else if (feePerCard * totalCards < thresholdFee) {
                handingFee = minimumFee;
            } else {
                handingFee = feePerCard * totalCards;
            }
        } else {
            handingFee = feePerMonth; // by month
        }
        handingFee = round(handingFee);

        return handingFee;
    }

    /**
     * 四捨五入，取到小數點第四位
     */
    private double round(double value) {
        BigDecimal bigData = new BigDecimal(value);
        bigData = bigData.setScale(4, BigDecimal.ROUND_HALF_UP);
        return bigData.doubleValue();
    }

    /**
     * 依計費方式格式化該欄位值，依卡量計算則放[卡片數]，依月費計算則放[手續費金額]
     *
     * @param handingFeeType 依卡量計算(0), 固定月費(1), ex:0
     * @param totalCards     卡片數量, ex:7
     * @param handingFee     手續費金額(FISC現行系統手續費沒有用到小數點), ex:5
     * @return ex:000007
     */
    private String setlTotCntFormat(Integer handingFeeType, Long totalCards, Integer handingFee) {
        return handingFeeType == HandingFeeType.CARD.getCode()
          ? StringUtils.leftPad(totalCards.toString(), MAX_SETL_TOT_CNT_LENGTH, '0')
          : StringUtils.leftPad(String.valueOf(handingFee), MAX_SETL_TOT_CNT_LENGTH, '0');
    }

    @Override
    public String toString() {
        return new StringBuilder()
          .append(cardPlan)
          .append(cardType)
          .append(recType)
          .append(feeCode)
          .append(creditBank)
          .append(debitBank)
          .append(setlBank)
          .append(setlFlag)
          .append(setlType)
          .append(setlAmt)
          .append(setlTotCnt)
          .append(setlBiz)
          .append(setlTotAmt)
          .append(subCategory)
          .append(resv)
          .append(newLine)
          .toString();
    }

    public static class Builder {

        private CardBrandTypeDto cardBrandTypeDto;
        private String issuerBankCode;
        private Long totalCards;
        private Integer issuerHandlingFeeType;
        private Double issuerHandlingFeePerCard;
        private Double issuerHandlingFeeThresholdFee;
        private Double issuerHandlingFeeMinimumFee;
        private Double issuerHandlingFeePerMonth;

        public Builder cardBrandTypeDto(CardBrandTypeDto cardBrandTypeDto) {
            this.cardBrandTypeDto = cardBrandTypeDto;
            return this;
        }

        public Builder issuerBankCode(String issuerBankCode) {
            this.issuerBankCode = issuerBankCode;
            return this;
        }

        public Builder totalCards(Long totalCards) {
            this.totalCards = totalCards;
            return this;
        }

        public Builder issuerHandlingFeeType(Integer issuerHandlingFeeType) {
            this.issuerHandlingFeeType = issuerHandlingFeeType;
            return this;
        }

        public Builder issuerHandlingFeePerCard(Double issuerHandlingFeePerCard) {
            this.issuerHandlingFeePerCard = issuerHandlingFeePerCard;
            return this;
        }

        public Builder issuerHandlingFeeThresholdFee(Double issuerHandlingFeeThresholdFee) {
            this.issuerHandlingFeeThresholdFee = issuerHandlingFeeThresholdFee;
            return this;
        }

        public Builder issuerHandlingFeeMinimumFee(Double issuerHandlingFeeMinimumFee) {
            this.issuerHandlingFeeMinimumFee = issuerHandlingFeeMinimumFee;
            return this;
        }

        public Builder issuerHandlingFeePerMonth(Double issuerHandlingFeePerMonth) {
            this.issuerHandlingFeePerMonth = issuerHandlingFeePerMonth;
            return this;
        }

        /**
         * @return
         * @throws UnsupportedDataTypeException (CardBrand or CardType) 可能會有Unknown的狀況
         */
        public BankHandlingFeeRawDataBO build() throws UnsupportedDataTypeException {
            return new BankHandlingFeeRawDataBO(this);
        }
    }
}
