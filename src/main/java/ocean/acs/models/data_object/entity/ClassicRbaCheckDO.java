package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ClassicRbaCheckDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private Boolean classicRbaEnabled = Boolean.FALSE;
    private Boolean purchaseAmountEnabled = Boolean.FALSE;
    private String purchaseAmountAmount = "";
    private String purchaseAmountMinAmount = "";
    private String purchaseAmountCurrencyCode = "";
    private Boolean cardholderDataEnabled = Boolean.FALSE;
    private Boolean cardholderDataName = Boolean.FALSE;
    private Boolean cardholderDataEmail = Boolean.FALSE;
    private Boolean cardholderDataPostcode = Boolean.FALSE;
    private Boolean cardholderDataPhone = Boolean.FALSE;
    private Boolean cumulativeAmountEnabled = Boolean.FALSE;
    private Long cumulativeAmountPeriod;
    private String cumulativeAmountAmount;
    private String cumulativeAmountCurrencyCode;
    private Boolean cumulativeAmountTxEnabled = Boolean.FALSE;
    private Long cumulativeAmountTxCount;
    private Boolean cumulativeTransactionEnabled = Boolean.FALSE;
    private Long cumulativeTransactionCount;
    private Boolean locationConsistencyEnabled = Boolean.FALSE;
    private Boolean locationConsistencyIp = Boolean.FALSE;
    private Boolean locationConsistencyBrwTz = Boolean.FALSE;
    private Boolean locationConsistencyBilling = Boolean.FALSE;
    private Boolean locationConsistencyShipping = Boolean.FALSE;
    private Boolean browserLanguageEnabled = Boolean.FALSE;
    private String browserLanguageList = "";
    private Boolean vpnEnabled = Boolean.FALSE;
    private Boolean proxyEnabled = Boolean.FALSE;
    private Boolean privateBrowsingEnabled = Boolean.FALSE;
    private Boolean deviceVariationEnabled = Boolean.FALSE;
    private Boolean mccEnabled = Boolean.FALSE;
    private String mccList;
    private Boolean recurringPaymentEnabled = Boolean.FALSE;
    private Long recurringPaymentFrequency;
    private Long recurringPaymentExpiration;
    private String auditStatus;

    public ClassicRbaCheckDO(Long id, Long issuerBankId, Boolean classicRbaEnabled,
      Boolean purchaseAmountEnabled, String purchaseAmountAmount, String purchaseAmountMinAmount,
      String purchaseAmountCurrencyCode,
      Boolean cardholderDataEnabled, Boolean cardholderDataName, Boolean cardholderDataEmail,
      Boolean cardholderDataPostcode, Boolean cardholderDataPhone,
      Boolean cumulativeAmountEnabled, Long cumulativeAmountPeriod,
      String cumulativeAmountAmount, String cumulativeAmountCurrencyCode, Boolean cumulativeAmountTxEnabled,
      Long cumulativeAmountTxCount, Boolean cumulativeTransactionEnabled,
      Long cumulativeTransactionCount, Boolean locationConsistencyEnabled,
      Boolean locationConsistencyIp, Boolean locationConsistencyBrwTz,
      Boolean locationConsistencyBilling, Boolean locationConsistencyShipping,
      Boolean browserLanguageEnabled, String browserLanguageList, Boolean vpnEnabled,
      Boolean proxyEnabled, Boolean privateBrowsingEnabled, Boolean deviceVariationEnabled,
      Boolean mccEnabled, String mccList, Boolean recurringPaymentEnabled,
      Long recurringPaymentFrequency, Long recurringPaymentExpiration, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.classicRbaEnabled = classicRbaEnabled;
        this.purchaseAmountEnabled = purchaseAmountEnabled;
        this.purchaseAmountAmount = purchaseAmountAmount;
        this.purchaseAmountMinAmount = purchaseAmountMinAmount;
        this.purchaseAmountCurrencyCode = purchaseAmountCurrencyCode;
        this.cardholderDataEnabled = cardholderDataEnabled;
        this.cardholderDataName = cardholderDataName;
        this.cardholderDataEmail = cardholderDataEmail;
        this.cardholderDataPostcode = cardholderDataPostcode;
        this.cardholderDataPhone = cardholderDataPhone;
        this.cumulativeAmountEnabled = cumulativeAmountEnabled;
        this.cumulativeAmountPeriod = cumulativeAmountPeriod;
        this.cumulativeAmountAmount = cumulativeAmountAmount;
        this.cumulativeAmountCurrencyCode = cumulativeAmountCurrencyCode;
        this.cumulativeAmountTxEnabled = cumulativeAmountTxEnabled;
        this.cumulativeAmountTxCount = cumulativeAmountTxCount;
        this.cumulativeTransactionEnabled = cumulativeTransactionEnabled;
        this.cumulativeTransactionCount = cumulativeTransactionCount;
        this.locationConsistencyEnabled = locationConsistencyEnabled;
        this.locationConsistencyIp = locationConsistencyIp;
        this.locationConsistencyBrwTz = locationConsistencyBrwTz;
        this.locationConsistencyBilling = locationConsistencyBilling;
        this.locationConsistencyShipping = locationConsistencyShipping;
        this.browserLanguageEnabled = browserLanguageEnabled;
        this.browserLanguageList = browserLanguageList;
        this.vpnEnabled = vpnEnabled;
        this.proxyEnabled = proxyEnabled;
        this.privateBrowsingEnabled = privateBrowsingEnabled;
        this.deviceVariationEnabled = deviceVariationEnabled;
        this.mccEnabled = mccEnabled;
        this.mccList = mccList;
        this.recurringPaymentEnabled = recurringPaymentEnabled;
        this.recurringPaymentFrequency = recurringPaymentFrequency;
        this.recurringPaymentExpiration = recurringPaymentExpiration;
        this.auditStatus = auditStatus;
    }

    public static ClassicRbaCheckDO valueOf(ocean.acs.models.oracle.entity.ClassicRbaCheck e) {
        return new ClassicRbaCheckDO(e.getId(), e.getIssuerBankId(), e.getClassicRbaEnabled(),
                e.getPurchaseAmountEnabled(), e.getPurchaseAmountAmount(), e.getPurchaseAmountMinAmount(),
                e.getPurchaseAmountCurrencyCode(),
                e.getCardholderDataEnabled(), e.getCardholderDataName(), e.getCardholderDataEmail(),
                e.getCardholderDataPostcode(), e.getCardholderDataPhone(),
                e.getCumulativeAmountEnabled(), e.getCumulativeAmountPeriod(),
                e.getCumulativeAmountAmount(), e.getCumulativeAmountCurrencyCode(), e.getCumulativeAmountTxEnabled(),
                e.getCumulativeAmountTxCount(), e.getCumulativeTransactionEnabled(),
                e.getCumulativeTransactionCount(), e.getLocationConsistencyEnabled(),
                e.getLocationConsistencyIp(), e.getLocationConsistencyBrwTz(),
                e.getLocationConsistencyBilling(), e.getLocationConsistencyShipping(),
                e.getBrowserLanguageEnabled(), e.getBrowserLanguageList(), e.getVpnEnabled(),
                e.getProxyEnabled(), e.getPrivateBrowsingEnabled(), e.getDeviceVariationEnabled(),
                e.getMccEnabled(), e.getMccList(), e.getRecurringPaymentEnabled(),
                e.getRecurringPaymentFrequency(), e.getRecurringPaymentExpiration(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

    public static ClassicRbaCheckDO valueOf(ocean.acs.models.sql_server.entity.ClassicRbaCheck e) {
        return new ClassicRbaCheckDO(e.getId(), e.getIssuerBankId(), e.getClassicRbaEnabled(),
                e.getPurchaseAmountEnabled(), e.getPurchaseAmountAmount(), e.getPurchaseAmountMinAmount(),
                e.getPurchaseAmountCurrencyCode(),
                e.getCardholderDataEnabled(), e.getCardholderDataName(), e.getCardholderDataEmail(),
                e.getCardholderDataPostcode(), e.getCardholderDataPhone(),
                e.getCumulativeAmountEnabled(), e.getCumulativeAmountPeriod(),
                e.getCumulativeAmountAmount(), e.getCumulativeAmountCurrencyCode(), e.getCumulativeAmountTxEnabled(),
                e.getCumulativeAmountTxCount(), e.getCumulativeTransactionEnabled(),
                e.getCumulativeTransactionCount(), e.getLocationConsistencyEnabled(),
                e.getLocationConsistencyIp(), e.getLocationConsistencyBrwTz(),
                e.getLocationConsistencyBilling(), e.getLocationConsistencyShipping(),
                e.getBrowserLanguageEnabled(), e.getBrowserLanguageList(), e.getVpnEnabled(),
                e.getProxyEnabled(), e.getPrivateBrowsingEnabled(), e.getDeviceVariationEnabled(),
                e.getMccEnabled(), e.getMccList(), e.getRecurringPaymentEnabled(),
                e.getRecurringPaymentFrequency(), e.getRecurringPaymentExpiration(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

}
