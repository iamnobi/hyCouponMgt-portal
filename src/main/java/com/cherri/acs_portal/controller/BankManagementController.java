package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.controller.request.BankIdQueryDTO;
import com.cherri.acs_portal.controller.request.OtpSendingSettingUpdateReqDTO;
import com.cherri.acs_portal.controller.request.OtpSendingUrlVerifyReqDto;
import com.cherri.acs_portal.controller.response.BankDataKeyResDTO;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.controller.response.OtpSendingSettingResDTO;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerBankAdminDto;
import com.cherri.acs_portal.dto.bank.IssuerBankAdminListDto;
import com.cherri.acs_portal.dto.bank.IssuerBankDto;
import com.cherri.acs_portal.dto.bank.IssuerHandingFeeQueryResultDto;
import com.cherri.acs_portal.dto.bank.IssuerHandingFeeUpdateDto;
import com.cherri.acs_portal.dto.bank.IssuerLogoUpdateDto;
import com.cherri.acs_portal.dto.bank.IssuerQueryDto;
import com.cherri.acs_portal.dto.bank.IssuerTradingChannelQueryResultDto;
import com.cherri.acs_portal.dto.bank.IssuerTradingChannelUpdateDto;
import com.cherri.acs_portal.dto.bank.OtpSendingKeyUploadDto;
import com.cherri.acs_portal.dto.bank.OtpSendingSettingDto;
import com.cherri.acs_portal.facade.BankManagementFacade;
import com.cherri.acs_portal.util.FileUtils;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.AbstractMap;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 銀行管理
 */
@Log4j2
@RestController
@RequestMapping("/auth/bank-management")
public class BankManagementController extends ContextProvider {

    private static final UrlValidator HTTPS_URL_VALIDATOR = new UrlValidator(new String[]{"https"});
    private static final long FILE_SIZE_LIMIT_BYTES = 5 * 1024L * 1024L;  //5MB.

    @Autowired
    private BankManagementFacade facade;

