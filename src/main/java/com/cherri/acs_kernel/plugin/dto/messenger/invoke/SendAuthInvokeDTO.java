package com.cherri.acs_kernel.plugin.dto.messenger.invoke;

import com.cherri.acs_kernel.plugin.dto.AuthenticationChannel;
import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class SendAuthInvokeDTO extends InvokeDTO {

    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;
    private String acsTransID;
    private String authId;
    private String authCode;
    private String cardNumber;
    private String bankCode;
    private String bankName;

    /**
     * card holder selected authentication channel, contains as least one channel e.g. sms
     * On the other hand, if user has multiple channels and should be notified all of them, then this list shall contain
     * all those channel info.
     */
    private List<AuthenticationChannel> otpAuthChannel;

//    private List<AuthenticationChannel> oobAuthChannel;
//    private List<AuthenticationChannel> decoubleAuthChannel;

    /**
     * pre-defined challenge message which is the outcome of challenge verify template page setting in ACS Portal
     */
    private String templateMessage;

    /**
     * Cherri Awesome Store
     */
    private String merchantName;

    /**
     * e.g. 1.99
     */
    private Double amount;

    /**
     * USD | JPY | MYR | VND | CNY | SGD | EUR | GBP | TWD
     */
    private String currency;

    /**
     * yyyy/MM/dd HH:mm:ss
     */
    private String purchaseDate;

    @Builder
    public SendAuthInvokeDTO(Map<String, String> systemPropertiesMap,
      Map<IssuerPropertyDefinition, String> issuerPropertiesMap, String acsTransID, String authId,
      String authCode, String cardNumber, String bankCode, String bankName,
      List<AuthenticationChannel> otpAuthChannel, String templateMessage, String merchantName, Double amount,
      String currency, String purchaseDate) {
        super(systemPropertiesMap);
        this.issuerPropertiesMap = issuerPropertiesMap;
        this.acsTransID = acsTransID;
        this.authId = authId;
        this.authCode = authCode;
        this.cardNumber = cardNumber;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.otpAuthChannel = otpAuthChannel;
        this.templateMessage = templateMessage;
        this.merchantName = merchantName;
        this.amount = amount;
        this.currency = currency;
        this.purchaseDate = purchaseDate;
    }
}
