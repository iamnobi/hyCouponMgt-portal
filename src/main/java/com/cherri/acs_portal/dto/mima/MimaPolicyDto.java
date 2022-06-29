
package com.cherri.acs_portal.dto.mima;

import com.cherri.acs_portal.controller.request.MimaPolicyCreateRequest;
import com.cherri.acs_portal.controller.request.MimaPolicyUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.models.data_object.entity.MimaPolicyDO;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
public class MimaPolicyDto {

    /**
     * Id
     */
    private Long id;

    /**
     * 銀行代碼
     */
    private Long issuerBankId;
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

    /**
     * Entity to Dto
     */
    public static MimaPolicyDto valueOf(MimaPolicyDO mimaPolicy) {
        MimaPolicyDto dto = new MimaPolicyDto();
        dto.setId(mimaPolicy.getId());
        dto.setIssuerBankId(mimaPolicy.getIssuerBankId());

        dto.setLoginRetryCount(mimaPolicy.getLoginRetryCount());
        dto.setMimaFreshInterval(mimaPolicy.getMimaFreshInterval());
        dto.setAccountMaxIdleDay(mimaPolicy.getAccountMaxIdleDay());
        dto.setMimaMinLength(mimaPolicy.getMimaMinLength());
        dto.setMimaMaxLength(mimaPolicy.getMimaMaxLength());
        dto.setMimaAlphabetCount(mimaPolicy.getMimaAlphabetCount());
        dto.setMimaMinUpperCase(mimaPolicy.getMimaMinUpperCase());
        dto.setMimaMinLowerCase(mimaPolicy.getMimaMinLowerCase());
        dto.setMimaMinNumeric(mimaPolicy.getMimaMinNumeric());
        dto.setMimaMinSpecialChar(mimaPolicy.getMimaMinSpecialChar());
        dto.setMimaHistoryDupCount(mimaPolicy.getMimaHistoryDupCount());
        return dto;
    }

    /**
     * Request to Dto
     */
    public static MimaPolicyDto valueOf(MimaPolicyUpdateRequest request, long issuerBankId) {
        MimaPolicyDto dto = new MimaPolicyDto();
        dto.setId(request.getId());
        dto.setIssuerBankId(issuerBankId);
        dto.setLoginRetryCount(request.getLoginRetryCount());
        dto.setMimaFreshInterval(request.getMimaFreshInterval());
        dto.setAccountMaxIdleDay(request.getAccountMaxIdleDay());
        dto.setMimaMinLength(request.getMimaMinLength());
        dto.setMimaMaxLength(request.getMimaMaxLength());
        dto.setMimaAlphabetCount(request.getMimaAlphabetCount());
        dto.setMimaMinUpperCase(request.getMimaMinUpperCase());
        dto.setMimaMinLowerCase(request.getMimaMinLowerCase());
        dto.setMimaMinNumeric(request.getMimaMinNumeric());
        dto.setMimaMinSpecialChar(request.getMimaMinSpecialChar());
        dto.setMimaHistoryDupCount(request.getMimaHistoryDupCount());
        return dto;
    }

    /**
     * 是否符合密碼原則規範
     *
     * @param mima 密碼
     * @return 是否符合規範
     */
    public boolean isMatchPattern(String mima) {
        return this.minAlphabetIsValid(mima)
          && this.minLengthIsValid(mima) && this.maxLengthIsValid(mima)
          && this.minLowerCaseIsValid(mima) && this.minUpperCaseIsValid(mima)
          && this.minSpecialCharIsValid(mima) && this.minNumericIsValid(mima);
    }

    public boolean minAlphabetIsValid(String mima) {
        return this.mimaAlphabetCount <= calAlphabetCount(mima);
    }

    public boolean minLengthIsValid(String mima) {
        return this.mimaMinLength <= mima.length();
    }

    public boolean maxLengthIsValid(String mima) {
        return this.mimaMaxLength >= mima.length();
    }

    public boolean minLowerCaseIsValid(String mima) {
        return this.mimaMinLowerCase <= calLowerCaseCount(mima);
    }

    public boolean minUpperCaseIsValid(String mima) {
        return this.mimaMinUpperCase <= calUpperCaseCount(mima);
    }

    public boolean minSpecialCharIsValid(String mima) {
        return this.mimaMinSpecialChar <= calSpecialCharCount(mima);
    }

    public boolean minNumericIsValid(String mima) {
        return this.mimaMinNumeric <= calNumericCount(mima);
    }

    private int calAlphabetCount(String str) {
        if (StringUtils.isBlank(str))
            return 0;
        int count = 0;
        for (char c : str.toCharArray()) {
            if (Character.isAlphabetic(c))
                count++;
        }
        log.debug("Alphabet char count: {}", count);
        return count;
    }

    private int calLowerCaseCount(String str) {
        if (StringUtils.isBlank(str))
            return 0;
        int count = 0;
        for (char c : str.toCharArray()) {
            if (Character.isLowerCase(c))
                count++;
        }
        log.debug("Lowercase char count: {}", count);
        return count;
    }

    private int calUpperCaseCount(String str) {
        if (StringUtils.isBlank(str))
            return 0;
        int count = 0;
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c))
                count++;
        }
        log.debug("Uppercase char count: {}", count);
        return count;
    }

    private int calSpecialCharCount(String str) {
        if (StringUtils.isBlank(str))
            return 0;
        int count = 0;
        String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
        for (char c : str.toCharArray()) {
            if (specialChars.contains(Character.toString(c)))
                count++;
        }
        log.debug("Special char count: {}", count);
        return count;
    }

    private int calNumericCount(String str) {
        if (StringUtils.isBlank(str))
            return 0;
        int count = 0;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c))
                count++;
        }
        log.debug("Number count: {}", count);
        return count;
    }
}
