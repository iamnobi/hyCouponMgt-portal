package ocean.acs.models.data_object.kernel;

import java.util.Map;
import lombok.Getter;
import lombok.ToString;


import ocean.acs.commons.constant.KernelEnvironmentConstant;
import ocean.acs.commons.constant.KernelMessageConstant;
import ocean.acs.commons.constant.KernelSystemConstant;
import ocean.acs.commons.enumerator.EmvErrorReason;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.models.data_object.entity.ErrorMessageLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;

@Getter
@ToString
public class ErrorDO {

    private String threeDSServerTransID;
    private String acsTransID;
    private String dsTransID;

    private String errorCode;
    private String errorComponent;
    private String errorDescription;
    private String errorDetail;
    private String errorMessageType;

    private String messageType;
    private String messageVersion;
    private String sdkTransID;

    private ErrorDO(Builder builder) {
        this.threeDSServerTransID = builder.threeDSServerTransID;
        this.acsTransID = builder.acsTransID;
        this.dsTransID = builder.dsTransID;
        this.errorCode = builder.errorCode.getCode();
        this.errorComponent = builder.errorComponent;
        this.errorDescription = builder.errorDescription;
        this.errorDetail = builder.errorDetail;
        this.errorMessageType = builder.errorMessageType.name();
        this.messageType = MessageType.Erro.name();
        if (null == builder.messageVersion) {
            this.messageVersion = KernelEnvironmentConstant.MESSAGE_VERSION;
        } else {
            this.messageVersion = builder.messageVersion;
        }
        this.sdkTransID = builder.sdkTransID;
    }

    public static ErrorDO getBrowserNotSupportError(String threeDSServerTransID) {
        return ErrorDO.builder().errorCode(EmvErrorReason.ACCESS_DENIED_INVALID_Endpoint)
                .errorDescription("Access denied, invalid endpoint.")
                .errorDetail("Browser not support").errorMessageType(MessageType.CReq)
                .threeDSServerTransID(threeDSServerTransID)
                .messageVersion(KernelEnvironmentConstant.MESSAGE_VERSION).build();
    }

    public static ErrorDO getOtpPhoneNumberNotSetError(String threeDSServerTransID) {
        return ErrorDO.builder().errorCode(EmvErrorReason.ACCESS_DENIED_INVALID_Endpoint)
                .errorDescription("Access denied, invalid endpoint.")
                .errorDetail("User otp phone number not set.").errorMessageType(MessageType.CReq)
                .threeDSServerTransID(threeDSServerTransID)
                .messageVersion(KernelEnvironmentConstant.MESSAGE_VERSION).build();
    }

    public static ErrorDO getTransientSystemFailureError(String threeDSServerTransID,
            MessageType messageType, String errorDetail) {
        return ErrorDO.builder().errorCode(EmvErrorReason.TRANSIENT_SYSTEM_FAILURE)
                .errorDescription("Transient system failure").errorDetail(errorDetail)
                .errorMessageType(messageType).threeDSServerTransID(threeDSServerTransID)
                .messageVersion(KernelEnvironmentConstant.MESSAGE_VERSION).build();
    }

    public static ErrorDO getTransactionIdNotRecognisedError(MessageType messageType,
            String threeDSServerTransID) {
        return ErrorDO.builder().errorCode(EmvErrorReason.TRANSACTION_ID_NOT_RECOGNISED)
                .errorDescription(
                        "Transaction ID received is not valid for the receiving component.")
                .errorDetail("Transaction ID received is not valid for the receiving component.")
                .errorMessageType(messageType).threeDSServerTransID(threeDSServerTransID)
                .messageVersion(KernelEnvironmentConstant.MESSAGE_VERSION).build();
    }

    public static ErrorDO getOtpLockError(String threeDSServerTransID, String acsTransID) {
        return ErrorDO.builder().errorCode(EmvErrorReason.ACCESS_DENIED_INVALID_Endpoint)
                .errorDescription("Access denied, invalid endpoint.")
                .errorDetail("OTP verification for this credit card has been locked.")
                .errorMessageType(MessageType.CReq).threeDSServerTransID(threeDSServerTransID)
                .acsTransID(acsTransID).messageVersion(KernelEnvironmentConstant.MESSAGE_VERSION)
                .build();
    }

