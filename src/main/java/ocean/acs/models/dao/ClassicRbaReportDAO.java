package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ClassicRbaReportDO;

public interface ClassicRbaReportDAO {

    Optional<ClassicRbaReportDO> findByGroupByDayOfMonth(int year, int month, int dayOfMonth)
            throws DatabaseException;

    Optional<ClassicRbaReportDO> findByGroupByMonth(int year, int month) throws DatabaseException;

    void deleteYesterdayReport() throws DatabaseException;

    boolean batchSave(List<ClassicRbaReportDO> classicRbaReportDoList) throws DatabaseException;

}
