package com.cherri.acs_portal.service;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.cardholder.HolderIdQueryDTO;
import com.cherri.acs_portal.dto.cardholder.ThreeDSVerifiedLogDTO;
import com.cherri.acs_portal.util.ExcelBuildUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.OtpOperationLogDAO;
import ocean.acs.models.dao.ThreeDSVerifiedDAO;
import ocean.acs.models.data_object.entity.OtpOperationLogDO;
import ocean.acs.models.data_object.portal.HolderIdQueryDO;
import ocean.acs.models.data_object.portal.PageQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.PortalThreeDSVerifiedLogDO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OperationLogService {

    private final ThreeDSVerifiedDAO threeDSVerifiedDAO;
    private final OtpOperationLogDAO otpOperationLogDAO;

    @Autowired
    public OperationLogService(
      ThreeDSVerifiedDAO threeDSVerifiedDAO, OtpOperationLogDAO otpOperationLogDAO) {
        this.threeDSVerifiedDAO = threeDSVerifiedDAO;
        this.otpOperationLogDAO = otpOperationLogDAO;
    }

    public PagingResultDTO<ThreeDSVerifiedLogDTO> getThreeDSVerifiedLogByCardHolderId(
      HolderIdQueryDTO queryDTO) {
        final Long cardHolderId = queryDTO.getPanId();

        PageQueryDO pageQueryDO = PageQueryDO.builder().page(queryDTO.getPage())
          .pageSize(queryDTO.getPageSize()).build();
        PagingResultDO<PortalThreeDSVerifiedLogDO> resultDTO =
          threeDSVerifiedDAO
            .getInfoByCardHolderIdAndIssuerBankId(cardHolderId, queryDTO.getIssuerBankId(),
              pageQueryDO);

        List<ThreeDSVerifiedLogDTO> list = resultDTO.getData().stream()
          .map(ThreeDSVerifiedLogDTO::valueOf).collect(
            Collectors.toList());

        PagingResultDTO<ThreeDSVerifiedLogDTO> logPageResult = new PagingResultDTO<>();
        logPageResult.setData(list);
        logPageResult.setTotal(resultDTO.getTotal());
        logPageResult.setTotalPages(resultDTO.getTotalPages());
        logPageResult.setCurrentPage(resultDTO.getCurrentPage());
        return logPageResult;
    }

    public void exportThreeDSVerifiedLog(HolderIdQueryDTO queryDTO, HttpServletResponse rps)
      throws IOException {
        PagingResultDTO<ThreeDSVerifiedLogDTO> logResultDTO =
          getThreeDSVerifiedLogByCardHolderId(queryDTO);
        if (logResultDTO != null) {
            exportThreeDSVerifiedLog(logResultDTO.getData(), rps);
        } else {
            log.error(
              "[exportThreeDSVerifiedLog] Failed in fetch three DS verified operation log. cardHolderId={}",
              queryDTO.getPanId());
        }
    }

    private static final List<String> THRESS_DS_VERIFIED_LOG_CSV_COLUMN_NAMES =
      Arrays.asList("設定時間", "信用卡號", "設定狀態", "設定人員");

    private void exportThreeDSVerifiedLog(
      List<ThreeDSVerifiedLogDTO> dataList, HttpServletResponse rps) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);

        ExcelBuildUtils.createHeader(workbook, sheet, THRESS_DS_VERIFIED_LOG_CSV_COLUMN_NAMES);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(EnvironmentConstants.ACS_TIMEZONE);

        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            ThreeDSVerifiedLogDTO operationLog = dataList.get(rowNum - 1);
            Date date = new Date(operationLog.getOperationTime());
            String createDateTime = sdf.format(date);
            row.createCell(0).setCellValue(createDateTime);
            row.createCell(1).setCellValue(operationLog.getCardNumber());
            row.createCell(2).setCellValue(operationLog.getLockStatus() ? "啟用" : "停用");
            row.createCell(3).setCellValue(operationLog.getCreator());
        }
        ExcelBuildUtils.resizeColums(sheet, THRESS_DS_VERIFIED_LOG_CSV_COLUMN_NAMES.size());
        String fileName = ExcelBuildUtils.getFileNameFormat("ThreeDSVerified_Log");
        ExcelBuildUtils.doExport(rps, fileName, workbook);
    }

    public PagingResultDTO<OtpOperationLogDO> getOtpLogByPanId(HolderIdQueryDTO queryDTO) {
        HolderIdQueryDO holderIdQueryDO = HolderIdQueryDO.builder()
          .page(queryDTO.getPage()).pageSize(queryDTO.getPageSize())
          .issuerBankId(queryDTO.getIssuerBankId()).panId(queryDTO.getPanId())
          .build();
        PagingResultDO<OtpOperationLogDO> resultDO = otpOperationLogDAO
          .getByPanIdAndIssuerBankId(holderIdQueryDO);
        List<OtpOperationLogDO> list = resultDO.getData().stream().map(OtpOperationLogDO::valueOf)
          .collect(Collectors.toList());
        PagingResultDTO<OtpOperationLogDO> pagingResultDTO = new PagingResultDTO<>();
        pagingResultDTO.setData(list);
        pagingResultDTO.setTotal(resultDO.getTotal());
        pagingResultDTO.setTotalPages(resultDO.getTotalPages());
        pagingResultDTO.setCurrentPage(resultDO.getCurrentPage());
        return pagingResultDTO;
    }

    public void exportOtpOperationLog(HolderIdQueryDTO queryDTO, HttpServletResponse rps)
      throws IOException {
        PagingResultDTO<OtpOperationLogDO> logResultDTO =
          getOtpLogByPanId(queryDTO);
        if (logResultDTO != null) {
            exportOtpOperationLog(logResultDTO.getData(), rps);
        } else {
            log.error("[exportOtpOperationLog] Failed in fetch OTP operation log. cardHolderId={}",
              queryDTO.getPanId());
        }
    }

    private static final List<String> OTP_OPERATION_LOG_CSV_COLUMN_NAMES =
      Arrays.asList("設定時間", "設定人員");

    private void exportOtpOperationLog(List<OtpOperationLogDO> dataList, HttpServletResponse rps)
      throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);

        ExcelBuildUtils.createHeader(workbook, sheet, OTP_OPERATION_LOG_CSV_COLUMN_NAMES);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(EnvironmentConstants.ACS_TIMEZONE);

        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            OtpOperationLogDO operationLog = dataList.get(rowNum - 1);
            Date date = new Date(operationLog.getCreateMillis());
            String createDateTime = sdf.format(date);
            row.createCell(0).setCellValue(createDateTime);
            row.createCell(1).setCellValue(operationLog.getCreator());
        }
        ExcelBuildUtils.resizeColums(sheet, OTP_OPERATION_LOG_CSV_COLUMN_NAMES.size());
        String fileName = ExcelBuildUtils.getFileNameFormat("OTP_Operation_Log");
        ExcelBuildUtils.doExport(rps, fileName, workbook);
    }
}
