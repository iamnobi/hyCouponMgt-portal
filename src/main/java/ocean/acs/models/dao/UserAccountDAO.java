package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.data_object.portal.IssuerBankAdminDO;
import ocean.acs.models.data_object.portal.PageQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.UserAccountDO;
import ocean.acs.models.data_object.portal.UserPageQueryDO;
import org.springframework.data.domain.Page;

public interface UserAccountDAO {

    UserAccountDO save(UserAccountDO userAccountDO);

    PagingResultDO<UserAccountDO> findUser(UserPageQueryDO query, Integer tryFailCount)
      throws DatabaseException;

    Optional<UserAccountDO> findByIdAndDeleteFlagFalse(Long id);

    /** 查詢單一使用者（含已刪除） */
    Optional<UserAccountDO> findById(Long id) throws DatabaseException;

    UserAccountDO saveOrUpdate(UserAccountDO userAccount) throws DatabaseException;

    List<UserAccountDO> findUserListByIssuerBankId(long issuerBankId);

    Optional<UserAccountDO> add(IssuerBankAdminDO issuerBankAdminDO);

    Optional<UserAccountDO> findByIssuerBankIdAndAccountAndDeleteFlagFalse(Long issuerBankId,
      String account);

    Optional<Long> findIdByIssuerBankIdAndAccountAndNotDelete(Long issuerBankId, String account);


    boolean existsByIssuerBankIdAndAccountAndDeleteFlagIsFalse(Long issuerBankId, String account);

    boolean existsByAccountNotSelfAndAvailable(Long userAccountId, String account)
      throws OceanExceptionForPortal;

    Page<Object[]> findByUserGroupIdAndNotDelete(Long userGroupId, PageQueryDO pageQueryDO);

    /** 修改銀行管理員資料 */
    Optional<UserAccountDO> update(IssuerBankAdminDO issuerBankAdminDO);

    int updateAuditStatusById(IssuerBankAdminDO issuerBankAdminDO);

    UserAccountDO delete(UserAccountDO userAccount);

    /** 查詢會員銀行管理員 */
    List<UserAccountDO> findBankAdminListByIssuerBankId(Long issuerBankId);

    Optional<String> findAccountNameById(Long id);

    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

    void increaseForgetMimaCount(long id, String updater, long updateMillis);

}