    /**
     * 取得全部銀行列表（含組織）
     */
    @GetMapping(value = "/issuer-banks-org")
    @Secured({"ROLE_BANK_MANAGE_QUERY", "ROLE_ACCESS_MULTI_BANK"})
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_LIST)
    public ApiResponse getIssuerBankAndOrgList() {
        List<IssuerBankDto> list = facade.getIssuerBankAndOrgList();
        return new ApiResponse<>(list);
    }

    /**
     * 查詢銀行（分頁）
     */
    @GetMapping("/issuer-banks")
    @Secured({"ROLE_BANK_MANAGE_QUERY", "ROLE_ACCESS_MULTI_BANK"})
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_LIST)
    public ApiResponse getIssuerBankList() {
        List<IssuerBankDto> resultPage = facade.getIssuerBankList();
        return new ApiResponse<>(resultPage);
    }

    /**
     * 查詢銀行 by id
     */
    @GetMapping("/issuer-bank/{id}")
    @PreAuthorize("hasRole('ROLE_BANK_MANAGE_QUERY') && hasRole('ROLE_ACCESS_MULTI_BANK') && "
        + "@permissionService.checkAccessMultipleBank(authentication.principal,#id)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_GET)
    public ApiResponse getIssuerBankById(@PathVariable Long id) {
        Optional<IssuerBankDto> result = facade.getIssuerBankById(id);
        return result.map(ApiResponse::valueOf).orElse(new ApiResponse(ResultStatus.NO_SUCH_DATA));
    }

    /**
     * 新增銀行
     */
    @PostMapping(value = "/issuer-bank", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_BANK_MANAGE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_CREATE)
    public ApiResponse createIssuerBank(@Valid @RequestBody IssuerBankDto issuerBankDto) {
        issuerBankDto.setUser(getUserAccount());
        //00003 allow control asc/3ds oper id by user
        try {
            log.info("Check Dup Issuer Oper ID");
            Optional<IssuerBankDto> result = facade.getAcsOperatorId(issuerBankDto.getAcsOperatorId());
            if(result.isPresent())
            {
                log.info("Dup Issuer Oper ID:{}",StringUtils.normalizeSpace(issuerBankDto.getAcsOperatorId()));
                return new ApiResponse(ResultStatus.DUPLICATE_DATA_ELEMENT,"Duplicate Acs Operator Id");
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
        return new ApiResponse<>(facade.createIssuerBank(issuerBankDto));

    }

    /**
     * 修改銀行
     */
    @PostMapping(value = "/issuer-bank/update", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_BANK_MANAGE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_MODIFY)
    public ApiResponse updateIssuerBank(@RequestBody IssuerBankDto issuerBankDto) {
        if (issuerBankDto == null
          || issuerBankDto.getId() == null
          || issuerBankDto.getIssuerBankId() == null
          || StringUtils.isEmpty(issuerBankDto.getName())
          || StringUtils.isEmpty(issuerBankDto.getCode())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "Incorrect arguments");
        }

        issuerBankDto.setUser(getUserAccount());
        return new ApiResponse<>(facade.updateIssuerBank(issuerBankDto));
    }

    /**
     * 刪除銀行
     */
    @PostMapping(value = "/issuer-bank/delete")
    @PreAuthorize("hasRole('ROLE_BANK_MANAGE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#deleteDataDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_DELETE)
    public ApiResponse deleteIssuerBank(@RequestBody DeleteDataDTO deleteDataDto) {

        validateDeleteDataDto(deleteDataDto);

        deleteDataDto.setUser(getUserAccount());

        return new ApiResponse<>(facade.deleteIssuerBank(deleteDataDto));
    }

    /**
     * 查詢銀行管理人員（多筆）
     */
    @GetMapping(value = "/issuer-bank/admin-list/{issuerBankId}")
    @PreAuthorize("hasRole('ROLE_BANK_MANAGE_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_ADMIN_LIST)
    public ApiResponse getIssuerBankAdminListById(@PathVariable Long issuerBankId) {
        List<IssuerBankAdminListDto> data = facade.queryIssuerBankAdminList(issuerBankId);
        return new ApiResponse<>(data);
    }

    /**
     * 新增銀行管理人員
     */
    @PostMapping(value = "/issuer-bank/admin")
    @PreAuthorize("hasRole('ROLE_BANK_MANAGE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#adminDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_ADMIN_CREATE)
    public ApiResponse createIssuerBankAdmin(@RequestBody IssuerBankAdminDto adminDto) {
        if (adminDto == null || adminDto.getIssuerBankId() == null
          || adminDto.getBankId() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "Incorrect arguments");
        }

        adminDto.setUser(getUserAccount());
        return new ApiResponse<>(facade.createIssuerBankAdmin(adminDto));
    }

    /**
     * 修改銀行管理人員
     */
    @PostMapping(value = "/issuer-bank/admin/update")
    @PreAuthorize("hasRole('ROLE_BANK_MANAGE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#adminDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_ADMIN_UPDATE)
    public ApiResponse updateIssuerBankAdmin(@Valid @RequestBody IssuerBankAdminDto adminDto) {
        adminDto.setUser(getUserAccount());
        return new ApiResponse<>(facade.updateIssuerBankAdmin(adminDto));
    }

    /**
     * 刪除銀行管理人員
     */
    @PostMapping(value = "/issuer-bank/admin/delete")
    @PreAuthorize("hasRole('ROLE_BANK_MANAGE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#deleteDataDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_ADMIN_DELETE)
    public ApiResponse deleteIssuerBankAdmin(@RequestBody DeleteDataDTO deleteDataDto) {

        validateDeleteDataDto(deleteDataDto);

        deleteDataDto.setUser(getUserAccount());

        return new ApiResponse<>(facade.deleteIssuerBankAdmin(deleteDataDto));
    }

    /**
     * 驗證刪除參數
     */
    private void validateDeleteDataDto(DeleteDataDTO deleteDataDto) {
        if (deleteDataDto == null
          || deleteDataDto.getIssuerBankId() == null
          || deleteDataDto.getId() == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "Incorrect arguments");
        }
    }

    @PostMapping(value = "/trading-channel")
    @PreAuthorize("hasRole('ROLE_BANK_CHANNEL_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerQueryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.TRADING_CHANNEL_LIST)
    public ApiResponse queryIssuerTradingChannel(
      @Valid @RequestBody IssuerQueryDto issuerQueryDto) {
        IssuerTradingChannelQueryResultDto issuerTradingChannelQueryResultDto = facade
          .queryByIssuerBankId(issuerQueryDto);
        return new ApiResponse<>(issuerTradingChannelQueryResultDto);
    }

    @PostMapping(value = "/trading-channel/update")
    @PreAuthorize("hasRole('ROLE_BANK_CHANNEL_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerTradingChannelUpdateDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.TRADING_CHANNEL_UPDATE)
    public ApiResponse updateIssuerTradingChannel(
      @Valid @RequestBody IssuerTradingChannelUpdateDto issuerTradingChannelUpdateDto) {
        issuerTradingChannelUpdateDto.setUser(getUserAccount());
        DataEditResultDTO result = facade.updateIssuerTradingChannel(issuerTradingChannelUpdateDto);
        return new ApiResponse<>(result);
    }

    // Tool bar 上顯示的 issuer bank logo
    @GetMapping(value = "/logo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> queryLogoForToolBar() {
        IssuerQueryDto dto = new IssuerQueryDto();
        dto.setIssuerBankId(getIssuerBankId());
        AbstractMap.SimpleEntry<AuditStatus, byte[]> result =
            facade.queryIssuerLogoByIssuerBankId(dto);

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .header("auditStatus", result.getKey().getSymbol())
            .body(result.getValue());
    }

    @PostMapping(value = "/logo", produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize(
      "hasRole('ROLE_BANK_LOGO_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerQueryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_LOGO_GET)
    public ResponseEntity<byte[]> queryLogo(@RequestBody IssuerQueryDto issuerQueryDto) {
        AbstractMap.SimpleEntry<AuditStatus, byte[]> result =
          facade.queryIssuerLogoByIssuerBankId(issuerQueryDto);

        return ResponseEntity.ok()
          .contentType(MediaType.IMAGE_JPEG)
          .header("auditStatus", result.getKey().getSymbol())
          .body(result.getValue());
    }

    @PostMapping(value = "/logo/update")
    @PreAuthorize("hasRole('ROLE_BANK_LOGO_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_LOGO_UPDATE)
    public ApiResponse updateLogo(@RequestParam MultipartFile image,
      @RequestParam Long issuerBankId) {

        if (image == null) {
            String errMsg = "Failed in update logo, file is null.";
            return new ApiResponse(ResultStatus.INVALID_FORMAT, errMsg);
        }

        // check file extension
        if (!FileUtils.isFileExtensionEqualsAny(image, "jpg", "jpeg", "png")) {
            String errMsg = "Failed in update logo, file extension is not supported, system only support for [jpg].";
            return new ApiResponse(ResultStatus.INVALID_FORMAT, errMsg);
        }

        String filename = FileUtils
          .extractFilename(Objects.requireNonNull(image.getResource().getFilename()));
        if (FileUtils.isFilenameExceededLengthLimit(filename)) {
            throw new OceanException(
              ResultStatus.INVALID_FORMAT, "Exceeds maximum filename length by 255");
        }

        IssuerLogoUpdateDto updateDto = new IssuerLogoUpdateDto();
        updateDto.setImageSize(Math.toIntExact(image.getSize()));
        updateDto.setIssuerBankId(issuerBankId);
        updateDto.setFileName(filename);
        try {
            updateDto.setFileContent(image.getBytes());
        } catch (IOException e) {
            throw new OceanException(ResultStatus.IO_EXCEPTION, "get image file bytes error");
        }
        updateDto.setUser(getUserAccount());
        DataEditResultDTO result = facade.updateLogo(updateDto);
        return new ApiResponse<>(result);
    }

    @PostMapping(value = "/fee")
    @PreAuthorize("hasRole('ROLE_BANK_FEE_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerQueryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_FEE_LIST)
    public ApiResponse queryHandingFee(@Valid @RequestBody IssuerQueryDto issuerQueryDto) {
        IssuerHandingFeeQueryResultDto issuerHandingFeeQueryResultDto = facade
          .queryIssuerHandingFeeByIssuerBankId(issuerQueryDto);
        return new ApiResponse<>(issuerHandingFeeQueryResultDto);
    }

    @PostMapping(value = "/fee/update")
    @PreAuthorize("hasRole('ROLE_BANK_FEE_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerHandingFeeUpdateDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ISSUER_BANK_FEE_MODIFY)
    public ApiResponse updateHandingFee(
      @Valid @RequestBody IssuerHandingFeeUpdateDto issuerHandingFeeUpdateDto) {
        issuerHandingFeeUpdateDto.setUser(getUserAccount());
        DataEditResultDTO result = facade.updateIssuerHandingFee(issuerHandingFeeUpdateDto);
        return new ApiResponse<>(result);
    }

    @GetMapping(value = "/otp/sending/{issuerBankId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.OTP_SENDING_GET)
    @PreAuthorize("hasRole('ROLE_BANK_OTP_SENDING_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    public ApiResponse<OtpSendingSettingResDTO> getOtpSendingSetting(
      @PathVariable("issuerBankId") Long issuerBankId) {
        OtpSendingSettingResDTO response = facade.getOtpSendingSetting(issuerBankId);
        return new ApiResponse<OtpSendingSettingResDTO>(response);
    }

    @PostMapping(value = "/otp/sending/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.OTP_SENDING_MODIFY)
    @PreAuthorize("hasRole('ROLE_BANK_OTP_SENDING_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#request.issuerBankId)")
    public ApiResponse<DataEditResultDTO> updateOtpSendingSetting(
      @Valid @RequestBody OtpSendingSettingUpdateReqDTO request) {
        if (request.getBankEnable().booleanValue()) {
            if (null == request.getBankApiUrl()) {
                return new ApiResponse<>(ResultStatus.INVALID_FORMAT, "Missing argument : url");
            }
            if (!HTTPS_URL_VALIDATOR.isValid(request.getBankApiUrl())) {
                return new ApiResponse<>(ResultStatus.ILLEGAL_URL, "Invalid argument : url");
            }
        }

        OtpSendingSettingDto dto = OtpSendingSettingDto.valueOf(request);
        dto.setUserAccount(super.getUserAccount());
        DataEditResultDTO dataEditResultDto = facade.updateOtpSendingSetting(dto);
        return new ApiResponse<DataEditResultDTO>(dataEditResultDto);
    }

    @PostMapping(value = "/otp/sending/secret-key/upload/{issuerBankId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.OTP_SENDING_MODIFY)
    @PreAuthorize("hasRole('ROLE_BANK_OTP_SENDING_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    public ApiResponse uploadSecretKey(@PathVariable("issuerBankId") Long issuerBankId,
      @RequestParam("secretKey") MultipartFile secretKey,
      @RequestParam("verifyCode") String verifyCode) {
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = IOUtils.toByteArray(secretKey.getInputStream());
        } catch (IOException e) {
            log.warn("[uploadSecretKey] Failed in read secretKey.", e);
            return new ApiResponse<>(ResultStatus.ILLEGAL_KEY, "Missing attachment.");
        }

        Optional<ApiResponse> verifyResult = verifySecretKey(secretKeyBytes, verifyCode);
        if (verifyResult.isPresent()) {
            return verifyResult.get();
        }

        try {
            String secretKeyBase64 = new String(secretKeyBytes);
            OtpSendingKeyUploadDto dto = OtpSendingKeyUploadDto
              .valueOf(issuerBankId, secretKeyBase64);
            dto.setUserAccount(super.getUserAccount());
            DataEditResultDTO dataEditResultDto = facade.uploadJweOrJwsKey(dto);
            return new ApiResponse<>(dataEditResultDto);

        } catch (Exception e) {
            log.error("[uploadSecretKey] Upload secret key failed", e);
            return new ApiResponse<>(ResultStatus.FILE_SAVE_ERROR);
        }
    }

    private Optional<ApiResponse> verifySecretKey(byte[] secretKey, String verifyCode) {
        // Verify by file size
        if (secretKey.length > BankManagementController.FILE_SIZE_LIMIT_BYTES) {
            return Optional
              .of(new ApiResponse<>(ResultStatus.ILLEGAL_FILE_SIZE, "File size limit 5 MB"));
        }
        // Verify by secretKey
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Base64Utils.decode(secretKey);
        } catch (Exception e) {
            return Optional.of(
              new ApiResponse<>(
                ResultStatus.ILLEGAL_KEY, "The secret key must be base64 encoded string"));
        }
        // Verify by checksum
        String sha256Hex = DigestUtils.sha256Hex(secretKeyBytes);
        sha256Hex = sha256Hex.substring(sha256Hex.length() - 10);
        if (!sha256Hex.equalsIgnoreCase(verifyCode)) {
            return Optional.of(
              new ApiResponse<>(ResultStatus.ILLEGAL_FILE_CHECKSUM,
                "Verify code does not match key"));
        }
        // Verify by key size
        if (secretKeyBytes.length != 32) {
            return Optional.of(
              new ApiResponse<>(ResultStatus.ILLEGAL_KEY_SIZE,
                "Secret key size must be 32 characters"));
        }
        return Optional.empty();
    }

    @PostMapping(value = "/otp/sending/rsa/upload/{issuerBankId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.OTP_SENDING_MODIFY)
    @PreAuthorize("hasRole('ROLE_BANK_OTP_SENDING_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    public ApiResponse<DataEditResultDTO> uploadRsaPublicKey(
      @PathVariable("issuerBankId") Long issuerBankId,
      @RequestParam("rsaPublicKey") MultipartFile rsaPublicKey,
      @RequestParam("verifyCode") String verifyCode) {
        byte[] rsaPublicKeyBytes;
        try {
            rsaPublicKeyBytes = IOUtils.toByteArray(rsaPublicKey.getInputStream());
            String publicKeyContent = new String(rsaPublicKeyBytes).replaceAll("\\n", "")
              .replace("-----BEGIN PUBLIC KEY-----", "")
              .replace("-----END PUBLIC KEY-----", "");

            rsaPublicKeyBytes = publicKeyContent.getBytes();
        } catch (IOException e) {
            log.warn("[uploadRsaPublicKey] Failed in read secretKey.", e);
            return new ApiResponse<>(ResultStatus.ILLEGAL_KEY, "Missing attachment.");
        }

        if (rsaPublicKeyBytes.length > BankManagementController.FILE_SIZE_LIMIT_BYTES) {
            return new ApiResponse<>(ResultStatus.ILLEGAL_FILE_SIZE, "File size limit 5 MB");
        }

        try {
            String sha256Hex = DigestUtils.sha256Hex(rsaPublicKeyBytes);
            sha256Hex = sha256Hex.substring(sha256Hex.length() - 10);
            if (!sha256Hex.equalsIgnoreCase(verifyCode)) {
                return new ApiResponse<>(ResultStatus.ILLEGAL_FILE_CHECKSUM,
                  "Verify code does not match key");
            }

            try {
                X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(
                  Base64.getDecoder().decode(rsaPublicKeyBytes));
                RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                  .generatePublic(keySpecX509);
                if (2048 != publicKey.getModulus().bitLength()) {
                    return new ApiResponse<>(ResultStatus.ILLEGAL_KEY_SIZE,
                      "RSA public key size must be 2048 bit");
                }
            } catch (Exception e) {
                return new ApiResponse<>(ResultStatus.ILLEGAL_KEY, "Illegal RSA-Public-Key");
            }

            OtpSendingKeyUploadDto dto = OtpSendingKeyUploadDto
              .valueOf(issuerBankId, rsaPublicKey.getOriginalFilename(),
                rsaPublicKeyBytes);
            dto.setUserAccount(super.getUserAccount());
            DataEditResultDTO dataEditResultDto = facade.uploadJweOrJwsKey(dto);
            return new ApiResponse<DataEditResultDTO>(dataEditResultDto);

        } catch (Exception e) {
            log.error("[uploadRsaPublicKey] Upload RSA public key failed", e);
            return new ApiResponse<>(ResultStatus.FILE_SAVE_ERROR);
        }
    }

    @PostMapping(value = "/otp/sending/url/verify", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Secured("ROLE_BANK_OTP_SENDING_QUERY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.OTP_SENDING_GET)
    public ApiResponse<?> verifyOtpSendingUrl(
      @Valid @RequestBody OtpSendingUrlVerifyReqDto request) {
        boolean isValid = HTTPS_URL_VALIDATOR.isValid(request.getUrl());
        if (isValid) {
            return ApiResponse.SUCCESS_API_RESPONSE;
        } else {
            return new ApiResponse<>(ResultStatus.ILLEGAL_URL);
        }
    }

    @GetMapping("/bank-data-key/{issuerBankId}")
    @PreAuthorize("hasRole('ROLE_BANK_DATA_KEY_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BANK_DATA_KEY_GET)
    public ApiResponse<BankDataKeyResDTO> queryBankDataKey(@PathVariable Long issuerBankId) {
        BankDataKeyResDTO resDTO = facade.queryBankDataKey(issuerBankId);
        return new ApiResponse<>(resDTO);
    }

    @PostMapping("/bank-data-key/generate")
    @PreAuthorize("hasRole('ROLE_BANK_DATA_KEY_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#bankIdQueryDTO.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.BANK_DATA_KEY_CREATE)
    public ApiResponse<BankDataKeyResDTO> createBankDataKey(
      @Valid @RequestBody BankIdQueryDTO bankIdQueryDTO) {
        BankDataKeyResDTO resDTO = facade
          .createBankDataKey(bankIdQueryDTO.getIssuerBankId(), getUserAccount());
        return new ApiResponse<>(resDTO);
    }
}
