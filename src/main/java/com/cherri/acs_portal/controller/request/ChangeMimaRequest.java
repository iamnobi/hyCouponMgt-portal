package com.cherri.acs_portal.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 變更密碼請求 Change Mima Request
 *
 * @author Alan Chen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMimaRequest {

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
}
