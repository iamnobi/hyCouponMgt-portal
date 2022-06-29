package com.cherri.acs_portal.dto;

import com.cherri.acs_portal.controller.request.ChangeMimaRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Change Mima Request Dto
 *
 * @author Alan Chen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMimaRequestDto {

    /**
     * 銀行代碼
     */
    private Long issuerBankId;
    /**
     * 使用者帳號
     */
    private String account;
    /**
     * 舊密碼
     */
    private String oldMima;
    /**
     * 新密碼
     */
    private String newMima;
    /**
     * 新密碼確認
     */
    private String mimaConfirm;

    /**
     * Transfer request to dto
     *
     * @param request Change Mima Request
     * @return Change Mima Request Dto
     */
    public static ChangeMimaRequestDto valueOf(ChangeMimaRequest request) {
        ChangeMimaRequestDto dto = new ChangeMimaRequestDto();
        dto.setIssuerBankId(request.getIssuerBankId());
        dto.setAccount(request.getAccount());
        dto.setOldMima(request.getOldMima());
        dto.setNewMima(request.getNewMima());
        return dto;
    }
}
