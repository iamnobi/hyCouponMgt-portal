package com.cherri.acs_portal.service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.cherri.acs_portal.config.ClassicRbaProperties;
import com.cherri.acs_portal.config.ClassicRbaProperties.EnabledModules;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.controller.request.ClassicRbaReportReqDto;
import com.cherri.acs_portal.controller.request.ClassicRbaSettingUpdateReqDto;
import com.cherri.acs_portal.dto.rba.ClassicRbaReportDto;
import com.cherri.acs_portal.dto.rba.ClassicRbaSettingDto;
import com.cherri.acs_portal.dto.rba.ClassicRbaStatus;
import com.cherri.acs_portal.dto.rba.ClassicRbaStatusDto;
import com.cherri.acs_portal.manager.ClassicRbaReportManager;
import com.cherri.acs_portal.util.ExcelBuildUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.ClassicRbaCheckDAO;
import ocean.acs.models.dao.ClassicRbaReportDAO;
import ocean.acs.models.dao.DdcaLogDAO;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.data_object.entity.ClassicRbaCheckDO;
import ocean.acs.models.data_object.entity.ClassicRbaReportDO;

@Log4j2
@Service
@AllArgsConstructor
public class ClassicRbaService {

    private final ClassicRbaProperties classicRbaProperties;
    private final ClassicRbaCheckDAO classicRbaCheckDao;
    private final ClassicRbaReportDAO classicRbaReportDao;
    private final ClassicRbaReportManager classicRbaReportManager;
    private final DdcaLogDAO ddcaLogDao;
    private final IssuerBankDAO issuerBankDAO;

    private final HttpServletResponse httpServletResponse;

    /**
     * 取得 RBA service 狀態
     */
    public Optional<ClassicRbaStatusDto> getClassicRbaStatus() {
        try {
            long responseTime = ddcaLogDao.findLatestResponseTime();
            String ddcaStatus =
              EnvironmentConstants.SYSTEM_HEALTH_NORMAL >= responseTime ? "Y" : "N";
            ClassicRbaStatusDto classicRbaStatusDto = ClassicRbaStatusDto.builder()
              .ddcaDelay(responseTime + "ms")
              .ddcaStatus(ddcaStatus)
              .build();
            return Optional.of(classicRbaStatusDto);
        } catch (DatabaseException e) {
            String message = "[getClassicRbaStatus] database error";
            log.error(message, e);
            throw new OceanException(e.getResultStatus(), message);
        }
    }

    /**
     * 取得 RBA 設定, 沒有 enable 的模組不會回傳其相關設定。
     */
    public Optional<ClassicRbaSettingDto> getClassicRbaSetting(long issuerBankId) {

        try {
            ClassicRbaCheckDO classicRbaCheck = classicRbaCheckDao
              .findClassicRbaSetting(issuerBankId)
              .orElseThrow(() ->
                new OceanException(ResultStatus.NO_SUCH_DATA,
                  "classic rba setting doesn't exist"));
            ClassicRbaSettingDto classicRbaSettingDto =
              ClassicRbaSettingDto.valueOf(classicRbaCheck,
                classicRbaProperties.getCumulativeAmountInterval(),
                classicRbaProperties.getCumulativePeriodInterval());

            // 把沒有 enable 的模組「移除」後再傳給前端
            removeNotEnabledRbaModuleSettings(classicRbaSettingDto,
              classicRbaProperties.getEnabledModules());

            return Optional.of(classicRbaSettingDto);
        } catch (DatabaseException e) {
            String message = "[getClassicRbaSetting] database error";
            log.error(message, e);
            throw new OceanException(e.getResultStatus(), message);
        }

    }

