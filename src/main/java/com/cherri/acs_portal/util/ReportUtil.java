package com.cherri.acs_portal.util;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.constant.SystemConstants;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ReportUtil {

    public static final Pattern MONTH_QUERY_PATTERN = Pattern.compile("^(\\d{4}-(\\d|\\d{2}))$");
    public static final Pattern DAY_QUERY_PATTERN =
      Pattern.compile("^(\\d{4}-(\\d|\\d{2})-(\\d|\\d{2}))$");

    public static List<String> getTxStatusDailyReportColumn() {
        return Arrays.asList(
            // 交易狀態統計報表
            MessageConstants.get(MessageConstants.EXCEL_BANK_NAME),
            MessageConstants.get(MessageConstants.EXCEL_YEAR),
            MessageConstants.get(MessageConstants.EXCEL_MONTH),
            MessageConstants.get(MessageConstants.EXCEL_DAY),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_TOTAL_TRANSACTION_AMOUNT),
            // MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_OTP_AMOUNT),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_N_AUTHENTICATION_FAILED),
            // MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_A),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_Y_AUTHENTICATION_SUCCEED),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_R_TRANSACTION_REJECT),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_U_SYSTEM_EXCEPTION),
            "U-Rate",
            "N-Rate");
    }

    public static List<String> getTxStatusMonthlyReportColumn() {
        return Arrays.asList(
            // 交易狀態統計報表
            MessageConstants.get(MessageConstants.EXCEL_BANK_NAME),
            MessageConstants.get(MessageConstants.EXCEL_YEAR),
            MessageConstants.get(MessageConstants.EXCEL_MONTH),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_TOTAL_TRANSACTION_AMOUNT),
            // MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_OTP_AMOUNT),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_N_AUTHENTICATION_FAILED),
            // MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_A),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_Y_AUTHENTICATION_SUCCEED),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_R_TRANSACTION_REJECT),
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS_U_SYSTEM_EXCEPTION),
            "U-Rate",
            "N-Rate");
    }

    @Deprecated
    public static boolean isDailyReport(String dateText) {
        return DAY_QUERY_PATTERN.matcher(dateText).matches();
    }

    @Deprecated
    public static long convertToStartTimeMillis(String dateText) throws IllegalArgumentException {
        LocalDate localDate = null;
        // 查詢月報表
        if (MONTH_QUERY_PATTERN.matcher(dateText).matches()) {
            String dayText = "-01";
            localDate =
              LocalDate.parse(dateText + dayText, SystemConstants.DATE_TIME_FORMATTER_YYYY_MM_DD);
        }
        // 查詢日報表
        if (DAY_QUERY_PATTERN.matcher(dateText).matches()) {
            localDate = LocalDate.parse(dateText, SystemConstants.DATE_TIME_FORMATTER_YYYY_MM_DD);
        }
        if (Objects.isNull(localDate)) {
            throw new IllegalArgumentException("Unsupported date format.");
        }
        return localDate.atStartOfDay(EnvironmentConstants.ACS_TIMEZONE_ID).toInstant().toEpochMilli();
    }

    @Deprecated
    public static long convertToEndTimeMillis(String dateText) throws IllegalArgumentException {
        ZonedDateTime zonedDateTime = null;
        // 查詢月報表
        if (ReportUtil.MONTH_QUERY_PATTERN.matcher(dateText).matches()) {
            String dayText = "-01";
            zonedDateTime =
              LocalDate.parse(dateText + dayText, SystemConstants.DATE_TIME_FORMATTER_YYYY_MM_DD)
                .atStartOfDay(EnvironmentConstants.ACS_TIMEZONE_ID)
                .plusMonths(1)
                .minusNanos(1);
        }
        // 查詢日報表
        if (ReportUtil.DAY_QUERY_PATTERN.matcher(dateText).matches()) {
            zonedDateTime =
              LocalDate.parse(dateText, SystemConstants.DATE_TIME_FORMATTER_YYYY_MM_DD)
                .atStartOfDay(EnvironmentConstants.ACS_TIMEZONE_ID)
                .plusDays(1)
                .minusNanos(1);
        }
        if (Objects.isNull(zonedDateTime)) {
            throw new IllegalArgumentException("Unsupported date format.");
        }
        return zonedDateTime.toInstant().toEpochMilli();
    }
}
