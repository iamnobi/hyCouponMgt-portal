package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.UserGroupDO;

public interface UserGroupDAO {

    Optional<UserGroupDO> save(UserGroupDO userGroupDO);

    boolean existsByIdAndNotDelete(Long id);

    boolean existsByIssuerBankIdAndNameAndNotDelete(Long issuerBankId, String name);

    Optional<UserGroupDO> findByIdAndNotDelete(Long id);

    List<UserGroupDO> findByUserAccountIdAndNotDelete(Long userAccountId);

    /** 查詢系統預設銀行管理群組 */
    Optional<UserGroupDO> findSysDefaultBankAdminByIssuerBankId(Long issuerBankId);

    /** 查詢UserGroup群組 by issuer_bank_id and name */
    Optional<UserGroupDO> findByIssuerBankIdAndName(Long issuerBankId, String name);

    List<UserGroupDO> findUserGroupListByIssuerBankIdAndNotDelete(long issuerBankId);

    List<UserGroupDO> findUserGroupListByIssuerBankId(Long issuerBankId);

    UserGroupDO delete(UserGroupDO userGroupDO, String deleter);

    Optional<String> findGroupNameById(Long id);

    List<UserGroupDO> findUserGroup(Long userAccountId);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