    /**
     * 將沒有 enable 的模組從 ClassicRbaSettingDto 中移除
     */
    private void removeNotEnabledRbaModuleSettings(ClassicRbaSettingDto classicRbaSettingDto,
      EnabledModules enabledModules) {
        if (!enabledModules.isAPT()) {
            classicRbaSettingDto.setPurchaseAmount(null);
        }
        if (!enabledModules.isCDC()) {
            classicRbaSettingDto.setCardholderData(null);
        }
        if (!enabledModules.isCAC()) {
            classicRbaSettingDto.setCumulativeAmount(null);
        }
        if (!enabledModules.isCTF()) {
            classicRbaSettingDto.setCumulativeTransaction(null);
        }
        if (!enabledModules.isLCC()) {
            classicRbaSettingDto.setLocationConsistency(null);
        }
        if (!enabledModules.isBLC()) {
            classicRbaSettingDto.setBrowserLanguage(null);
        }
        if (!enabledModules.isVPN()) {
            classicRbaSettingDto.setVpn(null);
        }
        if (!enabledModules.isPXY()) {
            classicRbaSettingDto.setProxy(null);
        }
        if (!enabledModules.isPBC()) {
            classicRbaSettingDto.setPrivateBrowsing(null);
        }
        if (!enabledModules.isDVC()) {
            classicRbaSettingDto.setDeviceVariation(null);
        }
        if (!enabledModules.isMCC()) {
            classicRbaSettingDto.setMcc(null);
        }
        if (!enabledModules.isRPR()) {
            classicRbaSettingDto.setRecurringPayment(null);
        }
    }

    /**
     * 更新 RBA 設定，ClassicRbaSettingUpdateReqDto 中「沒有出現的設定」會視為 disable ex:
     * classicRbaSettingUpdateReqDto.cardHolderData == null ---> cardHolderData.enabled = false
     */
    public ClassicRbaSettingUpdateReqDto updateClassicRbaSetting(
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        try {
            ClassicRbaCheckDO classicRbaCheck = classicRbaCheckDao.findClassicRbaSetting(
              updateReqDto.getIssuerBankId())
              .orElseThrow(() ->
                new OceanException(ResultStatus.NO_SUCH_DATA,
                  "classic rba setting doesn't exist"));

            classicRbaCheck.setClassicRbaEnabled(updateReqDto.getClassicRbaEnabled());

            setPurchaseAmount(classicRbaCheck, updateReqDto)
              .setCardholder(classicRbaCheck, updateReqDto)
              .setCumulativeAmount(classicRbaCheck, updateReqDto)
              .setCumulativeTransaction(classicRbaCheck, updateReqDto)
              .setLocationConsistency(classicRbaCheck, updateReqDto)
              .setChannel(classicRbaCheck, updateReqDto)
              .setMcc(classicRbaCheck, updateReqDto)
              .setRecurringPayment(classicRbaCheck, updateReqDto);

            classicRbaCheck.setUpdateMillis(System.currentTimeMillis());
            classicRbaCheck.setUpdater(updateReqDto.getUser());
            classicRbaCheck.setAuditStatus(updateReqDto.getAuditStatus().getSymbol());

            classicRbaCheckDao.save(classicRbaCheck)
              .ifPresent(e -> updateReqDto.setId(e.getId()));

            return updateReqDto;

        } catch (DatabaseException e) {
            String message = "[updateClassicRbaSetting] database error";
            log.error(message, e);
            throw new OceanException(e.getResultStatus(), message);
        }

    }

    private ClassicRbaService setPurchaseAmount(ClassicRbaCheckDO classicRbaCheck,
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        if (updateReqDto.getPurchaseAmount() == null) {
            classicRbaCheck.setPurchaseAmountEnabled(false);
        } else {
            classicRbaCheck.setPurchaseAmountEnabled(updateReqDto.getPurchaseAmount().getEnabled());
            classicRbaCheck.setPurchaseAmountAmount(updateReqDto.getPurchaseAmount().getAmount());
            classicRbaCheck
              .setPurchaseAmountMinAmount(updateReqDto.getPurchaseAmount().getMinAmount());
            classicRbaCheck.setPurchaseAmountCurrencyCode(String.valueOf(EnvironmentConstants.SYSTEM_CURRENCY_CODE));
        }
        return this;
    }

    private ClassicRbaService setCardholder(ClassicRbaCheckDO classicRbaCheck,
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        if (updateReqDto.getCardholderData() == null) {
            classicRbaCheck.setCardholderDataEnabled(false);
        } else {
            classicRbaCheck.setCardholderDataEnabled(updateReqDto.getCardholderData().getEnabled());
            classicRbaCheck.setCardholderDataName(updateReqDto.getCardholderData().getName());
            classicRbaCheck.setCardholderDataEmail(updateReqDto.getCardholderData().getEmail());
            classicRbaCheck
              .setCardholderDataPostcode(updateReqDto.getCardholderData().getPostcode());
            classicRbaCheck.setCardholderDataPhone(updateReqDto.getCardholderData().getPhone());
        }
        return this;
    }

