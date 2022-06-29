package com.cherri.acs_portal.service;

import com.cherri.acs_portal.annotation.LogInfo;
import com.cherri.acs_portal.config.FiscProperties;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.SystemConstants;
import com.cherri.acs_portal.dto.housekeeping.BackupRowDto;
import com.cherri.acs_portal.dto.housekeeping.TabColumnDto;
import com.cherri.acs_portal.dto.housekeeping.TableColumnInfoDto;
import com.cherri.acs_portal.manager.HouseKeepingManager;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.JDBCType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.BackupTableEnum;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class HouseKeepingJobService {

    @Autowired
    private HouseKeepingManager houseKeepingManager;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private FiscProperties fiscProperties;

    @Transactional
    @LogInfo(end = true)
    public boolean housekeeping(String operator) {
        Set<String> backupTableSet = BackupTableEnum.BACKUP_TABLE_SET;
        uploadZipFile(backupTableSet);
        move(backupTableSet);
        clean(backupTableSet);
        return true;
    }

    @LogInfo(end = true)
    public void uploadZipFile(Set<String> backupTableSet) {
        List<TabColumnDto> tabColumnDtoList = getBackupTableColumnInfo(backupTableSet);
        List<TableColumnInfoDto> tableColumnInfoDtoList =
          convertToTableColumnInfoDto(tabColumnDtoList, backupTableSet);
        List<List<BackupRowDto>> resultForEachTable = getResultForEachStable(
          tableColumnInfoDtoList);
        writeZipFile(resultForEachTable);
    }

    /**
     * 取得備份資料表欄位名稱及型態
     */
    private List<TabColumnDto> getBackupTableColumnInfo(Set<String> backupTableSet) {
        return new ArrayList<>(houseKeepingManager.getTabColumnList(backupTableSet));
    }

    private List<TableColumnInfoDto> convertToTableColumnInfoDto(
      List<TabColumnDto> tabColumnDtoList,
      Set<String> backupTableSet) {
        Map<BackupTableEnum, Integer> dependencyLevel =
          houseKeepingManager.getBackupTableForeignKeyDependencyLevel(backupTableSet);

        Map<String, TableColumnInfoDto> tableColumnInfoDtoMap = new HashMap<>();
        tabColumnDtoList.forEach(e -> {
            Integer level = dependencyLevel.get(BackupTableEnum.valueOf(e.getTableName()));
            if (tableColumnInfoDtoMap.get(e.getTableName()) == null) {
                tableColumnInfoDtoMap.put(
                  e.getTableName(),
                  createTableColumnInfoDto(e.getTableName(), level));
            }
            tableColumnInfoDtoMap.get(e.getTableName())
              .getColumnInfoDtoList()
              .add(createColumnInfoDto(e));
            if (isBlob(e)) {
                tableColumnInfoDtoMap.get(e.getTableName()).setHasBlob(true);
            }
        });
        List<TableColumnInfoDto> tableColumnInfoDtoList = new ArrayList<>(
          tableColumnInfoDtoMap.values());
        tableColumnInfoDtoList.sort(
          (o1, o2) -> o2.getLevel() - o1.getLevel()); // sort level order by desc (e.g.2 > 1 > 0)
        return tableColumnInfoDtoList;
    }

    private boolean isBlob(TabColumnDto tabColumnDto) {
        return JDBCType.BLOB.getName().equalsIgnoreCase(tabColumnDto.getDataType());
    }

    private TableColumnInfoDto createTableColumnInfoDto(String tableName, Integer level) {
        if (level == null) {
            level = 0; // no foreign key reference
        }
        return TableColumnInfoDto.builder()
          .tableName(tableName)
          .level(level)
          .columnInfoDtoList(new ArrayList<>())
          .build();
    }

    private TableColumnInfoDto.ColumnInfoDto createColumnInfoDto(TabColumnDto tabColumnDto) {
        return new TableColumnInfoDto.ColumnInfoDto(
          tabColumnDto.getColumnName(),
          tabColumnDto.getDataType());
    }

    private List<List<BackupRowDto>> getResultForEachStable(
      List<TableColumnInfoDto> tableColumnInfoDtoList) {

        return tableColumnInfoDtoList.stream()
          .map(tableColumnInfoDto ->
            houseKeepingManager.getBackupRowDtoList(tableColumnInfoDto))
          .collect(Collectors.toList());
    }

    private void writeZipFile(List<List<BackupRowDto>> resultForEachTable) {
        String filePath = fiscProperties.getHouseKeeping().getFileUploadPath();
        try (
          FileOutputStream fos = new FileOutputStream(createFileObject(filePath));
          BufferedOutputStream bos = new BufferedOutputStream(fos);
          ZipOutputStream zos = new ZipOutputStream(bos);
        ) {
            writeInsertStatements(resultForEachTable, zos);
            writeBinaryFile(resultForEachTable, zos);
        } catch (IOException e) {
            FileUtils.deleteQuietly(createFileObject(filePath));
            String message = String
              .format("[writeZipFile] Upload file failed, write out path=%s", filePath);
            log.error(message, e);
            throw new OceanException(message);
        }
    }

    private List<List<String>> getInsertStatementForEachTable(
      List<List<BackupRowDto>> resultForEachTable) {
        return resultForEachTable.stream()
          .map(e -> e.stream()
            .map(BackupRowDto::getInsertStatement)
            .collect(Collectors.toList())
          )
          .collect(Collectors.toList());
    }

    private void writeInsertStatements(
      List<List<BackupRowDto>> resultForEachTable,
      ZipOutputStream zos)
      throws IOException {

        List<List<String>> insertStatementForEachTable =
          getInsertStatementForEachTable(resultForEachTable);

        zos.putNextEntry(new ZipEntry(getFileName() + ".txt"));
        for (List<String> insertStatementOfOneTable : insertStatementForEachTable) {
            for (String insertStatement : insertStatementOfOneTable) {
                byte[] data = (insertStatement + System.lineSeparator()).getBytes();
                zos.write(data, 0, data.length);
            }
        }
        zos.closeEntry();
    }

    private void writeBinaryFile(List<List<BackupRowDto>> resultForEachTable, ZipOutputStream zos)
      throws IOException {
        for (List<BackupRowDto> backupRowDtoList : resultForEachTable) {
            for (BackupRowDto backupRowDto : backupRowDtoList) {
                if (backupRowDto.getFile() == null) {
                    continue;
                }

                zos.putNextEntry(
                  new ZipEntry(getBinaryFileName(
                    backupRowDto.getTableName(),
                    backupRowDto.getId())
                  )
                );
                zos.write(backupRowDto.getFile());
                zos.closeEntry();
            }
        }
    }

    private String getBinaryFileName(String tableName, String rowId) {
        return tableName + "-" + rowId + ".bin"; // e.g. AUDITING-19999.bin
    }

    private File createFileObject(String filePath) {
        String fileName = getFileName() + ".zip";
        try {
            FileUtils.forceMkdir(new File(filePath));
        } catch (IOException e) {
            log.error("[createFileObject] Create file object failed, filePath={}", filePath, e);
        }
        log.debug("[createFileObject] Create file object with fileName={}", fileName);
        return new File(filePath + File.separator + fileName);
    }

    /**
     * e.g. acs2_forever_20200415
     */
    private String getFileName() {
        LocalDateTime today = LocalDateTime.now(EnvironmentConstants.ACS_TIMEZONE_ID);
        return "acs2_forever_" + today.format(SystemConstants.DATE_TIME_FORMATTER_YYYYMMDD);
    }

    @LogInfo(end = true)
    public void move(Set<String> backupTableSet) {
        createDatabaseLink();
        createBackupTable(backupTableSet);
        houseKeepingManager.backupLogOverDaysToProd41(backupTableSet);
        List<String> backupTableList =
          sortedByForeignKeyDependencyOrderByAsc(backupTableSet);
        houseKeepingManager.deleteLogOverDaysFromAcs(backupTableList);
        closeDatabaseLink();
    }

    private List<String> sortedByForeignKeyDependencyOrderByAsc(Set<String> backupTableSet) {
        Map<BackupTableEnum, Integer> dependencyLevel =
          houseKeepingManager.getBackupTableForeignKeyDependencyLevel(backupTableSet);
        List<TableColumnInfoDto> backupTableInfoList =
          backupTableSet.stream().map(
            e -> createTableColumnInfoDto(e, dependencyLevel.get(BackupTableEnum.valueOf(e))))
            .sorted(Comparator.comparingInt(TableColumnInfoDto::getLevel)) // (e.g.0 > 1 > 2)
            .collect(Collectors.toList());

        return backupTableInfoList.stream()
          .map(TableColumnInfoDto::getTableName)
          .collect(Collectors.toList());
    }

    void createDatabaseLink() {
        if (houseKeepingManager.isDatabaseLinkExist()) {
            return;
        }
        houseKeepingManager.createDatabaseLink();
        if (!houseKeepingManager.isDatabaseLinkExist()) {
            throw new OceanException("Database Link is unavailable");
        }
    }

    void createBackupTable(Set<String> backupTableSet) {
        Set<String> copy = new HashSet<>(backupTableSet);
        houseKeepingManager.getProd41TableNameList()
          .forEach(copy::remove); // remove exist tables from backupTableSet
        houseKeepingManager.createProd41BackupTables(copy);
    }

    void closeDatabaseLink() {
        if (houseKeepingManager.isDatabaseLinkOpen()) {
            houseKeepingManager.closeDatabaseLink();
        }
    }

    @LogInfo(end = true)
    public void clean(Set<String> backupTableSet) {
        houseKeepingManager.deleteLogOverDaysFromProd41(backupTableSet);
    }

}
