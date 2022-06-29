package com.cherri.acs_portal.constant;

import java.time.format.DateTimeFormatter;

public class SystemConstants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER_YYYYMMDD =
      DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final int FORGET_MIMA_COUNT = 3;
    // 可以寄送 10 次
    public static final int SEND_OTP_COUNT = 9;
    // 可以驗證失敗 3 次
    public static final int VERIFY_OTP_COUNT = 2;

    public static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

    public static final String CARD_BRAND = "CARD_BRAND";

}
