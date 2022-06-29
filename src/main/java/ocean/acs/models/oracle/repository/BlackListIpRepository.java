package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.data_object.kernel.BlackListIpDO;
import ocean.acs.models.oracle.entity.BlackListIpGroup;

@Repository
public interface BlackListIpRepository extends CrudRepository<BlackListIpGroup, Long> {

    @Query("select new ocean.acs.models.data_object.kernel.BlackListIpDO(b.id, b.ip, b.cidr, b.transStatus) "
            + "from BlackListIpGroup b where b.issuerBankId=?1 and b.deleteFlag = false")
    List<BlackListIpDO> findIssuerBankIdAndIpAndCidrAndTransStatusByIssuerBankId(Long issuerBankId);
}
