package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ChallengeViewMessageCategory;
import ocean.acs.models.data_object.entity.ChallengeViewMessageDO;
import ocean.acs.models.data_object.entity.ChallengeViewOtpSettingDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeViewMessageDTO extends AuditableDTO {

    /** 語系 */
    @NotBlank(message = "{column.notempty}")
    @Pattern(regexp = "[a-z]{2,3}-[a-z]{2,3}(-[a-z]{2,3})?", flags = CASE_INSENSITIVE)
    private String languageCode;

    /** 手機號碼確認驗證畫面 */
    @Valid public VerifyPage phoneVerifyPage;
    /** 簡訊驗證碼畫面 */
    @Valid public VerifyPage otpVerifyPage;

    private AuditStatus auditStatus;
    private String user;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    /** 驗證畫面 */
    @Data
    @NoArgsConstructor
    public static class VerifyPage {

        @NotNull(message = "{column.notempty}")
        private Long id;

        /** 驗證畫面類型 (phoneVerifyPage/optVerifyPage) */
        @NotBlank(message = "{column.notempty}")
        private String category;
        /** 主訊息標題 */
        @NotBlank(message = "{column.notempty}")
        private String mainBodyTitle;
        /** PA主訊息內文 */
        @NotBlank(message = "{column.notempty}")
        private String mainBodyMessage;
        /** NPA主訊息內文 */
        @NotBlank(message = "{column.notempty}")
        private String npaMainBodyMessage;
        /** Web附註訊息 */
        @NotBlank(message = "{column.notempty}")
        private String webRemarkBodyMessage;
        /** Web附註訊息 顏色 */
        @NotBlank(message = "{column.notempty}")
        private String webRemarkBodyColor;
        /** Web附註訊息 提示文字 */
        @NotBlank(message = "{column.notempty}")
        private String webRemarkBodyPlaceholder;
        /** 輸入框提示文字 */
        private String verifyInputPlaceholder = "";
        /** APP附註訊息 */
        @NotBlank(message = "{column.notempty}")
        private String appRemarkBodyMessage;

        /** 發送驗證碼按鍵文字 */
        @NotBlank(message = "{column.notempty}")
        private String btnBodySubmitButton;
        /** 手機號碼錯誤按鍵文字 */
        @NotBlank(message = "{column.notempty}")
        private String btnBodyPhoneErrorButton;
        /** 重新發送驗證碼按鍵文字 */
        @NotBlank(message = "{column.notempty}")
        private String btnBodyOtpResendButton;
        /** 交易取消按鈕 */
        private String btnBodyCancelButton;

        /** 手機號碼錯誤訊息 */
        @NotBlank(message = "{column.notempty}")
        private String phoneErrorBodyMessage;

        @NotNull(message = "{column.notempty}")
        private Boolean phoneErrorBodySymbol;

        /** 置底資訊標題1 */
        @NotBlank(message = "{column.notempty}")
        private String footerLabel1;
        /** 置底資訊內文1 */
        @NotBlank(message = "{column.notempty}")
        private String footerMessage1;
        /** 置底資訊標題2 */
        @NotBlank(message = "{column.notempty}")
        private String footerLabel2;
        /** 置底資訊內文2 */
        @NotBlank(message = "{column.notempty}")
        private String footerMessage2;

        private AuditStatus auditStatus;

        /** OTP驗證設定 */
        @NotBlank(message = "{column.notempty}")
        private OtpSetting otpVerifySetting;

        /** Verify cancel button by category = optVerifyPage */
        public void setBtnBodyCancelButton(String btnBodyCancelButton) throws NullPointerException {
            if (StringUtils.isBlank(btnBodyCancelButton)
                    && ChallengeViewMessageCategory.otpVerifyPage.name().equals(this.category)) {
                throw new NullPointerException("Missing argument : cancelButton");
            }
            this.btnBodyCancelButton = btnBodyCancelButton;
        }

        public static VerifyPage valueOfPhoneVerifyPage(
                ChallengeViewMessageDO challengeViewMessage) {
            VerifyPage phoneVerifyPage = new VerifyPage();
            BeanUtils.copyProperties(challengeViewMessage, phoneVerifyPage);
            phoneVerifyPage.setAuditStatus(
                    AuditStatus.getStatusBySymbol(challengeViewMessage.getAuditStatus()));
            return phoneVerifyPage;
        }

        public static VerifyPage valueOfOtpVerifyPage(
                ChallengeViewMessageDO challengeViewMessage, VerifyPage.OtpSetting otpSetting) {

            VerifyPage otpVerifyPage = new VerifyPage();
            BeanUtils.copyProperties(challengeViewMessage, otpVerifyPage);
            otpVerifyPage.setOtpVerifySetting(otpSetting);
            otpVerifyPage.setAuditStatus(
                    AuditStatus.getStatusBySymbol(challengeViewMessage.getAuditStatus()));
            return otpVerifyPage;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
        }

        /** OTP驗證設定 */
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OtpSetting {

            @NotNull(message = "{column.notempty}")
            private Long id;

            @NotNull(message = "{column.notempty}")
            private Integer maxResend;

            @NotNull(message = "{column.notempty}")
            private Integer maxVerify;

            @NotBlank(message = "{column.notempty}")
            private String verifyFailMessage;

            @NotBlank(message = "{column.notempty}")
            private String resendMessage;

            @NotBlank(message = "{column.notempty}")
            private String notResendMessage;

            private AuditStatus auditStatus;

            public static OtpSetting valueOf(ChallengeViewMessageDO msgDO, ChallengeViewOtpSettingDO otpSettingDO) {
                return new OtpSetting(
                        otpSettingDO.getId(),
                        otpSettingDO.getMaxResendTimes(),
                        otpSettingDO.getMaxChallengeTimes(),
                        msgDO.getVerifyFailedMessage(),
                        msgDO.getResendMessage(),
                        msgDO.getNotResendMessage(),
                        AuditStatus.getStatusBySymbol(otpSettingDO.getAuditStatus()));
            }

            @Override
            public String toString() {
                return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
            }
        }
    }

    @Override
    @JsonIgnore
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_CHALLENGE_VIEW;
    }
}
