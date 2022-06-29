package com.cherri.acs_portal.manager;


import java.util.List;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ClassicRbaReportDO;

public interface ClassicRbaReportManager {

    List<ClassicRbaReportDO> queryClassicRbaReportDailyData() throws DatabaseException;
}
