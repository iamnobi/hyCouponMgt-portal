package com.cherri.acs_portal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import com.cherri.acs_portal.constant.EnvironmentConstants;

@Log4j2
public class DateUtil {

    // UTC
    public static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");
    public static final TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone(ZONE_ID_UTC);
    private static final DateTimeFormatter FORMATTER_UTC =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(TIME_ZONE_UTC.toZoneId());

    // ACS Time Zone.
    private static final DateTimeFormatter TW_YYYY_MM_DD_HH_MM_SS_PATTERN =
      DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(EnvironmentConstants.ACS_TIMEZONE_ID);

    /**
     * Convert UTC date text with format 'YYYYMMDDHHMMSS'(EMV) to millisecond.
     *
     * @param utcDateTimeStr UTC date text with format 'YYYYMMDDHHMMSS'
     * @return millisecond
     */
    public static long utcToTwDateTimeMillis(String utcDateTimeStr) {
        if (StringUtils.isBlank(utcDateTimeStr)) {
            return 0L;
        }

        ZonedDateTime utcDateTime =
          LocalDateTime.parse(utcDateTimeStr.trim(), FORMATTER_UTC).atZone(ZONE_ID_UTC);
        ZonedDateTime localDateTime = ZonedDateTime
          .ofInstant(utcDateTime.toInstant(), EnvironmentConstants.ACS_TIMEZONE_ID);
        return localDateTime.toInstant().toEpochMilli();
    }

    public static long threeDS1PurchaseDateToMillis(String purchaseDate) {
        if (StringUtils.isBlank(purchaseDate)) {
            return 0L;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date purchaseDateTmp = sdf.parse(purchaseDate);
            return purchaseDateTmp.toInstant().toEpochMilli();

        } catch (ParseException e) {
            log.error("[threeDS1PurchaseDateToMillis] execute error, invalid date string:{}", purchaseDate, e);
            return 0L;
        }
    }

    /**
     * Convert to UTC date text with format 'YYYYMMDDHHMMSS'(EMV) to date text with format 'yyyy/MM/dd HH:mm:ss' GMT+{acs.timezone}.
     * 
     * @param utcDateTimeStr UTC date text with format 'YYYYMMDDHHMMSS'
     * @return The date text with format 'yyyy/MM/dd HH:mm:ss' GMT+{acs.timezone}
     */
    public static String utcToTwDateTimeStr(String utcDateTimeStr) {
        return utcToTwDateTimeStr(utcDateTimeStr, EnvironmentConstants.ACS_TIMEZONE_ID);
    }
    public static String utcToTwDateTimeStr(String utcDateTimeStr, ZoneId zoneId) {
        if (StringUtils.isBlank(utcDateTimeStr)) {
            return "";
        }

        ZonedDateTime utcDateTime =
            LocalDateTime.parse(utcDateTimeStr.trim(), FORMATTER_UTC).atZone(ZONE_ID_UTC);
        LocalDateTime twLocalDateTime = LocalDateTime
            .ofInstant(utcDateTime.toInstant(), zoneId);
        return twLocalDateTime.format(TW_YYYY_MM_DD_HH_MM_SS_PATTERN);
    }

    /**
     * Convert timestamp to date text with format 'yyyy/MM/dd HH:mm:ss' GMT+{acs.timezone}.
     * 
     * @param timeMillis timestamp
     * @return The date text with format 'yyyy/MM/dd HH:mm:ss' GMT+{acs.timezone}
     */
    public static String millisToDateTimeStr(Long timeMillis) {
        return millisToDateTimeStr(timeMillis, EnvironmentConstants.ACS_TIMEZONE_ID);
    }
    public static String millisToDateTimeStr(Long timeMillis, ZoneId zoneId) {
        if (null == timeMillis) {
            return "";
        }

        LocalDateTime localDateTime =
            LocalDateTime
                .ofInstant(Instant.ofEpochMilli(timeMillis), zoneId);
        return TW_YYYY_MM_DD_HH_MM_SS_PATTERN.format(localDateTime);
    }

    public static String millisToUtcDateTimeStr(Long timeMillis) {
        if (null == timeMillis) {
            return "";
        }
        return FORMATTER_UTC.format(Instant.ofEpochMilli(timeMillis));
    }

    public static int calDayDuration(long millisA, long millisB) {
        return (int) ((millisA - millisB) / 1000 / 60 / 60 / 24);
    }
}
