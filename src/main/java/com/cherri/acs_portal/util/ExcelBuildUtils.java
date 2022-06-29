package com.cherri.acs_portal.util;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.google.common.net.UrlEscapers;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelBuildUtils {

    public static Font getHeaderFont(Workbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        return headerFont;
    }

    public static void createHeader(Workbook workbook, Sheet sheet, List<String> columns) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(getHeaderFont(workbook));

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum);
        for (int cellNum = 0; cellNum < columns.size(); cellNum++) {
            String name = columns.get(cellNum);
            Cell cell = headerRow.createCell(cellNum);
            cell.setCellValue(name);
            cell.setCellStyle(headerCellStyle);
        }
    }

    public static void resizeColums(Sheet sheet, int columnNamesSize) {
        for (int cellNum = 0; cellNum < columnNamesSize; cellNum++) {
            sheet.autoSizeColumn(cellNum);
        }
    }

    public static String getFileNameFormat(String fileName) {
        SimpleDateFormat smf = new SimpleDateFormat("yyMMddHHmmss");
        smf.setTimeZone(EnvironmentConstants.ACS_TIMEZONE);
        String dateStr = smf.format(new Date());
        fileName = String.format("%s_%s.xlsx", fileName, dateStr);
        return fileName;
    }

    public static void doExport(HttpServletResponse response, String fileName, Workbook workbook)
      throws IOException {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        // url encode filename
        String urlEncodeFilename = UrlEscapers.urlFragmentEscaper().escape(fileName);
        String contentDispositionHeaderValue =
            String.format(
                "attachment;filename=\"%s\";filename*=utf-8''%s", urlEncodeFilename, urlEncodeFilename);
        response.setHeader("Content-Disposition", contentDispositionHeaderValue);
        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
            out.flush();
            workbook.close();
        }
    }
}
