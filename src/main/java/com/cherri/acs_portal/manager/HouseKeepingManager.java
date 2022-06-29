package com.cherri.acs_portal.manager;

import com.cherri.acs_portal.dto.housekeeping.BackupRowDto;
import com.cherri.acs_portal.dto.housekeeping.TabColumnDto;
import com.cherri.acs_portal.dto.housekeeping.TableColumnInfoDto;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ocean.acs.commons.enumerator.BackupTableEnum;

public interface HouseKeepingManager {

    List<TabColumnDto> getTabColumnList(Set<String> backupTableSet);

    Map<BackupTableEnum, Integer> getBackupTableForeignKeyDependencyLevel(
      Set<String> backupTableSet);

    List<BackupRowDto> getBackupRowDtoList(TableColumnInfoDto tableColumnInfoDto);

    boolean isDatabaseLinkExist();

    void createDatabaseLink();

    List<String> getProd41TableNameList();

    void createProd41BackupTables(Set<String> backupTableSet);

    void backupLogOverDaysToProd41(Set<String> backupTableSet);

    boolean isDatabaseLinkOpen();

    void closeDatabaseLink();

    void deleteLogOverDaysFromAcs(List<String> backupTableSet);

    void deleteLogOverDaysFromProd41(Set<String> backupTableSet);

}
