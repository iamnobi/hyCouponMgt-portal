package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.PageQueryDO;
import ocean.acs.models.data_object.portal.PortalIssuerBankDO;

public interface IssuerBankDAO {

    Optional<IssuerBankDO> findById(Long id);

    List<IssuerBankDO> findByIds(List<Long> idList) throws DatabaseException;

    Optional<IssuerBankDO> findByCode(String bankCode) throws DatabaseException;
    //00003 allow control asc/3ds oper id by user
    Optional<IssuerBankDO> findByAcsOperatorId(String acsOperatorId) throws DatabaseException;

    boolean existsByCodeAndDeleteFlagIsFalse(String bankCode) throws DatabaseException;

    Page<IssuerBankDO> findAll(PageQueryDO pageQueryDO) throws DatabaseException;

    /** 取得全部銀行（含組織）列表 */
    List<IssuerBankDO> findAll() throws DatabaseException;

    List<Long> findIdAll() throws DatabaseException;

    Optional<IssuerBankDO> add(PortalIssuerBankDO issuerBankDO);

    Optional<IssuerBankDO> update(PortalIssuerBankDO issuerBankDO);

    Optional<IssuerBankDO> delete(DeleteDataDO deleteDataDO);

    Long getAcsOperatorIdNextVal();
}
