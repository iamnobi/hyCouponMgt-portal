package com.cherri.acs_kernel.plugin.dto.cardholder.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.CardInfo;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class CardholderGetCardListResultDTO extends ResultDTO {
    private String cardHolderId;
    private String cardHolderName;
    private String bankCode;
    private List<CardInfo> cardInfoList;
    private boolean isCardListExists;

    private CardholderGetCardListResultDTO(
            boolean isSuccess,
            boolean isCardListExists,
            Exception exception,
            String cardHolderId,
            String cardHolderName,
            String bankCode,
            List<CardInfo> cardInfoList) {
        super(isSuccess, exception);
        this.cardHolderId = cardHolderId;
        this.cardHolderName = cardHolderName;
        this.bankCode = bankCode;
        this.cardInfoList = cardInfoList;
        this.isCardListExists = isCardListExists;
    }

    public static CardholderGetCardListResultDTO newInstanceOfSuccess(
            String cardHolderId,
            String cardHolderName,
            String bankCode,
            List<CardInfo> cardInfoList) {
        boolean isCardListExists = cardInfoList != null && !cardInfoList.isEmpty();
        return new CardholderGetCardListResultDTO(
                true, isCardListExists, null, cardHolderId, cardHolderName, bankCode, cardInfoList);
    }

    public static CardholderGetCardListResultDTO newInstanceOfFailure() {
        return new CardholderGetCardListResultDTO(false, false, null, "", "", "", Collections.emptyList());
    }

    public static CardholderGetCardListResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new CardholderGetCardListResultDTO(false, false, e, "", "", "", Collections.emptyList());
    }
}