    public static ErrorDO getInvalidParameterError(MessageType messageType) {
        return ErrorDO.builder().errorCode(EmvErrorReason.INVALID_FORMAT)
                .errorDescription(KernelSystemConstant.ERROR_DES_INVALID_FORMAT)
                .errorDetail("CReq Message Data is invalid.").errorMessageType(messageType)
                .messageVersion(KernelEnvironmentConstant.MESSAGE_VERSION).build();
    }

    public static ErrorDO getTimeoutError(MessageType errorMessageType, TransactionLogDO txLog)
            throws NullPointerException {
        return ErrorDO.builder().errorMessageType(errorMessageType)
                .messageVersion(txLog.getMessageVersion())
                .errorCode(EmvErrorReason.TRANSACTION_TIMED_OUT)
                .errorDescription("Transaction timed-out.")
                .errorDetail(
                        "Timeout expiry reached for the transaction as defined in Section 5.5.")
                .threeDSServerTransID(txLog.getThreeDSServerTransID())
                .dsTransID(txLog.getDsTransID()).acsTransID(txLog.getAcsTransID())
                .sdkTransID(txLog.getSdkTransID()).build();
    }

    public static ErrorDO getDataDecryptionFailureError(String threeDSServerTransID,
            String sdkTransID, String dsTransID, String acsTransID, String errorDetail) {
        return ErrorDO.builder().errorMessageType(MessageType.CReq)
                .errorCode(EmvErrorReason.DATA_DECRYPTION_FAILURE)
                .errorDescription(
                        "Data could not be decrypted by the receiving system due to technical or other reason")
                .errorDetail(errorDetail).threeDSServerTransID(threeDSServerTransID)
                .sdkTransID(sdkTransID).dsTransID(dsTransID).acsTransID(acsTransID).build();
    }

    public static ErrorDO getSystemConnectionFailureError(String acsTransID,
            String threeDSServerTransID, String errorDetail) {
        return ErrorDO.builder().errorMessageType(MessageType.RRes)
                .errorCode(EmvErrorReason.SYSTEM_CONNECTION_FAILURE)
                .errorDescription("System connection failure.").errorDetail(errorDetail)
                .acsTransID(acsTransID).threeDSServerTransID(threeDSServerTransID).build();
    }

    public static ErrorDO valueOf(Map<String, Object> errMap) {
        String errorCodeStr = (String) errMap.get("errorCode");
        EmvErrorReason errorCode = null;
        if (errorCodeStr != null) {
            errorCode = EmvErrorReason.codeOf((String) errMap.get("errorCode"));
        }

        String errorMessageTypeStr = (String) errMap.get("errorMessageType");
        MessageType errorMessageType = null;

        if (errorMessageTypeStr != null) {
            errorMessageType = MessageType.valueOf((String) errMap.get("errorMessageType"));
        }

        return ErrorDO.builder().errorCode(errorCode)
                .threeDSServerTransID((String) errMap.get("threeDSServerTransID"))
                .acsTransID((String) errMap.get("acsTransID"))
                .dsTransID((String) errMap.get("dsTransID"))
                .sdkTransID((String) errMap.get("sdkTransID"))
                .errorDescription((String) errMap.get("errorDescription"))
                .errorDetail((String) errMap.get("errorDetail")).errorMessageType(errorMessageType)
                .messageVersion((String) errMap.get("messageVersion"))
                .errorComponent((String) errMap.get("errorComponent"))
                .build();
    }

    public static ErrorDO getRequiredDataMissingError(String threeDSServerTransID,
            String sdkTransID, String dsTransID, String acsTransID, String errorDetail,
            MessageType errorMessageType) {
        return ErrorDO.builder().errorCode(EmvErrorReason.REQUIRED_DATA_ELEMENT_MISSING)
                .errorDescription(KernelSystemConstant.ERROR_DES_REQUIRED_PARAMETER)
                .errorDetail(errorDetail).errorMessageType(errorMessageType)
                .threeDSServerTransID(threeDSServerTransID).sdkTransID(sdkTransID)
                .dsTransID(dsTransID).acsTransID(acsTransID).build();
    }