    private ClassicRbaService setCumulativeAmount(ClassicRbaCheckDO classicRbaCheck,
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        if (updateReqDto.getCumulativeAmount() == null) {
            classicRbaCheck.setCumulativeAmountEnabled(false);
        } else {
            classicRbaCheck
              .setCumulativeAmountEnabled(updateReqDto.getCumulativeAmount().getEnabled());
            classicRbaCheck
              .setCumulativeAmountPeriod(updateReqDto.getCumulativeAmount().getPeriod());
            classicRbaCheck
              .setCumulativeAmountAmount(updateReqDto.getCumulativeAmount().getAmount());
            classicRbaCheck.setCumulativeAmountCurrencyCode(String.valueOf(EnvironmentConstants.SYSTEM_CURRENCY_CODE));
            classicRbaCheck.setCumulativeAmountTxEnabled(
              updateReqDto.getCumulativeAmount().getTransactionCountEnabled());
            classicRbaCheck
              .setCumulativeAmountTxCount(updateReqDto.getCumulativeAmount().getTransactionCount());
        }
        return this;
    }

    private ClassicRbaService setCumulativeTransaction(ClassicRbaCheckDO classicRbaCheck,
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        if (updateReqDto.getCumulativeTransaction() == null) {
            classicRbaCheck.setCumulativeTransactionEnabled(false);
        } else {
            classicRbaCheck.setCumulativeTransactionEnabled(
              updateReqDto.getCumulativeTransaction().getEnabled());
            classicRbaCheck.setCumulativeTransactionCount(
              updateReqDto.getCumulativeTransaction().getTransactionCount());
        }

        return this;
    }

    private ClassicRbaService setLocationConsistency(ClassicRbaCheckDO classicRbaCheck,
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        if (updateReqDto.getLocationConsistency() == null) {
            classicRbaCheck.setLocationConsistencyEnabled(false);
        } else {
            classicRbaCheck
              .setLocationConsistencyEnabled(updateReqDto.getLocationConsistency().getEnabled());
            classicRbaCheck
              .setLocationConsistencyIp(updateReqDto.getLocationConsistency().getIpCountry());
            classicRbaCheck.setLocationConsistencyBrwTz(
              updateReqDto.getLocationConsistency().getBrowserTimeZone());
            classicRbaCheck.setLocationConsistencyBilling(
              updateReqDto.getLocationConsistency().getBillingCountry());
            classicRbaCheck.setLocationConsistencyShipping(
              updateReqDto.getLocationConsistency().getShippingCountry());
        }
        return this;
    }

    private ClassicRbaService setChannel(ClassicRbaCheckDO classicRbaCheck,
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        if (updateReqDto.getBrowserLanguage() == null) {
            classicRbaCheck.setBrowserLanguageEnabled(false);
        } else {
            classicRbaCheck
              .setBrowserLanguageEnabled(updateReqDto.getBrowserLanguage().getEnabled());
            classicRbaCheck.setBrowserLanguageList(
              String.join(",",
                Optional.ofNullable(updateReqDto.getBrowserLanguage().getBrowserLanguageList())
                  .orElse(Collections.emptyList())));
        }

        if (updateReqDto.getVpn() == null) {
            classicRbaCheck.setVpnEnabled(false);
        } else {
            classicRbaCheck.setVpnEnabled(updateReqDto.getVpn().getEnabled());
        }

        if (updateReqDto.getProxy() == null) {
            classicRbaCheck.setProxyEnabled(false);
        } else {
            classicRbaCheck.setProxyEnabled(updateReqDto.getProxy().getEnabled());
        }

        if (updateReqDto.getPrivateBrowsing() == null) {
            classicRbaCheck.setPrivateBrowsingEnabled(false);
        } else {
            classicRbaCheck
              .setPrivateBrowsingEnabled(updateReqDto.getPrivateBrowsing().getEnabled());
        }

        if (updateReqDto.getDeviceVariation() == null) {
            classicRbaCheck.setDeviceVariationEnabled(false);
        } else {
            classicRbaCheck
              .setDeviceVariationEnabled(updateReqDto.getDeviceVariation().getEnabled());
        }
        return this;
    }

