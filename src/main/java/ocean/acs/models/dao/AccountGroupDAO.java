package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.AccountGroupDO;

public interface AccountGroupDAO {

    AccountGroupDO save(AccountGroupDO accountGroupDO);

    AccountGroupDO add(AccountGroupDO accountGroupDO);

    boolean existById(Long id);

    boolean existByAccountIdAndGroupId(Long accountId, Long groupId);

    Optional<Long> findIdByAccountIdAndGroupId(Long accountId, Long groupId);

    Optional<AccountGroupDO> findById(Long id);

    long countByGroupId(Long groupId);

    void deleteById(Long accountGroupId);

    void deleteByAccountId(Long accountId);
    void deleteByGroupId(Long groupId);

    List<String> findUserListWhoIsOnlyInOneGroup(Long groupId);
}
