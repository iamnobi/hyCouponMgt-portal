package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ocean.acs.models.sql_server.entity.AccountGroup;

@Repository
public interface AccountGroupRepository extends CrudRepository<AccountGroup, Long> {

    @Query("select ag from AccountGroup ag where ag.accountId = :accountId and ag.groupId = :groupId")
    Optional<Integer> existByAccountIdAndGroupId(@Param("accountId") Long accountId,
            @Param("groupId") Long groupId);

    @Query("select ag.id from AccountGroup ag where ag.accountId = :accountId and ag.groupId = :groupId")
    Optional<Long> findIdByAccountIdAndGroupId(@Param("accountId") Long accountId,
            @Param("groupId") Long groupId);

    @Query("select count(ag) from AccountGroup ag where ag.groupId = :groupId")
    long countByGroupId(@Param("groupId") Long groupId);

    @Modifying
    @Transactional
    @Query("delete from AccountGroup ag where ag.accountId = :accountId and ag.groupId = :groupId")
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
