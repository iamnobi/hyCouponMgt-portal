package ocean.acs.commons.enumerator;

import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.utils.EnumUtils;

@Log4j2
public enum TransStatus2Eci {
  // VISA
  VISA_TX_STATUS_Y("05", null),
  VISA_TX_STATUS_A("06", null),
  VISA_TX_STATUS_N("07", null),
  VISA_TX_STATUS_U("07", null),

  // JCB
  JCB_TX_STATUS_Y("05", null),
  JCB_TX_STATUS_A("06", null),
  JCB_TX_STATUS_N(null, null),
  JCB_TX_STATUS_N_FIRST_TIMEOUT("07", "07"),
  JCB_TX_STATUS_U("07", "07"),

  // AMEX
  AMERICAN_EXPRESS_TX_STATUS_Y("05", null),
  AMERICAN_EXPRESS_TX_STATUS_A("06", null),
  AMERICAN_EXPRESS_TX_STATUS_N("07", null),
  AMERICAN_EXPRESS_TX_STATUS_U("07", null),

  // UnionPay
  UNIONPAY_TX_STATUS_Y("05", null),
  UNIONPAY_TX_STATUS_A("06", null),
  UNIONPAY_TX_STATUS_U("07", null),
  UNIONPAY_TX_STATUS_N("10", null),

  // MasterCard
  MASTERCARD_TX_STATUS_Y("02", "N2"),
  MASTERCARD_TX_STATUS_A("01", "N2"),
  MASTERCARD_TX_STATUS_N("00", "N0"),
  MASTERCARD_TX_STATUS_U("00", "N0");

  private final String paEci;
  private final String npaEci;

  private static final Class<TransStatus2Eci> THIS_CLZ = TransStatus2Eci.class;
  private static final List<Enum> THIS_VALUES = Arrays.asList(TransStatus2Eci.values());

  TransStatus2Eci(String paEci, String npaEci) {
    this.paEci = paEci;
    this.npaEci = npaEci;
  }

  public String getPaEci() {
    return paEci;
  }
  /** If the null is a return value, it means that it was not found. */
  public static String getEci(String cardBrand, String messageCategory, String transStatus) {
    try {
      transStatus = transStatus.toUpperCase();
      String name = String.format("%s_TX_STATUS_%s", cardBrand, transStatus);
      TransStatus2Eci t =
          (TransStatus2Eci) EnumUtils.getByName(THIS_CLZ, THIS_VALUES, name.toUpperCase());
      if (MessageCategory.isNPA(messageCategory)) {
        return t.getNpaEci();
      }
      return t.getPaEci();
    } catch (Exception e) {
      return null;
    }
  }

  public static String getEciThreeDS1(String cardBrand, TransStatus transStatus) {
    // 1.0 只有 Y 有 ECI
    if (transStatus != TransStatus.Authentication) {
      return null;
    }

    // Y ECI 的狀況跟 2.0 一樣
    return getEci(cardBrand, MessageCategory.PA.getCode(), transStatus.getCode());
  }

  public String getNpaEci() {
    return npaEci;
  }
}
