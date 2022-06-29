package com.cherri.acs_portal.serializer;

import com.cherri.acs_portal.config.ClassicRbaProperties.EnabledModules;
import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.dto.transactionLog.ClassicRbaResultDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import com.cherri.acs_portal.enumeration.ClassicRbaReportName;
import ocean.acs.commons.enumerator.ThreeDsVersion;

public class ClassicRbaResultDtoSerializer extends StdSerializer<ClassicRbaResultDto> {

    public ClassicRbaResultDtoSerializer() {
        this(null);
    }

    public ClassicRbaResultDtoSerializer(Class<ClassicRbaResultDto> t) {
        super(t);
    }

    @Override
    public void serialize(
      ClassicRbaResultDto classicRbaResultDto, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
        EnabledModules enabledModules = classicRbaResultDto.getEnabledModules();

        jgen.writeStartObject();

        jgen.writeStringField(
            MessageConstants.get(MessageConstants.RBA_CLASSIC_RBA_ENABLED), convertRbaResultToDescription(classicRbaResultDto.getRbaChecked()));

        if (classicRbaResultDto.getThreeDsVersion().equals(ThreeDsVersion.TWO)) {
            if (enabledModules.isAPT()) {
                jgen.writeStringField(
                  ClassicRbaReportName.PURCHASE_AMOUNT.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(classicRbaResultDto.getRbaPurchaseAmountResult()));
            }
            if (enabledModules.isCDC()) {
                jgen.writeStringField(
                  ClassicRbaReportName.CARDHOLDER_DATA.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(classicRbaResultDto.getRbaCardholderDataResult()));
            }
            if (enabledModules.isCAC()) {
                StringBuilder sb = new StringBuilder();
                sb.append(convertRbaResultToDescription(
                  classicRbaResultDto.getRbaCumulativeAmountResult()));
                if (classicRbaResultDto.getRbaCumulativeAmountLogAmount() != null) {
                    sb.append("(");
                    sb.append(classicRbaResultDto.getRbaCumulativeAmountLogAmount());
                    if (classicRbaResultDto.getRbaCumulativeAmountLogCount() != null) {
                        sb.append(",");
                        sb.append(classicRbaResultDto.getRbaCumulativeAmountLogCount());
                    }
                    sb.append(")");
                }
                jgen.writeStringField(
                  ClassicRbaReportName.CUMULATIVE_AMOUNT.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  sb.toString());
            }
            if (enabledModules.isCTF()) {
                jgen.writeStringField(
                  ClassicRbaReportName.CUMULATIVE_TRANSACTION.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(
                    classicRbaResultDto.getRbaCumulativeTransactionResult()));
            }
            if (enabledModules.isLCC()) {
                jgen.writeStringField(
                  ClassicRbaReportName.LOCATION_CONSISTENCY.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(
                    classicRbaResultDto.getRbaLocationConsistencyResult()));
            }
            if (enabledModules.isBLC()) {
                jgen.writeStringField(
                  ClassicRbaReportName.BROWSER_LANGUAGE.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(classicRbaResultDto.getRbaBrowserLanguageResult()));
            }
            if (enabledModules.isVPN()) {
                jgen.writeStringField(
                  ClassicRbaReportName.VPN.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(classicRbaResultDto.getRbaVpnResult()));
            }
            if (enabledModules.isPBC()) {
                jgen.writeStringField(
                  ClassicRbaReportName.PROXY.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(classicRbaResultDto.getRbaProxyResult()));
            }
            if (enabledModules.isPBC()) {
                jgen.writeStringField(
                  ClassicRbaReportName.PRIVATE_BROWSING.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(classicRbaResultDto.getRbaPrivateBrowsingResult()));
            }
            if (enabledModules.isDVC()) {
                jgen.writeStringField(
                  ClassicRbaReportName.DEVICE_VARIATION.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(classicRbaResultDto.getRbaDeviceVariationResult()));
            }
            if (enabledModules.isMCC()) {
                jgen.writeStringField(
                  ClassicRbaReportName.MCC.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(classicRbaResultDto.getRbaMccResult()));
            }
            if (enabledModules.isRPR()) {
                jgen.writeStringField(
                  ClassicRbaReportName.RECURRING_PAYMENT.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
                  convertRbaResultToDescription(
                    classicRbaResultDto.getRbaRecurringPaymentResult()));
            }
        }

        // ACS 目前不支援 1.0
//    if (classicRbaResultDto.getThreeDsVersion().equals(ThreeDsVersion.ONE)) {
//
//      jgen.writeStringField(
//          ClassicRbaReportName.CUMULATIVE_AMOUNT.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
//          convertRbaResultToDescription(classicRbaResultDto.getRbaCumulativeAmountResult()));
//
//      jgen.writeStringField(
//          ClassicRbaReportName.RECURRING_PAYMENT.getCodeAndName() + MessageConstants.get(MessageConstants.RBA_RESULT),
//          convertRbaResultToDescription(classicRbaResultDto.getRbaRecurringPaymentResult()));
//    }

        jgen.writeEndObject();
    }

    private String convertRbaResultToDescription(Boolean result) {
        if (null == result) {
            return MessageConstants.get(MessageConstants.RBA_NOT_APPLICABLE);
        }
        return result ? MessageConstants.get(MessageConstants.RBA_PASS) : MessageConstants.get(MessageConstants.RBA_FAIL);
    }
}
