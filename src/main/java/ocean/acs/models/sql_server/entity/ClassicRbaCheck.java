package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.ClassicRbaCheckDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@DynamicInsert
@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CLASSIC_RBA_CHECK)
public class ClassicRbaCheck extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CLASSIC_RBA_ENABLED)
    private Boolean classicRbaEnabled = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_PURCHASE_AMOUNT_ENABLED)
    private Boolean purchaseAmountEnabled = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_PURCHASE_AMOUNT_AMOUNT)
    private String purchaseAmountAmount;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_PURCHASE_AMOUNT_MIN_AMOUNT)
    private String purchaseAmountMinAmount;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_PURCHASE_AMOUNT_CURRENCY_CODE)
    private String purchaseAmountCurrencyCode;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_ENABLED)
    private Boolean cardholderDataEnabled = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_NAME)
    private Boolean cardholderDataName = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_EMAIL)
    private Boolean cardholderDataEmail = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_POSTCODE)
    private Boolean cardholderDataPostcode = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_PHONE)
    private Boolean cardholderDataPhone = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_ENABLED)
    private Boolean cumulativeAmountEnabled = Boolean.FALSE;

    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_PERIOD)
    private Long cumulativeAmountPeriod;

    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_AMOUNT)
    private String cumulativeAmountAmount;

    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_CURRENCYCODE)
    private String cumulativeAmountCurrencyCode;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_TX_ENABLED)
    private Boolean cumulativeAmountTxEnabled = Boolean.FALSE;

    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_TX_COUNT)
    private Long cumulativeAmountTxCount;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CUMULATIVE_TRANSACTION_ENABLED)
    private Boolean cumulativeTransactionEnabled = Boolean.FALSE;

    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_CUMULATIVE_TRANSACTION_COUNT)
    private Long cumulativeTransactionCount;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_ENABLED)
    private Boolean locationConsistencyEnabled = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_IP)
    private Boolean locationConsistencyIp = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_BRW_TZ)
    private Boolean locationConsistencyBrwTz = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_BILLING)
    private Boolean locationConsistencyBilling = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_SHIPPING)
    private Boolean locationConsistencyShipping = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_BROWSER_LANGUAGE_ENABLED)
    private Boolean browserLanguageEnabled = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_BROWSER_LANGUAGE_LIST)
    private String browserLanguageList;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_VPN_ENABLED)
    private Boolean vpnEnabled = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_PROXY_ENABLED)
    private Boolean proxyEnabled = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_PRIVATE_BROWSING_ENABLED)
    private Boolean privateBrowsingEnabled = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_DEVICE_VARIATION_ENABLED)
    private Boolean deviceVariationEnabled = Boolean.FALSE;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_MCC_ENABLED)
    private Boolean mccEnabled = Boolean.FALSE;

    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_MCC_LIST)
    private String mccList;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_RECURRING_PAYMENT_ENABLED)
    private Boolean recurringPaymentEnabled = Boolean.FALSE;

    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_RECURRING_PAYMENT_FREQUENCY)
    private Long recurringPaymentFrequency;

    @Column(name = DBKey.COL_CLASSIC_RBA_CHECK_RECURRING_PAYMENT_EXPIRATION)
    private Long recurringPaymentExpiration;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public ClassicRbaCheck(Long id, Long issuerBankId, Boolean classicRbaEnabled,
      Boolean purchaseAmountEnabled, String purchaseAmountAmount,
      String purchaseAmountMinAmount, String purchaseAmountCurrencyCode,
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

    public static ClassicRbaCheck valueOf(ClassicRbaCheckDO d) {
        ClassicRbaCheck a = new ClassicRbaCheck();
        a.setId(d.getId());
        a.setIssuerBankId(d.getIssuerBankId());
        a.setClassicRbaEnabled(d.getClassicRbaEnabled());
        a.setPurchaseAmountEnabled(d.getPurchaseAmountEnabled());
        a.setPurchaseAmountAmount(d.getPurchaseAmountAmount());
        a.setPurchaseAmountMinAmount(d.getPurchaseAmountMinAmount());
        a.setPurchaseAmountCurrencyCode(d.getPurchaseAmountCurrencyCode());
        a.setCardholderDataEnabled(d.getCardholderDataEnabled());
        a.setCardholderDataName(d.getCardholderDataName());
        a.setCardholderDataEmail(d.getCardholderDataEmail());
        a.setCardholderDataPostcode(d.getCardholderDataPostcode());
        a.setCardholderDataPhone(d.getCardholderDataPhone());
        a.setCumulativeAmountEnabled(d.getCumulativeAmountEnabled());
        a.setCumulativeAmountPeriod(d.getCumulativeAmountPeriod());
        a.setCumulativeAmountAmount(d.getCumulativeAmountAmount());
        a.setCumulativeAmountCurrencyCode(d.getCumulativeAmountCurrencyCode());
        a.setCumulativeAmountTxEnabled(d.getCumulativeAmountTxEnabled());
        a.setCumulativeAmountTxCount(d.getCumulativeAmountTxCount());
        a.setCumulativeTransactionEnabled(d.getCumulativeTransactionEnabled());
        a.setCumulativeTransactionCount(d.getCumulativeTransactionCount());
        a.setLocationConsistencyEnabled(d.getLocationConsistencyEnabled());
        a.setLocationConsistencyIp(d.getLocationConsistencyIp());
        a.setLocationConsistencyBrwTz(d.getLocationConsistencyBrwTz());
        a.setLocationConsistencyBilling(d.getLocationConsistencyBilling());
        a.setLocationConsistencyShipping(d.getLocationConsistencyShipping());
        a.setBrowserLanguageEnabled(d.getBrowserLanguageEnabled());
        a.setBrowserLanguageList(d.getBrowserLanguageList());
        a.setVpnEnabled(d.getVpnEnabled());
        a.setProxyEnabled(d.getProxyEnabled());
        a.setPrivateBrowsingEnabled(d.getPrivateBrowsingEnabled());
        a.setDeviceVariationEnabled(d.getDeviceVariationEnabled());
        a.setMccEnabled(d.getMccEnabled());
        a.setMccList(d.getMccList());
        a.setRecurringPaymentEnabled(d.getRecurringPaymentEnabled());
        a.setRecurringPaymentFrequency(d.getRecurringPaymentFrequency());
        a.setRecurringPaymentExpiration(d.getRecurringPaymentExpiration());
        a.setAuditStatus(d.getAuditStatus());
        a.setCreator(d.getCreator());
        a.setCreateMillis(d.getCreateMillis());
        a.setUpdater(d.getUpdater());
        a.setUpdateMillis(d.getUpdateMillis());
        a.setDeleteFlag(d.getDeleteFlag());
        a.setDeleter(d.getDeleter());
        a.setDeleteMillis(d.getDeleteMillis());
        return a;
    }

}