    private ClassicRbaService setMcc(ClassicRbaCheckDO classicRbaCheck,
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        if (updateReqDto.getMcc() == null) {
            classicRbaCheck.setMccEnabled(false);
        } else {
            classicRbaCheck.setMccEnabled(updateReqDto.getMcc().getEnabled());
            classicRbaCheck.setMccList(String.join(",", updateReqDto.getMcc().getMccList()));
        }
        return this;
    }

    private ClassicRbaService setRecurringPayment(ClassicRbaCheckDO classicRbaCheck,
      ClassicRbaSettingUpdateReqDto updateReqDto) {
        if (updateReqDto.getRecurringPayment() == null) {
            classicRbaCheck.setRecurringPaymentEnabled(false);
        } else {
            classicRbaCheck
              .setRecurringPaymentEnabled(updateReqDto.getRecurringPayment().getEnabled());
            classicRbaCheck
              .setRecurringPaymentExpiration(updateReqDto.getRecurringPayment().getExpiration());
            classicRbaCheck
              .setRecurringPaymentFrequency(updateReqDto.getRecurringPayment().getFrequency());
        }
        return this;
    }

    public Optional<ClassicRbaReportDto> getClassicRbaReport(ClassicRbaReportReqDto queryDto) {
        try {
            if (queryDto.isMonthlyReport()) {
                return classicRbaReportDao.findByGroupByMonth(
                        queryDto.getYear(), queryDto.getMonth())
                  .map(ClassicRbaReportDto::valueOf);
            } else {
                return classicRbaReportDao.findByGroupByDayOfMonth(
                        queryDto.getYear(), queryDto.getMonth(), queryDto.getDay())
                  .map(ClassicRbaReportDto::valueOf);
            }
        } catch (DatabaseException e) {
            log.error("[getClassicRbaReport] get classic rba report error, issuerBankId={}, year={}, month={}, day={}", 
                    queryDto.getIssuerBankId(), queryDto.getYear(), queryDto.getMonth(), queryDto.getDay(), e);
            throw new OceanException(e.getResultStatus(), "Get classic rba report error");
        } catch (Exception e) {
            log.error("[getClassicRbaReport] get classic rba report error, issuerBankId={}, year={}, month={}, day={}", 
                    queryDto.getIssuerBankId(), queryDto.getYear(), queryDto.getMonth(), queryDto.getDay(), e);
            throw new OceanException(ResultStatus.SERVER_ERROR, "Get classic rba report error");
        }
    }

    @Transactional
    public boolean collectRbaReportData(String operator) {

        long createMillis = ZonedDateTime.now(EnvironmentConstants.ACS_TIMEZONE_ID).toInstant().toEpochMilli();
        try {
            classicRbaReportDao.deleteYesterdayReport();
            List<ClassicRbaReportDO> classicRbaReportList =
              classicRbaReportManager.queryClassicRbaReportDailyData().stream()
                .peek(e -> {
                    e.setDeleteFlag(false);
                    e.setCreateMillis(createMillis);
                    e.setSysCreator(operator);
                }).collect(Collectors.toList());

            return classicRbaReportDao.batchSave(classicRbaReportList);
        } catch (DatabaseException e) {
            String message = "[collectRbaReportData] collect classic rba report daily data fail";
            log.error(message);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, message);
        }
    }

    public void exportClassicRbaReport(ClassicRbaReportReqDto queryDto) {

        List<ClassicRbaStatus> classicRbaStatusList =
          getClassicRbaReport(queryDto).map(ClassicRbaReportDto::getClassicRbaStatusList)
            .orElse(Collections.emptyList());

        String fileName = ExcelBuildUtils.getFileNameFormat("classic_rba_report");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> header = Arrays
          .asList("風險評估名稱", "Frictionless(%)", "Challenge(%)", "Reject(%)");
        ExcelBuildUtils.createHeader(workbook, sheet, header);

        for (int rowNum = 1; rowNum <= classicRbaStatusList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            ClassicRbaStatus status = classicRbaStatusList.get(rowNum - 1);
            row.createCell(0).setCellValue(status.getName());
            row.createCell(1).setCellValue(status.getFrictionless());
            row.createCell(2).setCellValue(status.getChallenge());
            row.createCell(3).setCellValue(status.getReject());
        }
        ExcelBuildUtils.resizeColums(sheet, header.size());
        try {
            ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
        } catch (IOException e) {
            String message = "[exportClassicRbaReport] execute failed.";
            log.error(message, e);
            throw new OceanException(ResultStatus.IO_EXCEPTION, message);
        }

    }

}
