package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.controller.request.mfa.MfaCommonRequest;
import com.cherri.acs_portal.controller.request.mfa.MfaVerifyRequest;
import com.cherri.acs_portal.controller.validator.impl.MfaRegisterValidator;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.MailDto;
import com.cherri.acs_portal.dto.mfa.MfaRegisterRequestDto;
import com.cherri.acs_portal.dto.mfa.MfaVerifyRequestDto;
import com.cherri.acs_portal.dto.usermanagement.UserAccountDTO;
import com.cherri.acs_portal.service.MailService;
import com.cherri.acs_portal.service.MimaPolicyService;
import com.cherri.acs_portal.service.UserManagementService;
import com.cherri.acs_portal.service.mfa.MfaOtpRecordService;
import com.cherri.acs_portal.service.mfa.MfaRegisterService;
import com.cherri.acs_portal.service.mfa.MfaVerifyService;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.commons.utils.RandomUtils;
import ocean.acs.models.dao.UserAccountDAO;
import ocean.acs.models.data_object.portal.UserAccountDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * MFA Facade
 *
 * @author Alan Chen
 */
@Slf4j
@Service
public class MfaFacade {

    private final boolean debugMode;

    private final String mfaOtpMailFrom;
    private final String mfaOtpMailSubject;
    private final Integer mfaOtpLength;

    private final UserManagementService userManagementService;
    private final MailService mailService;
    private final MfaRegisterService mfaRegisterService;
    private final MfaVerifyService mfaVerifyService;
    private final MfaRegisterValidator mfaRegisterValidator;
    private final MfaOtpRecordService mfaOtpRecordService;
    private final MimaPolicyService mimaPolicyService;
    private final UserAccountDAO userAccountDAO;

    private final HttpSession session;

    @Autowired
    public MfaFacade(
        @Value("${mfa.otp.code.debug.mode: false}") boolean debugMode,
        @Value("${mfa.otp.mail.from}") String mfaOtpMailFrom,
        @Value("${mfa.otp.mail.subject}") String mfaOtpMailSubject,
        @Value("${mfa.otp.code.length: 7}") Integer mfaOtpLength,
        UserManagementService userManagementService,
        MailService mailService, MfaRegisterService mfaRegisterService,
        MfaVerifyService mfaVerifyService,
        MfaRegisterValidator mfaRegisterValidator,
        MfaOtpRecordService mfaOtpRecordService,
        MimaPolicyService mimaPolicyService, UserAccountDAO userAccountDAO,
        HttpSession session) {
        this.debugMode = debugMode;
        this.mfaOtpMailFrom = mfaOtpMailFrom;
        this.mfaOtpMailSubject = mfaOtpMailSubject;
        this.mfaOtpLength = mfaOtpLength;
        this.userManagementService = userManagementService;
        this.mailService = mailService;
        this.mfaRegisterService = mfaRegisterService;
        this.mfaVerifyService = mfaVerifyService;
        this.mfaRegisterValidator = mfaRegisterValidator;
        this.mfaOtpRecordService = mfaOtpRecordService;
        this.mimaPolicyService = mimaPolicyService;
        this.userAccountDAO = userAccountDAO;
        this.session = session;
    }

    public ApiResponse<Byte[]> register(MfaCommonRequest request) {
        try {
            if (registerParameterIsNotValid(request)) {
                return new ApiResponse<>(ResultStatus.ILLEGAL_ARGUMENT);
            }

            MfaRegisterRequestDto requestDto = MfaRegisterRequestDto.valueOf(request);

            Byte[] qrCodeByteArray = mfaRegisterService.isRegistered(requestDto)
              ? mfaRegisterService.getExistQrCode(requestDto)
              : mfaRegisterService.register(requestDto);

            return new ApiResponse<>(qrCodeByteArray);
        } catch (OceanException oe) {
            return new ApiResponse<>(oe.getResultStatus());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ApiResponse<>(ResultStatus.SERVER_ERROR);
        }
    }

