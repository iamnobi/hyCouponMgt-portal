package ocean.acs.models.oracle.repository;

import ocean.acs.models.oracle.entity.Mcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MccRepository extends
    JpaRepository<Mcc, Long>,
    JpaSpecificationExecutor<Mcc> {
  @Modifying
  @Query(value = "delete from mcc", nativeQuery = true)
  void deleteAll();

}
