package com.cherri.acs_portal.dto.cardholder;


import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.EnrollStatus;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class HolderSummaryDTO {

    private String name;
    private String identifyNumber;
    private String birthday;

    private HolderContactDTO credit;
    private HolderContactDTO debit;
    private List<CardSetting> cardList;

    @Data
    @ToString
    @NoArgsConstructor
    public static class CardSetting {

        private Long panId; // panId
        private String cardType;
        private String cardNumber;

        private AuditStatus attemptAuditStatus;

        private Boolean verifyEnabled;
        private AuditStatus verifyEnabledAuditStatus;

        private Boolean otpLock;
        private AuditStatus otpLockAuditStatus;

    }
}
