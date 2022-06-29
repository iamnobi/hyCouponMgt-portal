
package com.cherri.acs_portal.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MimaPolicyUpdateRequest {

    private Long id;
    /**
     * 登入重試次數
     */
    private Integer loginRetryCount;
    /**
     * 更新週期天數
     */
    private Integer mimaFreshInterval;
    /**
     * 最長未使用期間天數
     */
    private Integer accountMaxIdleDay;
    /**
     * 最長長度
     */
    private Integer mimaMaxLength;
    /**
     * 最短長度
     */
    private Integer mimaMinLength;
    /**
     * 最少英文字元數量
     */
    private Integer mimaAlphabetCount;
    /**
     * 最少小寫英文數量
     */
    private Integer mimaMinLowerCase;
    /**
     * 最少大寫英文數量
     */
    private Integer mimaMinUpperCase;
    /**
     * 最少數字數量
     */
    private Integer mimaMinNumeric;
    /**
     * 最少特殊字元數量
     */
    private Integer mimaMinSpecialChar;
    /**
     * 不可重複代數
     */
    private Integer mimaHistoryDupCount;
}
