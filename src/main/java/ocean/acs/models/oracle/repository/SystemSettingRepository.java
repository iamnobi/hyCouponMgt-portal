package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.SystemSetting;

@Repository
public interface SystemSettingRepository
        extends JpaRepository<SystemSetting, Long>, JpaSpecificationExecutor<SystemSetting> {

    List<SystemSetting> findByClassName(String className);

    Optional<SystemSetting> findByKey(String key);

    @Override
    @Query("select s from SystemSetting s where s.id = :id and s.deleteFlag = 0")
    Optional<SystemSetting> findById(@Param("id") Long id);

}
