package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.models.data_object.entity.BlackListIpGroupDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.PortalBlackListIpGroupDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlackListIpGroupDAO {

    Optional<BlackListIpGroupDO> findById(Long id);

    void deleteByIds(DeleteDataDO deleteDO);

    BlackListIpGroupDO create(PortalBlackListIpGroupDO createDO, TransStatus transStatus,
      String creator);

    BlackListIpGroupDO update(BlackListIpGroupDO blackListIpGroupDO);

    void updateTransStatusByIssuerBankId(Long issuerBankId, TransStatus transStatus,
      String updater);

    List<BlackListIpGroupDO> findByIssuerBankId(Long issuerBankId);

    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

    Page<BlackListIpGroupDO> findAll(BlackListIpGroupDO blackListIpGroupDO, Pageable pageable);

    List<BlackListIpGroupDO> findByIdIn(List<Long> ids, Pageable pageable);

}
