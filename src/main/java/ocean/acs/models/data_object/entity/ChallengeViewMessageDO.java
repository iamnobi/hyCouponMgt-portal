package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.ChallengeViewMessageCategory;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ChallengeViewMessageDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private ChallengeViewMessageCategory category;
    private String languageCode;

    private String mainBodyTitle;
    private String mainBodyMessage;
    private String npaMainBodyMessage;
    private String webRemarkBodyMessage;
    private String webRemarkBodyColor;
    private String verifyInputPlaceholder;
    private String appRemarkBodyMessage;
    private String btnBodySubmitButton;
    private String btnBodyPhoneErrorButton;
    private String btnBodyOtpResendButton;
    private String btnBodyCancelButton;
    private String phoneErrorBodyMessage;
    private Boolean phoneErrorBodySymbol;
    private String footerLabel1;
    private String footerMessage1;
    private String footerLabel2;
    private String footerMessage2;

    private String verifyFailedMessage;
    private String resendMessage;
    private String notResendMessage;

    private String auditStatus;

    public ChallengeViewMessageDO(
            Long id,
            Long issuerBankId,
            ChallengeViewMessageCategory category,
            String languageCode,
            String mainBodyTitle,
            String mainBodyMessage,
            String npaMainBodyMessage,
            String webRemarkBodyMessage,
            String webRemarkBodyColor,
            String verifyInputPlaceholder,
            String appRemarkBodyMessage,
            String btnBodySubmitButton,
            String btnBodyPhoneErrorButton,
            String btnBodyOtpResendButton,
            String btnBodyCancelButton,
            String phoneErrorBodyMessage,
            Boolean phoneErrorBodySymbol,
            String footerLabel1,
            String footerMessage1,
            String footerLabel2,
            String footerMessage2,
            String verifyFailedMessage,
            String resendMessage,
            String notResendMessage,
            String auditStatus,
            String creator,
            Long createMillis,
            String updater,
            Long updateMillis,
            Boolean deleteFlag,
            String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.category = category;
        this.languageCode = languageCode;
        this.mainBodyTitle = mainBodyTitle;
        this.mainBodyMessage = mainBodyMessage;
        this.npaMainBodyMessage = npaMainBodyMessage;
        this.webRemarkBodyMessage = webRemarkBodyMessage;
        this.webRemarkBodyColor = webRemarkBodyColor;
        this.verifyInputPlaceholder = verifyInputPlaceholder;
        this.appRemarkBodyMessage = appRemarkBodyMessage;
        this.btnBodySubmitButton = btnBodySubmitButton;
        this.btnBodyPhoneErrorButton = btnBodyPhoneErrorButton;
        this.btnBodyOtpResendButton = btnBodyOtpResendButton;
        this.btnBodyCancelButton = btnBodyCancelButton;
        this.phoneErrorBodyMessage = phoneErrorBodyMessage;
        this.phoneErrorBodySymbol = phoneErrorBodySymbol;
        this.btnBodyCancelButton = btnBodyCancelButton;
        this.footerLabel1 = footerLabel1;
        this.footerMessage1 = footerMessage1;
        this.footerLabel2 = footerLabel2;
        this.footerMessage2 = footerMessage2;
        this.verifyFailedMessage = verifyFailedMessage;
        this.resendMessage = resendMessage;
        this.notResendMessage = notResendMessage;
        this.auditStatus = auditStatus;
    }

    public static ChallengeViewMessageDO valueOf(
            ocean.acs.models.oracle.entity.ChallengeViewMessage e) {
        return new ChallengeViewMessageDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getCategory(),
                e.getLanguageCode(),
                e.getMainBodyTitle(),
                e.getMainBodyMessage(),
                e.getNpaMainBodyMessage(),
                e.getWebRemarkBodyMessage(),
                e.getWebRemarkBodyColor(),
                e.getVerifyInputPlaceholder(),
                e.getAppRemarkBodyMessage(),
                e.getBtnBodySubmitButton(),
                e.getBtnBodyPhoneErrorButton(),
                e.getBtnBodyOtpResendButton(),
                e.getBtnBodyCancelButton(),
                e.getPhoneErrorBodyMessage(),
                e.getPhoneErrorBodySymbol(),
                e.getFooterLabel1(),
                e.getFooterMessage1(),
                e.getFooterLabel2(),
                e.getFooterMessage2(),
                e.getVerifyFailedMessage(),
                e.getResendMessage(),
                e.getNotResendMessage(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }

    public static ChallengeViewMessageDO valueOf(
            ocean.acs.models.sql_server.entity.ChallengeViewMessage e) {
        return new ChallengeViewMessageDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getCategory(),
                e.getLanguageCode(),
                e.getMainBodyTitle(),
                e.getMainBodyMessage(),
                e.getNpaMainBodyMessage(),
                e.getWebRemarkBodyMessage(),
                e.getWebRemarkBodyColor(),
                e.getVerifyInputPlaceholder(),
                e.getAppRemarkBodyMessage(),
                e.getBtnBodySubmitButton(),
                e.getBtnBodyPhoneErrorButton(),
                e.getBtnBodyOtpResendButton(),
                e.getBtnBodyCancelButton(),
                e.getPhoneErrorBodyMessage(),
                e.getPhoneErrorBodySymbol(),
                e.getFooterLabel1(),
                e.getFooterMessage1(),
                e.getFooterLabel2(),
                e.getFooterMessage2(),
                e.getVerifyFailedMessage(),
                e.getResendMessage(),
                e.getNotResendMessage(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
}
