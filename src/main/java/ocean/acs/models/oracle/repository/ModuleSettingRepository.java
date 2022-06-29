package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ModuleSetting;

@Repository
public interface ModuleSettingRepository extends CrudRepository<ModuleSetting, Long>,
        JpaSpecificationExecutor<ModuleSetting>, PagingAndSortingRepository<ModuleSetting, Long> {

    List<ModuleSetting> findByFunctionType(String functionType);

}
