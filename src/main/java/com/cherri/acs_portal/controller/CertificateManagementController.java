package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.SystemConstants;
import com.cherri.acs_portal.controller.request.ChangeSigningCertDto;
import com.cherri.acs_portal.controller.request.CreateOrRenewSslCertificateP12ReqDto;
import com.cherri.acs_portal.controller.request.CreateOrRenewSslCertificateReqDto;
import com.cherri.acs_portal.controller.request.CreateSigningCertificateReqDto;
import com.cherri.acs_portal.controller.request.GenCsrRequestDto;
import com.cherri.acs_portal.controller.request.SigningCertificateQueryDto;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.certificate.CardBrandSigningCertificateConfigDTO;
import com.cherri.acs_portal.dto.system.CaCertificateDto;
import com.cherri.acs_portal.dto.system.DownloadCertP12RequestDto;
import com.cherri.acs_portal.dto.system.GenKeyRequest;
import com.cherri.acs_portal.dto.system.SslClientCertificateDto;
import com.cherri.acs_portal.facade.CertificateManagementFacade;
import com.cherri.acs_portal.util.FileUtils;
import com.cherri.acs_portal.validator.validation.CardBrandValidation;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
@Validated
@RequestMapping("/auth/certificate-management")
public class CertificateManagementController extends ContextProvider {

  private final static String[] CERTIFICATE_FILE_EXTENSION_NAMES = {"pem", "cer", "crt", "der", "p7b", "p7c", "p12"};

  @Autowired CertificateManagementFacade facade;

