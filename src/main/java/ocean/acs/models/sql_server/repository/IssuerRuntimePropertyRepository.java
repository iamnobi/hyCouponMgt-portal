package ocean.acs.models.sql_server.repository;

import java.util.List;
import ocean.acs.models.sql_server.entity.IssuerRuntimeProp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuerRuntimePropertyRepository extends CrudRepository<IssuerRuntimeProp, Long> {

    List<IssuerRuntimeProp> findByIssuerIdAndPluginId(Long issuerId, String pluginId);

    void deleteIssuerRuntimePropsByIssuerIdAndPluginId(Long issuerId, String pluginId);

}