    public static ErrorDO getRequiredDataMissingError(String acsTransID,
            String threeDSServerTransID, MessageType messageType) {
        return ErrorDO.builder().errorMessageType(messageType)
                .errorCode(EmvErrorReason.REQUIRED_DATA_ELEMENT_MISSING)
                .errorDescription(KernelMessageConstant.COLUMN_NOT_EMPTY)
                .errorDetail(KernelMessageConstant.COLUMN_NOT_EMPTY).acsTransID(acsTransID)
                .threeDSServerTransID(threeDSServerTransID).build();
    }

    public static ErrorDO getInvalidFormatError(String threeDSServerTransID, String sdkTransID,
            String dsTransID, String acsTransID, String errorDescription, String errorDetail,
            MessageType errorMessageType) {

        return ErrorDO.builder().errorCode(EmvErrorReason.INVALID_FORMAT)
                .errorDescription(errorDescription).errorDetail(errorDetail)
                .errorMessageType(errorMessageType).threeDSServerTransID(threeDSServerTransID)
                .sdkTransID(sdkTransID).dsTransID(dsTransID).acsTransID(acsTransID).build();
    }

    public static ErrorDO getCardNumberNotBelongingToIssuerError(String threeDSServerTransID,
            String sdkTransID, String dsTransID, String acsTransID, MessageType errorMessageType) {

        return ErrorDO.builder().errorCode(EmvErrorReason.TRANSACTION_DATA_NOT_VALID)
                .errorDescription("Cardholder Account Number is not in a range belonging to Issuer")
                .errorDetail("acctNumber").errorMessageType(errorMessageType)
                .threeDSServerTransID(threeDSServerTransID).sdkTransID(sdkTransID)
                .dsTransID(dsTransID).acsTransID(acsTransID).build();
    }

    public static ErrorDO getMessageNotRecognizedError(String threeDSServerTransID,
            String sdkTransID, String dsTransID, String acsTransID, MessageType errorMessageType) {

        return ErrorDO.builder().errorCode(EmvErrorReason.MESSAGE_RECEIVED_INVALID)
                .errorDescription("Message not recognised.").errorDetail("Invalid Message Type")
                .errorMessageType(errorMessageType).threeDSServerTransID(threeDSServerTransID)
                .sdkTransID(sdkTransID).dsTransID(dsTransID).acsTransID(acsTransID).build();
    }

    public static ErrorDO getMessageVersionNotSupportedError(String threeDSServerTransID,
            String sdkTransID, String dsTransID, String acsTransID, MessageType errorMessageType) {
        return ErrorDO.builder().errorCode(EmvErrorReason.MESSAGE_VERSION_NUMBER_NOT_SUPPORTED)
                .errorDescription(
                        "Message Version Number received is not valid for the receiving component.")
                .errorDetail(KernelEnvironmentConstant.SUPPORTED_MESSAGE_VERSION_LIST)
                .errorMessageType(errorMessageType).threeDSServerTransID(threeDSServerTransID)
                .sdkTransID(sdkTransID).dsTransID(dsTransID).acsTransID(acsTransID).build();
    }

    public static ErrorDO getIsoCodeInvalidError(String threeDSServerTransID, String sdkTransID,
            String dsTransID, String acsTransID, String errorDetail, MessageType errorMessageType) {
        return ErrorDO.builder().errorCode(EmvErrorReason.ISO_CODE_INVALID).errorDescription(
                "ISO code not valid per ISO tables (for either country or currency), or code is one of the excluded values listed in Table A.5.")
                .errorDetail(errorDetail).errorMessageType(errorMessageType)
                .threeDSServerTransID(threeDSServerTransID).sdkTransID(sdkTransID)
                .dsTransID(dsTransID).acsTransID(acsTransID).build();
    }

