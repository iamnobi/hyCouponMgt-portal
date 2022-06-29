package ocean.acs.commons.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ConditionalOnProperty<br>
 * The setting for batch server, because batch server no need setting portal properties<br>
 * - havingValue = true, ignore setting properties<br>
 * - havingValue = false, must setting properties<br>
 * - havingValue is missing, must setting properties
 */
@Component
@ConditionalOnProperty(name = "portal.properties.ignore", havingValue = "false", matchIfMissing = true)
public class PortalMessageConstants {

  // Kernel
  public static String NOT_FOUND;
  public static String COLUMN_NOT_EMPTY;
  public static String MIMA_INCORRECT;
  public static String NOT_SUPPORTED;
  public static String IO_LOAD_ERROR;
  public static String IO_WRITE_ERROR;
  public static String IO_CLOSE_ERROR;
  public static String KEY_SPECIFICATIONS_INVALID;
  public static String COUNTRY_CODE_ERROR;
  public static String ENUM_NAME_INVALID;
  public static String ACS_SETTING_NOT_FOUND;
  public static String REQUIRED_SSL_CLIENT_CERTIFICATE;
  public static String DUPLICATE_DATA_ELEMENT;
  public static String AUTH_DEVICECHANNEL_INVALID;
  public static String INVALID_MESSAGE_TYPE;
  public static String INVALID_FORMATTED_MESSAGE;
  public static String CONNECT_TIMEOUT;
  public static String READ_TIMEOUT;
  public static String CONNECT_FAIL;
  public static String UNACCEPTED_VALUE;
  public static String DECRYPTION_ERROR;
  public static String NO_SUCH_TRANSACTION;
  public static String JSON_PARSE_ERROR;
  public static String RECEIVE_ERROR;
  public static String MISSING_REQUIRED_PARAMETER_KEK;
  public static String PORTAL_SERVER_ERROR;

  @Value("${not.found}")
  public void setNotFound(String notFound) {
    PortalMessageConstants.NOT_FOUND = notFound;
  }

  @Value("${column.notempty}")
  public void setColumnNotEmpty(String columnNotEmpty) {
    PortalMessageConstants.COLUMN_NOT_EMPTY = columnNotEmpty;
  }

  @Value("${mima.incorrect}")
  public void setMimaIncorrect(String mimaIncorrect) {
    this.MIMA_INCORRECT = mimaIncorrect;
  }

  @Value("${not.supported}")
  public void setNotSupported(String notSupported) {
    PortalMessageConstants.NOT_SUPPORTED = notSupported;
  }

  @Value("${io.load.error}")
  public void setIoLoadError(String ioLoadError) {
    IO_LOAD_ERROR = ioLoadError;
  }

  @Value("${io.write.error}")
  public void setIoWriteError(String ioWriteError) {
    PortalMessageConstants.IO_WRITE_ERROR = ioWriteError;
  }

  @Value("${io.close.error}")
  public void setIoCloseError(String ioCloseError) {
    IO_CLOSE_ERROR = ioCloseError;
  }

  @Value("${key.specifications.invalid}")
  public void setKeySpecificationsInvalid(String keySpecificationsInvalid) {
    PortalMessageConstants.KEY_SPECIFICATIONS_INVALID = keySpecificationsInvalid;
  }

  @Value("${country.code.error}")
  public void setCountryCodeError(String countryCodeError) {
    COUNTRY_CODE_ERROR = countryCodeError;
  }

  @Value("${enum.name.invalid}")
  public void setEnumNameInvalid(String enumNameInvalid) {
    ENUM_NAME_INVALID = enumNameInvalid;
  }

  @Value("${acs.setting.not.found}")
  public void setAcsSettingNotFound(String acsSettingNotFound) {
    ACS_SETTING_NOT_FOUND = acsSettingNotFound;
  }

  @Value("${required.ssl.client.certificate}")
  public void setRequiredSslClientCertificate(String requiredSslClientCertificate) {
    REQUIRED_SSL_CLIENT_CERTIFICATE = requiredSslClientCertificate;
  }

  @Value("${duplicate.data.element}")
  public void setDuplicateDataElement(String duplicateDataElement) {
    DUPLICATE_DATA_ELEMENT = duplicateDataElement;
  }

  @Value("${auth.deviceChannel.invalid}")
  public void setAuthDevicechannelInvalid(String authDevicechannelInvalid) {
    AUTH_DEVICECHANNEL_INVALID = authDevicechannelInvalid;
  }

  @Value("${invalid.message.type}")
  public void setInvalidMessageType(String invalidMessageType) {
    INVALID_MESSAGE_TYPE = invalidMessageType;
  }

  @Value("${invalid.formatted.message}")
  public void setInvalidFormattedMessage(String invalidFormattedMessage) {
    INVALID_FORMATTED_MESSAGE = invalidFormattedMessage;
  }

  @Value("${connect.timeout}")
  public void setConnectTimeout(String connectTimeout) {
    CONNECT_TIMEOUT = connectTimeout;
  }

  @Value("${read.timeout}")
  public void setReadTimeout(String readTimeout) {
    READ_TIMEOUT = readTimeout;
  }

  @Value("${connect.fail}")
  public void setConnectFail(String connectFail) {
    CONNECT_FAIL = connectFail;
  }

  @Value("${unaccepted.value}")
  public void setUnacceptedValue(String unacceptedValue) {
    UNACCEPTED_VALUE = unacceptedValue;
  }

