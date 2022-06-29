package ocean.acs.models.oracle.repository;

import java.util.List;
import javax.transaction.Transactional;
import ocean.acs.models.oracle.entity.IssuerRuntimeProp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuerRuntimePropertyRepository extends CrudRepository<IssuerRuntimeProp, Long> {
  List<IssuerRuntimeProp> findByIssuerIdAndPluginId(Long issuerId, String pluginId);

  void deleteIssuerRuntimePropsByIssuerIdAndPluginId(Long issuerId, String pluginId);

  @Transactional
  @Modifying
  @Query(
      "update IssuerRuntimeProp c set c.deleteFlag = true, c.deleter = :deleter, c.deleteMillis = :deleteMillis where c.issuerId = :issuerBankId and c.deleteFlag = false")
  int deleteByIssuerBankId(
      @Param("issuerBankId") Long issuerBankId,
      @Param("deleter") String deleter,
      @Param("deleteMillis") long deleteMillis);
}
