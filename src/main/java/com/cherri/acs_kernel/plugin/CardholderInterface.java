package com.cherri.acs_kernel.plugin;

import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderGetAuthChannelInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderGetCardListInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderGetInfoInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderInitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.invoke.CardholderVerifyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderGetAuthChannelResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderGetCardListResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderGetCardholderInfoResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderInitializeResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderVerifyResultDTO;

public interface CardholderInterface extends IPlugin {

    CardholderInitializeResultDTO initialize(
        CardholderInitializeInvokeDTO cardholderInitializeInvokeDTO);

    CardholderGetCardListResultDTO getCardList(
        CardholderGetCardListInvokeDTO cardholderGetCardListInvokeDTO);

    CardholderGetCardholderInfoResultDTO getCardHolderInfo(
        CardholderGetInfoInvokeDTO cardholderGetInfoInvokeDTO);

    CardholderGetAuthChannelResultDTO getChallengeChannelInfo(
        CardholderGetAuthChannelInvokeDTO cardholderGetAuthChannelInvokeDTO);

    CardholderVerifyResultDTO verifyCard(CardholderVerifyInvokeDTO cardholderVerifyInvokeDTO);


}