    private boolean registerParameterIsNotValid(MfaCommonRequest request) {
        return !mfaRegisterValidator.isValid(request);
    }

    public ApiResponse<Boolean> verify(MfaVerifyRequestDto requestDto) {
        try {
            UserAccountDO userAccountDO =
                userAccountDAO.findByIssuerBankIdAndAccountAndDeleteFlagFalse(
                    requestDto.getIssuerBankId(), requestDto.getAccount())
                    .orElseThrow(() -> new OceanException(ResultStatus.SERVER_ERROR, "cannot find userAccount"));

            if (mimaPolicyService.isAccountLocked(userAccountDO, true)) {
                throw new OceanException(ResultStatus.ACCOUNT_LOCKED);
            }

            Boolean isVerified = mfaVerifyService.verify(requestDto);
            session.setAttribute(SessionAttributes.IS_MFA_VERIFIED, isVerified);

            if (!isVerified) {
                userAccountDO.setVerifyOtpCount(userAccountDO.getVerifyOtpCount() + 1);
                userAccountDAO.save(userAccountDO);
                // 驗證失敗再檢查看看 account lock
                if (mimaPolicyService.isAccountLocked(userAccountDO, true)) {
                    throw new OceanException(ResultStatus.ACCOUNT_LOCKED);
                }
                return new ApiResponse<>(ResultStatus.MFA_OTP_INCORRECT);
            } else {
                userAccountDO.setVerifyOtpCount(0);
                userAccountDO.setSendOtpCount(0);
                userAccountDAO.save(userAccountDO);
                return new ApiResponse<>(true);
            }
        } catch (OceanException oe) {
            return new ApiResponse<>(oe.getResultStatus());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ApiResponse<>(ResultStatus.SERVER_ERROR);
        }
    }

    public ApiResponse<Boolean> send(MfaCommonRequest request) {
        Optional<UserAccountDO> userAccountOptional = userAccountDAO
            .findByIssuerBankIdAndAccountAndDeleteFlagFalse(request.getIssuerBankId(),
                request.getAccount());

        if (userAccountOptional.isPresent()) {
            UserAccountDO userAccountDO = userAccountOptional.get();
            // check lock
            boolean accountLocked = mimaPolicyService
                .isAccountLocked(userAccountDO);
            if (accountLocked) {
                throw new OceanException(ResultStatus.ACCOUNT_LOCKED);
            }
            // send otp
            String otpCodeStr = getOtpCode();
            mfaOtpRecordService.save(request.getIssuerBankId(), request.getAccount(), otpCodeStr);
            sendMail(UserAccountDTO.valueOf(userAccountDO), otpCodeStr);
            // update send otp count
            userAccountDO.setSendOtpCount(userAccountDO.getSendOtpCount() + 1);
            userAccountDAO.save(userAccountDO);
        }

        return new ApiResponse<>(true);
    }

    private void sendMail(UserAccountDTO userAccount, String otpCodeStr) {
        String email = userAccount.getEmail();
        String userName = userAccount.getName();

        MailDto mailDto = new MailDto();
        mailDto.setFrom(mfaOtpMailFrom);
        mailDto.setTo(email);
        mailDto.setSubject(mfaOtpMailSubject);
        mailDto.setSentDate(new Date());

        Map<String, Object> paramMap = new HashMap<>(3);
        paramMap.put("otpCode", otpCodeStr);
        paramMap.put("name", userName);
        paramMap.put("mail", email);

        String template = mailService.getMailTemplateString("SIMPLE_OTP", paramMap);

        mailDto.setText(template);
        mailService.multipleSendHtmlMail(Collections.singletonList(mailDto));
    }

    private String getOtpCode() {
        int maxNum = generateMaxNumberByLength();
        if (debugMode) {
            return Integer.toString(maxNum);
        }
        return String.format("%0" + mfaOtpLength + "d", RandomUtils.generateRandom(maxNum));
    }

    private int generateMaxNumberByLength() {
        return Integer.parseInt(String.format("%" + mfaOtpLength + "s", "").replace(' ', '9'));
    }
}
