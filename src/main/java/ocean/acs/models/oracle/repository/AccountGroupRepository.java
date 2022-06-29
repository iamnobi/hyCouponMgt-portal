package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ocean.acs.models.oracle.entity.AccountGroup;

@Repository
public interface AccountGroupRepository extends CrudRepository<AccountGroup, Long> {

    @Query(value = "select 1 from account_group where account_id = :accountId and group_id = :groupId",
            nativeQuery = true)
    Optional<Integer> existByAccountIdAndGroupId(@Param("accountId") Long accountId,
            @Param("groupId") Long groupId);

    @Query(value = "select id from account_group where account_id = :accountId and group_id = :groupId",
            nativeQuery = true)
    Optional<Long> findIdByAccountIdAndGroupId(@Param("accountId") Long accountId,
            @Param("groupId") Long groupId);

    @Query(value = "select count(id) from account_group where group_id = :groupId",
            nativeQuery = true)
    long countByGroupId(@Param("groupId") Long groupId);

    @Modifying
    @Transactional
    @Query(value = "delete account_group where account_id = :accountId and group_id = :groupId",
            nativeQuery = true)
    void deleteByAccountIdAndGroupId(@Param("accountId") Long accountId,
            @Param("groupId") Long groupId);

    void deleteByAccountId(Long accountId);

    void deleteByGroupId(Long groupId);

    // 1. 找出 group 中的 accountId list (select accountId from AccountGroup where groupId = :groupId)
    // 2. 計算這些 accountId 所屬的 group 數量 (group by)
    // 2. 找出 group 數量 = 1 的 accountId (having count(a) = 1)
    @Query(
        "select a.accountId from AccountGroup a "
            + "where a.accountId in (select accountId from AccountGroup where groupId = :groupId) "
            + "group by a.accountId "
            + "having count(a) = 1")
    List<Long> findAccountIdListWhoIsOnlyInOneGroup(Long groupId);
}