  /** 查詢CA憑證 */
  @GetMapping("/ca")
  @Secured("ROLE_CERT_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.CA_CERTIFICATE_GET)
  public ApiResponse<CaCertificateDto> getCaCertificate() {
      List<CaCertificateDto> dto =
        facade.getCaCertificateByCardBrand(SystemConstants.CARD_BRAND);
    return new ApiResponse(dto);
  }

  /** 上傳CA憑證 */
  @PostMapping(value = "/ca", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.CA_CERTIFICATE_CREATE)
  public ApiResponse saveCaCertificate(
      @RequestParam(value = "certificate") MultipartFile caCertFile)
      throws IOException {

    ApiResponse apiResponse = validateUploadCertificateFile(caCertFile, CERTIFICATE_FILE_EXTENSION_NAMES);
    if (apiResponse != null) {
      return apiResponse;
    }

      CaCertificateDto caCertificateDto = new CaCertificateDto(SystemConstants.CARD_BRAND, caCertFile);
    if (FileUtils.isFilenameExceededLengthLimit(caCertificateDto.getFileName())) {
      throw new OceanException(
          ResultStatus.INVALID_FORMAT, "Exceeds maximum filename length by 255");
    }
    caCertificateDto.setIssuerBankId(getIssuerBankId());
    caCertificateDto.setUser(getUserAccount());
    DataEditResultDTO dto = facade.uploadCaCertificate(caCertificateDto);
    return new ApiResponse<>(dto);
  }

  /** 驗證上傳憑證檔案副檔名 */
  private ApiResponse validateUploadCertificateFile(MultipartFile file, String... extNames) {
    if (extNames == null || extNames.length == 0) {
      return null;
    }

    if(file == null) {
      String errMsg = "Failed in upload certificate, file is null.";
      return new ApiResponse(ResultStatus.INVALID_FORMAT, errMsg);
    }

    return null;
  }

  /** 刪除CA憑證 */
  @PostMapping("/ca/delete")
  @Secured("ROLE_CERT_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.CA_CERTIFICATE_DELETE)
  public ApiResponse deleteCaCertificate(@RequestBody DeleteDataDTO deleteDataDto) {
    deleteDataDto.setUser(getUserAccount());
    return new ApiResponse<>(facade.deleteCaCertificate(deleteDataDto));
  }

  /** 查詢SSL憑證 */
  @GetMapping("/ssl")
  @Secured("ROLE_CERT_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_GET)
  public ApiResponse getSslCertificate() {
    SslClientCertificateDto dto = facade.findSslCertificateByCardBrand(SystemConstants.CARD_BRAND);
    if (dto == null) {
      return new ApiResponse<>(new SslClientCertificateDto());
    }
    return new ApiResponse<>(dto);
  }

  /** 上傳SSL p12 憑證 */
  @PostMapping(value = "/ssl/upload/p12", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_UPLOAD_P12)
  public ApiResponse uploadSslCertificateP12(
      @RequestParam("password") String pwd,
      @RequestParam("p12") MultipartFile file)
      throws IOException {
    String cardBrand = SystemConstants.CARD_BRAND;
    ApiResponse apiResponse = validateUploadCertificateFile(file, CERTIFICATE_FILE_EXTENSION_NAMES);
    if (apiResponse != null) {
      return apiResponse;
    }

      String cardInfo = cardBrand.toUpperCase();
      CreateOrRenewSslCertificateP12ReqDto uploadFileDto = new CreateOrRenewSslCertificateP12ReqDto(
        cardInfo, pwd, file);
    if (FileUtils.isFilenameExceededLengthLimit(uploadFileDto.getFileName())) {
      throw new OceanException(
          ResultStatus.INVALID_FORMAT, "Exceeds maximum filename length by 255");
    }
    uploadFileDto.setIssuerBankId(getIssuerBankId());
    uploadFileDto.setUser(getUserAccount());
    if(file == null) {
      throw new OceanException(ResultStatus.INVALID_FORMAT, "upload f12 file is empty.");
    }
    DataEditResultDTO dto  = facade.uploadSslCertificateP12(uploadFileDto);
    return new ApiResponse<>(dto);
  }

  /** 更新SSL p12 憑證 */
  @PostMapping(value = "/ssl/renew/p12", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_RENEW_P12)
  public ApiResponse renewSslCertificateByP12(
          @RequestParam("password") String pwd,
          @RequestParam("id") Long id,
          @RequestParam("p12") MultipartFile file)
          throws IOException {
      String cardBrand = SystemConstants.CARD_BRAND;
      String cardInfo = cardBrand.toUpperCase();
      CreateOrRenewSslCertificateP12ReqDto uploadFileDto = new CreateOrRenewSslCertificateP12ReqDto(
        id, cardInfo, pwd, file);
    if (FileUtils.isFilenameExceededLengthLimit(uploadFileDto.getFileName())) {
      throw new OceanException(
              ResultStatus.INVALID_FORMAT, "Exceeds maximum filename length by 255");
    }
    uploadFileDto.setIssuerBankId(getIssuerBankId());
    uploadFileDto.setUser(getUserAccount());
    DataEditResultDTO dto  = facade.renewSslCertificateByP12(uploadFileDto);
    return new ApiResponse<>(dto);
  }

  /** 下載SSL p12 憑證 */
  @PostMapping(value = "/ssl/download/p12")
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_DOWNLOAD_P12)
  public void downloadSslCertificateP12(@RequestBody DownloadCertP12RequestDto dto) {
    facade.downloadSslCertificateP12(SystemConstants.CARD_BRAND, dto.getPassword());
  }

  /** 產生SSL CSR key */
  @PostMapping("/ssl/gen-key")
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_GENERATE_KEY)
  public ApiResponse genSslCertificateTmpKey(
      @RequestBody GenKeyRequest request) {
    request.setCardBrand(SystemConstants.CARD_BRAND);
    request.setUser(getUserAccount());
    return ApiResponse.valueOf(facade.generateSslCertificateKeyAndCSR(request));
  }

  /** 刪除SSL CSR key */
  @PostMapping("/ssl/delete/tmp-key")
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_DELETE_TEMP_KEY)
  public ApiResponse deleteSslCertificateTmpKey() {
    boolean isSuccess = facade.deleteSslCertificateTmpKey(SystemConstants.CARD_BRAND);
    return isSuccess
        ? ApiResponse.SUCCESS_API_RESPONSE
        : new ApiResponse(ResultStatus.SERVER_ERROR, "Failed in delete ssl-certificate tmp key.");
  }

