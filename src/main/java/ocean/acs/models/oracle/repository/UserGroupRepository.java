package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.UserGroup;

@Repository
public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {

    @Query(value = "select 1 from user_group where delete_flag = 0 and id = :id",
            nativeQuery = true)
    Optional<Integer> existsByIdAndNotDelete(@Param("id") Long id);

    @Query(value = "select 1 from user_group where delete_flag = 0 and issuer_bank_id = :issuerBankId "
            + "and name = :name", nativeQuery = true)
    Optional<Integer> existsByIssuerBankIdAndNameAndNotDelete(
            @Param("issuerBankId") Long issuerBankId, @Param("name") String name);

    @Query(value = "select * from user_group where delete_flag = 0 and id = :id",
            nativeQuery = true)
    Optional<UserGroup> findByIdAndNotDelete(@Param("id") Long id);

    @Query(value = "select b.* from account_group a\n" + "join User_group b on a.group_id = b.id\n"
            + "where a.account_id = ?1 and b.delete_flag=0", nativeQuery = true)
    List<UserGroup> findUserGroup(Long userAccountId);

    @Query(value = "select * from user_group up inner join account_group ap on up.id = ap.group_id "
            + "where up.delete_flag = 0 and ap.account_id = :userAccountId", nativeQuery = true)
    List<UserGroup> findByUserAccountIdAndNotDelete(@Param("userAccountId") Long userAccountId);

    /** 依issuerBankId查詢未刪除且非預設組織管理群組(USER_GROUP.TYPE != 'SYSTEM')的群組權限設定 */
    @Query(value = "select * from user_group " + " where delete_flag=0 " + " and type != 'SYSTEM' "
            + " and issuer_bank_id=?1 ", nativeQuery = true)
    List<UserGroup> findByIssuerBankIdAndNotDelete(Long issuerBankId);

    /** 以issuerBankId查詢未刪除的群組 */
    List<UserGroup> findByIssuerBankIdAndDeleteFlagFalse(Long issuerBankId);

    /** 以issuerBankId查詢為刪除的銀行會員預設群組權限 */
    @Query(value = "select * from user_group where delete_flag=0 " + " and type = 'SYSTEM' "
            + " and scope = 'BANK' " + " and issuer_bank_id=?1 ", nativeQuery = true)
    UserGroup findSysDefaultBankAdminUserGroupByIssuerBankId(Long issuerBankId);

    @Query(value = "select * from user_group where delete_flag=0 " + " and issuer_bank_id=?1"
            + " and name=?2 ", nativeQuery = true)
    UserGroup findByIssuerBankIdAndName(Long issuerBankId, String name);

    @Query(value = "select name from user_group where id = :id", nativeQuery = true)
    Optional<String> findGroupNameById(@Param("id") Long id);

}
