package ocean.acs.commons.enumerator;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.EnumUtils;

public enum CardType {
  CREDIT(0, "CREDIT"),
  DEBIT(1, "DEBIT"),
  HOST(2, "主機連線"),
  UNKNOWN(99, "Unknown");

  private final int code;
  private final String acsOneDbValue;

  CardType(int code, String acsOneDbValue) {
    this.code = code;
    this.acsOneDbValue = acsOneDbValue;
  }

  public static final List<Enum> VALUES = Arrays.asList(CardType.values());

  public static CardType getByName(String cardTypeName) {
//    return (CardType) EnumUtils.getByName(CardType.class, VALUES, cardTypeName);  //TODO
	  return EnumUtils.getEnum(CardType.class, cardTypeName);
  }

  /** If not found, then return null */
  public static CardType getByCode(int code) {
    return (CardType)
        VALUES.stream().filter(enm -> ((CardType) enm).getCode() == code).findFirst().orElse(null);
  }

  public static CardType getByAcsOneDbValue(String acsOneDbValue) {
    return (CardType)
            VALUES.stream().filter(enm -> ((CardType) enm).getAcsOneDbValue().equals(acsOneDbValue)).findFirst().orElse(UNKNOWN);
  }

  public int getCode() {
    return code;
  }

  public String getAcsOneDbValue() {
    return acsOneDbValue;
  }
}
