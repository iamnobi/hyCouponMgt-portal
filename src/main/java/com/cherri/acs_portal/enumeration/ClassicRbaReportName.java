package com.cherri.acs_portal.enumeration;

import com.cherri.acs_portal.constant.MessageConstants;
import lombok.Getter;

public enum ClassicRbaReportName {

    PURCHASE_AMOUNT(MessageConstants.RBA_AMOUNT_PER_TRANSACTION, "#APT"),
    CARDHOLDER_DATA(MessageConstants.RBA_CARDHOLDER_DATA_CHECK, "#CDC"),
    CUMULATIVE_AMOUNT(MessageConstants.RBA_CUMULATIVE_AMOUNT_CHECK, "#CAC"),
    LOCATION_CONSISTENCY(MessageConstants.RBA_LOCATION_CONSISTENCY_CHECK, "#LCC"),
    BROWSER_LANGUAGE(MessageConstants.RBA_BROWSER_LANGUAGE_CHECK, "#BLC"),
    VPN(MessageConstants.RBA_VPN_CHECK, "#VPN"),
    PROXY(MessageConstants.RBA_PROXY_CHECK, "#PXY"),
    PRIVATE_BROWSING(MessageConstants.RBA_PRIVATE_BROWSING_CHECK, "#PBC"),
    DEVICE_VARIATION(MessageConstants.RBA_DEVICE_VARIATION_CHECK, "#DVC"),
    MCC(MessageConstants.RBA_MCC_RISKY_MERCHANT, "#MCC"),
    RECURRING_PAYMENT(MessageConstants.RBA_RECURRING_PAYMENT_RISK, "#RPR"),

    // deprecated
    CUMULATIVE_TRANSACTION("累積交易筆數計算", "#CTF"),

    // NOTE: 非 Classic RBA rule, 目前不會出現在 Portal 上
    //       這是 CTBC 的需求
    BIN_RANGE("Bin Range" , ""),
    BLOCK_CODE("BlockCode（鎖卡）", ""),
    BLACK_LIST_CARD("黑名單(卡號)", ""),
    BLACK_LIST_IP("黑名單(IP)", ""),
    BLACK_LIST_MERCHANT_URL("黑名單(Merchant URL)", ""),
    BLACK_LIST_DEVICE_ID("黑名單(裝置識別碼)", ""),
    WHITE_LIST("白名單", ""),
    ATTEMPT("人工彈性授權", "");

    @Getter
    private String name;
    @Getter
    private String code;

    private ClassicRbaReportName(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getCodeAndName(){
        return code + " " + MessageConstants.get(name);
    }
}