    /**
     *
     * @param threeDSServerTransID
     * @param sdkTransID
     * @param dsTransID
     * @param acsTransID
     * @param errorDetail: ID of critical Message Extension(s) that was not recognised; if more than
     *        one extension is detected, this is a comma delimited list of message identifiers that
     *        were not recognised.
     * @param errorMessageType
     * @return
     */
    public static ErrorDO getNonRecognisedCriticalMessageExtensionError(String threeDSServerTransID,
            String sdkTransID, String dsTransID, String acsTransID, String errorDetail,
            MessageType errorMessageType) {
        return ErrorDO.builder().errorCode(EmvErrorReason.CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED)
                .errorDescription("Critical message extension not recognised.")
                .errorDetail(errorDetail).errorMessageType(errorMessageType)
                .threeDSServerTransID(threeDSServerTransID).sdkTransID(sdkTransID)
                .dsTransID(dsTransID).acsTransID(acsTransID).build();
    }

    public static ErrorDO getRResIoExceptionError(String acsTransID, String threeDSServerTransID) {
        return ErrorDO.builder().errorMessageType(MessageType.RRes)
                .errorCode(EmvErrorReason.REQUIRED_DATA_ELEMENT_MISSING)
                .errorDescription(KernelMessageConstant.COLUMN_NOT_EMPTY)
                .errorDetail(KernelMessageConstant.IO_LOAD_ERROR).acsTransID(acsTransID)
                .threeDSServerTransID(threeDSServerTransID).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String threeDSServerTransID;
        private String acsTransID;
        private String dsTransID;

        private EmvErrorReason errorCode;
        private String errorComponent = "A"; // ACS
        private String errorDescription;
        private String errorDetail;
        private MessageType errorMessageType;

        private String messageVersion;
        private String sdkTransID;

        public Builder threeDSServerTransID(String threeDSServerTransID) {
            this.threeDSServerTransID = threeDSServerTransID;
            return this;
        }

        public Builder acsTransID(String acsTransID) {
            this.acsTransID = acsTransID;
            return this;
        }

        public Builder dsTransID(String dsTransID) {
            this.dsTransID = dsTransID;
            return this;
        }

        public Builder errorCode(EmvErrorReason errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder errorComponent(String errorComponent) {
            this.errorComponent = errorComponent;
            return this;
        }

        public Builder errorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
            return this;
        }

        public Builder errorDetail(String errorDetail) {
            this.errorDetail = errorDetail;
            return this;
        }

        public Builder errorMessageType(MessageType errorMessageType) {
            this.errorMessageType = errorMessageType;
            return this;
        }

        public Builder messageVersion(String messageVersion) {
            this.messageVersion = messageVersion;
            return this;
        }

        public Builder sdkTransID(String sdkTransID) {
            this.sdkTransID = sdkTransID;
            return this;
        }

        public ErrorDO build() {
            return new ErrorDO(this);
        }
    }

    public ErrorMessageLogDO convertToErrorMessageLogDO(){
        String creator =
                this.getErrorMessageType() == null ? MessageType.Erro.name() : this.getErrorMessageType();
        String errorCode =
                this.getErrorCode() == null
                        ? EmvErrorReason.TRANSIENT_SYSTEM_FAILURE.getCode()
                        : this.getErrorCode();
        MessageType errorMessageType = MessageType.getByName(this.getErrorMessageType());
        ErrorMessageLogDO entity = new ErrorMessageLogDO();
        entity.setErrorCode(this.getErrorCode());
        entity.setErrorComponent(this.getErrorComponent());
        entity.setErrorDescription(this.getErrorDescription());
        entity.setErrorDetail(this.getErrorDetail());
        entity.setErrorMessageType(MessageType.getByName(this.getErrorMessageType()));
        entity.setMessageType(this.getMessageType());
        entity.setMessageVersion(this.getMessageVersion());
        entity.setErrorCode(errorCode);
        entity.setErrorMessageType(errorMessageType);
        entity.setSysCreator(creator);

        return entity;
    }
}
