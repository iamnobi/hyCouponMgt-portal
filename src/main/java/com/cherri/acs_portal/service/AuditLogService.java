package com.cherri.acs_portal.service;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.usermanagement.AuditLogDto;
import com.cherri.acs_portal.dto.usermanagement.AuditLogListRequest;
import com.cherri.acs_portal.util.AcsPortalUtil;
import com.cherri.acs_portal.util.ExcelBuildUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.AuditLogDAO;
import ocean.acs.models.data_object.entity.AuditLogDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AuditLogService {

    private static final List<String> HEADER =
      Arrays.asList(
        "Method Name", "Action", "Error Code", "Value", "Create Millis", "IP", "Account");
    private AuditLogDAO repo;
    private HttpServletResponse response;

    @Autowired
    public AuditLogService(AuditLogDAO repo, HttpServletResponse response) {
        this.repo = repo;
        this.response = response;
    }

    public PagingResultDTO<AuditLogDto> listAuditLogByPage(AuditLogListRequest request)
      throws DatabaseException {
        Pageable pageRequest =
          PageRequest.of(
            request.getPage() - 1, request.getPageSize(), Sort.Direction.DESC, "createMillis");
        Page<AuditLogDO> auditLogPage =
          repo.findAll(
            (Specification<AuditLogDO>)
              (auditLogRoot, criteriaQuery, criteriaBuilder) ->
                getPredicateByRequest(request, criteriaBuilder, auditLogRoot),
            pageRequest);

        PagingResultDTO<AuditLogDto> resDto = new PagingResultDTO<>();
        resDto.setTotal(auditLogPage.getTotalElements());
        resDto.setTotalPages(auditLogPage.getTotalPages());
        resDto.setCurrentPage(auditLogPage.getNumber() + 1);
        List<AuditLogDto> data = auditLogPage.getContent().stream()
          .map(AuditLogDto::valueOf)
          .collect(Collectors.toList());
        resDto.setData(data);
        return resDto;
    }

    public void downloadAuditLog(AuditLogListRequest request)
      throws IOException, DatabaseException {
        String fileName = ExcelBuildUtils.getFileNameFormat("audit_log");
        List<AuditLogDO> dataList =
          repo.findAll(
            (Specification<AuditLogDO>)
              (auditLogRoot, criteriaQuery, criteriaBuilder) ->
                getPredicateByRequest(request, criteriaBuilder, auditLogRoot),
                  Sort.by(Sort.Direction.DESC, "createMillis"));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        ExcelBuildUtils.createHeader(workbook, sheet, HEADER);
        SimpleDateFormat createMillisDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        createMillisDateFormat.setTimeZone(EnvironmentConstants.ACS_TIMEZONE);
        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            AuditLogDO auditLog = dataList.get(rowNum - 1);
            String createDateTime = createMillisDateFormat
              .format(new Date(auditLog.getCreateMillis()));
            row.createCell(0).setCellValue(auditLog.getMethodName());
            row.createCell(1).setCellValue(auditLog.getAction());
            row.createCell(2).setCellValue(auditLog.getErrorCode());
            row.createCell(3).setCellValue(auditLog.getValue());
            row.createCell(4).setCellValue(createDateTime);
            row.createCell(5).setCellValue(auditLog.getIp());
            row.createCell(6).setCellValue(auditLog.getSysCreator());
        }
        ExcelBuildUtils.resizeColums(sheet, HEADER.size());
        ExcelBuildUtils.doExport(response, fileName, workbook);
    }

    private Predicate getPredicateByRequest(
      AuditLogListRequest request, CriteriaBuilder builder, Root<AuditLogDO> root) {

        Long issuerBankId = request.getIssuerBankId();
        String account = request.getAccount();
        String ip = request.getIp();
        String methodName = request.getMethodName();
        String action = request.getAction();
        String errorCode = request.getErrorCode();
        Long startMillis = request.getStartMillis();
        Long endMillis = request.getEndMillis();

        List<Predicate> predList = new ArrayList<>();
        if (!AcsPortalUtil.isFiscUser(issuerBankId)) {
            predList.add(builder.equal(root.get("issuerBankId"), issuerBankId));
        }
        if (!StringUtils.isEmpty(account)) {
            predList.add(builder.like(root.get("sysCreator"), "%" + account.trim() + "%"));
        }
        if (!StringUtils.isEmpty(ip)) {
            predList.add(builder.like(root.get("ip"), "%" + ip.trim() + "%"));
        }
        if (!StringUtils.isEmpty(methodName)) {
            predList.add(builder.like(root.get("methodName"), "%" + methodName.trim() + "%"));
        }
        if (!StringUtils.isEmpty(action)) {
            predList.add(builder.like(root.get("action"), "%" + action.trim().toUpperCase() + "%"));
        }
        if (!StringUtils.isEmpty(errorCode)) {
            predList.add(builder.like(root.get("errorCode"), "%" + errorCode.trim() + "%"));
        }
        if (startMillis != null) {
            predList.add(builder.greaterThanOrEqualTo(root.get("createMillis"), startMillis));
        }
        if (endMillis != null) {
            predList.add(builder.lessThanOrEqualTo(root.get("createMillis"), endMillis));
        }

        Predicate[] predicates = new Predicate[predList.size()];
        return builder.and(predList.toArray(predicates));
    }

    public void downloadAuditLogByIdList(List<Long> idList) throws IOException, DatabaseException {
        String fileName = ExcelBuildUtils.getFileNameFormat("audit_log");
        List<AuditLogDO> dataList = repo.findByIds(idList);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        ExcelBuildUtils.createHeader(workbook, sheet, HEADER);
        SimpleDateFormat createMillisDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        createMillisDateFormat.setTimeZone(EnvironmentConstants.ACS_TIMEZONE);
        // Create data body
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            AuditLogDO auditLog = dataList.get(rowNum - 1);
            String createDateTime = createMillisDateFormat
              .format(new Date(auditLog.getCreateMillis()));
            row.createCell(0).setCellValue(auditLog.getMethodName());
            row.createCell(1).setCellValue(auditLog.getAction());
            row.createCell(2).setCellValue(auditLog.getErrorCode());
            row.createCell(3).setCellValue(auditLog.getValue());
            row.createCell(4).setCellValue(createDateTime);
            row.createCell(5).setCellValue(auditLog.getIp());
            row.createCell(6).setCellValue(auditLog.getSysCreator());
        }
        ExcelBuildUtils.resizeColums(sheet, HEADER.size());
        ExcelBuildUtils.doExport(response, fileName, workbook);
    }
}