  @Value("${decryption.error}")
  public void setDecryptionError(String decryptionError) {
    DECRYPTION_ERROR = decryptionError;
  }

  @Value("${no.such.transaction}")
  public void setNoSuchTransaction(String noSuchTransaction) {
    NO_SUCH_TRANSACTION = noSuchTransaction;
  }

  @Value("${json.parse.error}")
  public void setJsonParseError(String jsonParseError) {
    JSON_PARSE_ERROR = jsonParseError;
  }

  @Value("${receive.error}")
  public void setReceiveError(String rresReceiveError) {
    RECEIVE_ERROR = rresReceiveError;
  }

  @Value("${missing.required.parameter.kek}")
  public void setMissingRequiredParameterKek(String missingRequiredParameterKek) {
    MISSING_REQUIRED_PARAMETER_KEK = missingRequiredParameterKek;
  }

  @Value("${portal.server.error}")
  public void setPortalServerError(String portalServerError) {
    PORTAL_SERVER_ERROR = portalServerError;
  }

  // 3DS-Method
  public static String THREE_DS_METHOD_REQUEST_NOT_FOUND;

  @Value("${three.ds.method.request.not.found}")
  public void setThreeDsMethodRequestNotFound(String threeDsMethodRequestNotFound) {
    THREE_DS_METHOD_REQUEST_NOT_FOUND = threeDsMethodRequestNotFound;
  }

  // Certificate
  public static String CERT_IN_PROCESSING;
  public static String CERT_IS_COMPLETED;
  public static String CERT_PRIVATE_KEY_EMPTY;
  public static String CERT_INVALID;
  public static String CERTIFICATE_RELOAD_ERROR;
  public static String PRIVATE_KEY_INVALID;
  public static String RENEW_INVALID;
  public static String NO_TEMP_KEY_TO_DELETE;
  public static String CA_CERTIFICATE_NOT_FOUND;

  @Value("${certificate.in.processing}")
  public void setCertInProcessing(String certInProcessing) {
    this.CERT_IN_PROCESSING = certInProcessing;
  }

  @Value("${certificate.is.completed}")
  public void setCertIsCompleted(String certIsCompleted) {
    CERT_IS_COMPLETED = certIsCompleted;
  }

  @Value("${certificate.private.key.empty}")
  public void setCertPrivateKeyEmpty(String certPrivateKeyEmpty) {
    this.CERT_PRIVATE_KEY_EMPTY = certPrivateKeyEmpty;
  }

  @Value("${certificate.invalid}")
  public void setCertInvalid(String certInvalid) {
    this.CERT_INVALID = certInvalid;
  }

  @Value("${certificate.reload.error}")
  public void setCertificateReloadError(String certificateReloadError) {
    CERTIFICATE_RELOAD_ERROR = certificateReloadError;
  }

  @Value("${private.key.invalid}")
  public void setPrivateKeyInvalid(String privateKeyInvalid) {
    PRIVATE_KEY_INVALID = privateKeyInvalid;
  }

  @Value("${renew.invalid}")
  public void setRenewInvalid(String renewInvalid) {
    RENEW_INVALID = renewInvalid;
  }

  @Value("${no.temp.key.to.delete}")
  public void setNoTempKeyToDelete(String noTempKeyToDelete) {
    NO_TEMP_KEY_TO_DELETE = noTempKeyToDelete;
  }

  @Value("${ca.certificate.not.found}")
  public void setCaCertificateNotFound(String caCertificateNotFound) {
    CA_CERTIFICATE_NOT_FOUND = caCertificateNotFound;
  }

  // Error Message
  public static String VALID_FAILED;
  public static String NOT_RECOGNISED;
  public static String MESSAGE_EXTENSION_MORE_THAN_10_EXTENSIONS;

  @Value("${valid.failed}")
  public void setValidFailed(String validFailed) {
    VALID_FAILED = validFailed;
  }

  @Value("${not.recognised}")
  public void setNotRecognised(String notRecognised) {
    NOT_RECOGNISED = notRecognised;
  }

  @Value("${messageExtension.more.than.10.extensions}")
  public void setMessageExtensionMoreThan10Extensions(String messageExtensionMoreThan10Extensions) {
    MESSAGE_EXTENSION_MORE_THAN_10_EXTENSIONS = messageExtensionMoreThan10Extensions;
  }

  // ACS-Integrator
  public static String ACS_INTEGRATOR_CONNECTION_ERROR;
  public static String ACS_INTEGRATOR_RESPONSE_FORMAT_INCORRECT;

  @Value("${acs.integrator.connection.error}")
  public void setAcsIntegratorConnectionError(String acsIntegratorConnectionError) {
    ACS_INTEGRATOR_CONNECTION_ERROR = acsIntegratorConnectionError;
  }

  @Value("${acs.integrator.response.format.incorrect}")
  public void setAcsIntegratorResponseFormatIncorrect(String acsIntegratorResponseFormatIncorrect) {
    ACS_INTEGRATOR_RESPONSE_FORMAT_INCORRECT = acsIntegratorResponseFormatIncorrect;
  }

  // DB
  public static String DB_SAVE_ERROR;
  public static String DB_READ_ERROR;

  @Value("${db.save.error}")
  public void setDbSaveError(String dbSaveError) {
    DB_SAVE_ERROR = dbSaveError;
  }

  @Value("${db.read.error}")
  public void setDbReadError(String dbReadError) {
    DB_READ_ERROR = dbReadError;
  }
}
