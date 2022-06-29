package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import ocean.acs.models.sql_server.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository
  extends CrudRepository<UserAccount, Long>, JpaSpecificationExecutor<UserAccount> {

    Optional<UserAccount> findByIdAndDeleteFlagFalse(Long id);

    Optional<UserAccount> findByIdAndIssuerBankId(Long id, Long issuerBankId);

    Optional<UserAccount> findByIssuerBankIdAndAccountAndDeleteFlagFalse(Long issuerBankId,
      String account);

    List<UserAccount> findByIssuerBankIdAndDeleteFlagFalse(Long issuerBankId);

    boolean existsByIssuerBankIdAndAccountAndDeleteFlagIsFalse(Long issuerBankId, String account);

    boolean existsByAccountAndDeleteFlagIsFalse(String account);

    boolean existsByIdNotAndAccountAndDeleteFlagIsFalse(Long userAccountId, String account);

    @Query(value = "select id from user_account where issuer_bank_id = ?1 and account = ?2 and delete_flag = 0", nativeQuery = true)
    Optional<Long> findIdByIssuerBankIdAndAccountAndDeleteFlagFalse(Long issuerBankId,
      String account);

    @Query(value =
      "select ua.account, ua.username, agp.auditStatus from UserAccount ua join AccountGroup agp "
        + "on ua.id = agp.accountId where agp.groupId = :userGroupId and ua.deleteFlag = 0")
    Page<Object[]> findByUserGroupIdAndNotDelete(@Param("userGroupId") Long userGroupId,
      Pageable pageable);

    @Query(value =
      "select ua.* from user_account ua join account_group ag on ua.id = ag.account_id "
        + "join user_group ug on ag.group_id = ug.id where ua.issuer_bank_id = ?1 and ug.type = 'SYSTEM' "
        + "and ug.scope = 'BANK' and ua.delete_flag = 0", nativeQuery = true)
    List<UserAccount> findBankAdminListByIssuerBankId(Long issuerBankId);

    @Modifying
    @Query("update UserAccount u set u.auditStatus = :auditStatus, u.updater = :updater, updateMillis = :updateMillis where u.id = :id")
    int updateAuditStatusById(@Param("id") Long id, @Param("updater") String updater,
      @Param("updateMillis") long updateMillis, @Param("auditStatus") String auditStatus);

    Optional<String> findAccountById(Long id);

    @Transactional
    @Modifying
    @Query("update UserAccount u "
        + "set u.forgetMimaCount = u.forgetMimaCount + 1 , u.updater = :updater, u.updateMillis = :updateMillis "
        + "where u.id = :id")
    void increaseForgetMimaCount(
        @Param("id") long id,
        @Param("updater") String updater,
        @Param("updateMillis") long updateMillis);

}
