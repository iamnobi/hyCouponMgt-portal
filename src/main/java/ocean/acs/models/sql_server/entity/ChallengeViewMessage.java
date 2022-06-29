package ocean.acs.models.sql_server.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.ChallengeViewMessageCategory;
import ocean.acs.models.data_object.entity.ChallengeViewMessageDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CHALLENGE_VIEW_MESSAGE)
public class ChallengeViewMessage extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_ID)
    private Long id;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_CATEGORY)
    @Enumerated(EnumType.STRING)
    private ChallengeViewMessageCategory category;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_LANGUAGE_CODE)
    private String languageCode;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_MAIN_BODY_TITLE)
    private String mainBodyTitle;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_MAIN_BODY_MESSAGE)
    private String mainBodyMessage;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_NPA_MAIN_BODY_MESSAGE)
    private String npaMainBodyMessage;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_WEB_REMARK_BODY_MESSAGE)
    private String webRemarkBodyMessage;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_WEB_REMARK_BODY_COLOR)
    private String webRemarkBodyColor;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_VERIFY_INPUT_PLACEHOLDER)
    private String verifyInputPlaceholder;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_APP_REMARK_BODY_MESSAGE)
    private String appRemarkBodyMessage;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_BTN_BODY_SUBMIT_BUTTON)
    private String btnBodySubmitButton;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_BTN_BODY_PHONE_ERROR_BUTTON)
    private String btnBodyPhoneErrorButton;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_BTN_BODY_OTP_RESEND_BUTTON)
    private String btnBodyOtpResendButton;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_PHONE_ERROR_BODY_MESSAGE)
    private String phoneErrorBodyMessage;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_PHONE_ERROR_BODY_SYMBOL)
    private Boolean phoneErrorBodySymbol;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_FOOTER_LABEL_1)
    private String footerLabel1;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_FOOTER_MESSAGE_1)
    private String footerMessage1;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_FOOTER_LABEL_2)
    private String footerLabel2;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_FOOTER_MESSAGE_2)
    private String footerMessage2;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_BTN_BODY_CANCEL_BUTTON)
    private String btnBodyCancelButton;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_VERIFY_FAILED_MESSAGE)
    private String verifyFailedMessage;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_RESEND_MESSAGE)
    private String resendMessage;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_MESSAGE_NOT_RESEND_MESSAGE)
    private String notResendMessage;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public ChallengeViewMessage(
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
        this.footerLabel1 = footerLabel1;
        this.footerMessage1 = footerMessage1;
        this.footerLabel2 = footerLabel2;
        this.footerMessage2 = footerMessage2;
        this.verifyFailedMessage = verifyFailedMessage;
        this.resendMessage = resendMessage;
        this.notResendMessage = notResendMessage;
        this.auditStatus = auditStatus;
    }

    public static ChallengeViewMessage valueOf(ChallengeViewMessageDO d) {
        return new ChallengeViewMessage(
                d.getId(),
                d.getIssuerBankId(),
                d.getCategory(),
                d.getLanguageCode(),
                d.getMainBodyTitle(),
                d.getMainBodyMessage(),
                d.getNpaMainBodyMessage(),
                d.getWebRemarkBodyMessage(),
                d.getWebRemarkBodyColor(),
                d.getVerifyInputPlaceholder(),
                d.getAppRemarkBodyMessage(),
                d.getBtnBodySubmitButton(),
                d.getBtnBodyPhoneErrorButton(),
                d.getBtnBodyOtpResendButton(),
                d.getBtnBodyCancelButton(),
                d.getPhoneErrorBodyMessage(),
                d.getPhoneErrorBodySymbol(),
                d.getFooterLabel1(),
                d.getFooterMessage1(),
                d.getFooterLabel2(),
                d.getFooterMessage2(),
                d.getVerifyFailedMessage(),
                d.getResendMessage(),
                d.getNotResendMessage(),
                d.getAuditStatus(),
                d.getCreator(),
                d.getCreateMillis(),
                d.getUpdater(),
                d.getUpdateMillis(),
                d.getDeleteFlag(),
                d.getDeleter(),
                d.getDeleteMillis());
    }
}