  /** 更新SSL CSR key */
  @PostMapping(value = "/ssl/renew/key")
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_RENEW_GENERATE_KEY)
  public ApiResponse<Map<String, String>> renewSslCertificateRequestByGenKey(
          @RequestBody GenKeyRequest request) {
    request.setCardBrand(SystemConstants.CARD_BRAND);
    request.setUser(getUserAccount());
    return ApiResponse.valueOf(facade.renewSslCertificateByGenKey(request));
  }

  /** 上傳SSL Certificate 憑證 */
  @PostMapping(value = "/ssl/upload/cert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_UPLOAD_CERTIFICATE)
  public ApiResponse uploadSslCertificate(@RequestParam("certificate") MultipartFile certFile)
      throws IOException {
    String cardBrand = SystemConstants.CARD_BRAND;

    ApiResponse apiResponse = validateUploadCertificateFile(certFile, CERTIFICATE_FILE_EXTENSION_NAMES);
    if (apiResponse != null) {
      return apiResponse;
    }

      String cardInfo = cardBrand.toUpperCase();
      CreateOrRenewSslCertificateReqDto uploadFileDto = new CreateOrRenewSslCertificateReqDto(
        cardInfo, certFile);
    uploadFileDto.setIssuerBankId(getIssuerBankId());
    uploadFileDto.setUser(getUserAccount());
    DataEditResultDTO dto = facade.saveSslCertificate(uploadFileDto);
    return new ApiResponse<>(dto);
  }

  /** 更新SSL Certificate 憑證 */
  @PostMapping(value = "/ssl/renew/cert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_RENEW_UPLOAD_CERTIFICATE)
  public ApiResponse renewSslCertificate(
          @RequestParam("id") Long id,
          @RequestParam("certificate") MultipartFile certFile)
          throws IOException {
    String cardBrand = SystemConstants.CARD_BRAND;
      String cardInfo = cardBrand.toUpperCase();
      CreateOrRenewSslCertificateReqDto uploadFileDto = new CreateOrRenewSslCertificateReqDto(id,
        cardInfo, certFile.getBytes(), certFile.getName());
    uploadFileDto.setIssuerBankId(getIssuerBankId());
    uploadFileDto.setUser(getUserAccount());
    DataEditResultDTO dto = facade.renewSslCertificate(uploadFileDto);
    return new ApiResponse<>(dto);
  }

    /** 查詢Signing憑證 */
    @PostMapping("/signing")
    @Secured("ROLE_CERT_QUERY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.SIGNING_GET)
    public ApiResponse getCertificateByCardBrand(
      @RequestBody SigningCertificateQueryDto signingCertificateQueryDto) {
      signingCertificateQueryDto.setVersion(2);
      signingCertificateQueryDto.setCardBrand(SystemConstants.CARD_BRAND);
      signingCertificateQueryDto.setIssuerBankId(EnvironmentConstants.ORG_ISSUER_BANK_ID);
        return new ApiResponse<>(
          facade.getCertificatesByCardBrandAndIssuerBankId(signingCertificateQueryDto));
    }

    /** 查詢Signing憑證各 card-brand 是否共用 */
    @GetMapping("/signing/card-brand-config")
    public ApiResponse<List<CardBrandSigningCertificateConfigDTO>> getCardBrandSigningCertificateConfig() {
        return new ApiResponse<>(facade.findCardBrandSigningCertificateConfig());
    }

    /** 上傳Signing 憑證鏈 */
    @PostMapping("/signing/upload")
    @Secured("ROLE_CERT_MODIFY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.SIGNING_CERTIFICATE_UPDATE)
    public ApiResponse uploadSigningCertificate(
      @RequestParam MultipartFile certificateList
//      @RequestParam String cardBrand,
//      @RequestParam Long bankId, // 因為jcb需要依各銀行產signing憑證，若是全體銀行這裡的值為-1，若是JCB則為各家銀行的id
//      @RequestParam String issuerBankCode,
//      @RequestParam Long issuerBankId, // 登入者的銀行id
//      @RequestParam int version
  ) {

      String cardBrand = SystemConstants.CARD_BRAND;
      Long bankId = null;
      String issuerBankCode = "0000";
      Long issuerBankId = EnvironmentConstants.ORG_ISSUER_BANK_ID;
      int version = 2;

    try {
        CreateSigningCertificateReqDto uploadFileDto = new CreateSigningCertificateReqDto(
            version, cardBrand, issuerBankCode, certificateList.getBytes(), certificateList.getName(), issuerBankId);
      uploadFileDto.setIssuerBankId(issuerBankId);// 這裡不要再帶入登入者的銀行id!!!會造成jcb查不到各銀行的signing
      uploadFileDto.setUser(getUserAccount());
      DataEditResultDTO dto = facade.uploadSigningCertificate(uploadFileDto);
      return new ApiResponse<>(dto);
    } catch (IOException e) {
      log.warn("[uploadSigningCertificate] failed in upload p7b file.", e);
      return new ApiResponse(ResultStatus.SERVER_ERROR, "Failed in upload p7b file.");
    }
  }

  /** 產生Signing憑證 CSR key */
  @PostMapping("/signing/gen-csr")
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_GENERATE_KEY)
  public ApiResponse genCsrKey(@RequestBody GenCsrRequestDto request) {
    request.setIssuerBankId(EnvironmentConstants.ORG_ISSUER_BANK_ID);
    request.setCardBrand(SystemConstants.CARD_BRAND);
    request.setVersion(2);
    return ApiResponse.valueOf(facade.generateCsrKey(request, getUserAccount()));
  }

  /** 查詢Signing憑證列表 */
  @PostMapping("/signing/cert-list")
  @Secured("ROLE_CERT_QUERY")
  public ApiResponse getSigningCertList(@RequestBody SigningCertificateQueryDto request) {
    request.setVersion(2);
    request.setIssuerBankId(EnvironmentConstants.ORG_ISSUER_BANK_ID);
    request.setCardBrand(SystemConstants.CARD_BRAND);
    return ApiResponse.valueOf(
        facade.getSigningCertList(
            request.getVersion(), request.getIssuerBankId(), request.getCardBrand()));
  }
  /** 查詢SSL憑證列表 */
  @PostMapping("/ssl/cert-list")
  @Secured("ROLE_CERT_QUERY")
  public ApiResponse getSslCertList(@RequestBody SigningCertificateQueryDto request) {
    request.setVersion(2);
    request.setIssuerBankId(EnvironmentConstants.ORG_ISSUER_BANK_ID);
    request.setCardBrand(SystemConstants.CARD_BRAND);
    return ApiResponse.valueOf(
        facade.getSslCertList(request.getCardBrand()));
  }

  /** 切換Signing憑證 */
  @PostMapping("/signing/change-cert")
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SIGNING_CHANGE_CERT)
  public ApiResponse changeSigningCert(@RequestBody ChangeSigningCertDto request) {
    request.setVersion(2);
    request.setIssuerBankId(EnvironmentConstants.ORG_ISSUER_BANK_ID);
    request.setCardBrand(SystemConstants.CARD_BRAND);
    facade.changeSigningCert(
        request.getCertId(), request.getVersion(), request.getIssuerBankId(), request.getCardBrand(), getUserAccount());
    return ApiResponse.SUCCESS_API_RESPONSE;
  }

  /** 切換Signing憑證 */
  @PostMapping("/ssl/change-cert")
  @Secured("ROLE_CERT_MODIFY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.SSL_CHANGE_CERT)
  public ApiResponse changeSslCert(@RequestBody ChangeSigningCertDto request) {
    request.setVersion(2);
    request.setIssuerBankId(EnvironmentConstants.ORG_ISSUER_BANK_ID);
    request.setCardBrand(SystemConstants.CARD_BRAND);
    facade.changeSslCert(
        request.getCertId(), request.getCardBrand(), getUserAccount());
    return ApiResponse.SUCCESS_API_RESPONSE;
  }

}
