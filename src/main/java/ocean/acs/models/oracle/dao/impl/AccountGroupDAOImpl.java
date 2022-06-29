package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.AccountGroupDAO;
import ocean.acs.models.data_object.entity.AccountGroupDO;
import ocean.acs.models.oracle.entity.AccountGroup;
import ocean.acs.models.oracle.entity.UserAccount;
import ocean.acs.models.oracle.repository.UserAccountRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class AccountGroupDAOImpl implements AccountGroupDAO {

  private final ocean.acs.models.oracle.repository.AccountGroupRepository repo;
  private final UserAccountRepository userAccountRepository;

  @Override
  public AccountGroupDO save(AccountGroupDO accountGroupDO) {
    try {
      return AccountGroupDO.valueOf(repo.save(AccountGroup.valueOf(accountGroupDO)));
    } catch (Exception e) {
      log.error("[save] unknown exception", e);
      throw new OceanExceptionForPortal(ResultStatus.DB_SAVE_ERROR, e.getMessage());
    }
  }

  @Override
  public AccountGroupDO add(AccountGroupDO accountGroupDO) {
    AccountGroup accountGroup = new AccountGroup();
    accountGroup.setAccountId(accountGroupDO.getAccountId());
    accountGroup.setGroupId(accountGroupDO.getGroupId());
    accountGroup.setAuditStatus(accountGroupDO.getAuditStatus().getSymbol());
    accountGroup.setCreator(accountGroupDO.getCreator());
    accountGroup.setCreateMillis(accountGroupDO.getCreateMillis());
    return AccountGroupDO.valueOf(repo.save(AccountGroup.valueOf(accountGroupDO)));
  }

  @Override
  public boolean existById(Long id) {
    return repo.existsById(id);
  }

  @Override
  public boolean existByAccountIdAndGroupId(Long accountId, Long groupId) {
    try {
      Optional<Integer> existOpt =
        repo.existByAccountIdAndGroupId(accountId, groupId);
      return existOpt.isPresent();
    } catch (Exception e) {
      log.error("[existByAccountIdAndGroupId] unknown exception, accountId={}, groupId={}",
        accountId, groupId, e);
      throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }

  @Override
  public Optional<Long> findIdByAccountIdAndGroupId(Long accountId, Long groupId) {
    try {
      return repo.findIdByAccountIdAndGroupId(accountId, groupId);
    } catch (Exception e) {
      log.error("[findIdByAccountIdAndGroupId] unknown exception, accountId={}, groupId={}",
        accountId, groupId, e);
      throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }

  @Override
  public Optional<AccountGroupDO> findById(Long id) {
    try {
      Optional<AccountGroup> accountGroupOptional = repo.findById(id);
      return accountGroupOptional.map(AccountGroupDO::valueOf);
    } catch (Exception e) {
      log.error("[findById] unknown exception, id={}", id, e);
      throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }

  @Override
  public long countByGroupId(Long groupId) {
    try {
      return repo.countByGroupId(groupId);
    } catch (Exception e) {
      log.error("[countByGroupId] unknown exception, groupId={}", groupId, e);
      throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
    }
  }

  @Override
  public void deleteById(Long accountGroupId) {
    repo.deleteById(accountGroupId);
  }

  @Override
  public void deleteByAccountId(Long accountId) {
    repo.deleteByAccountId(accountId);
  }

  @Override
  public void deleteByGroupId(Long groupId) {
    repo.deleteByGroupId(groupId);
  }

  @Override
  public List<String> findUserListWhoIsOnlyInOneGroup(Long groupId) {
    List<Long> accountIdList = repo.findAccountIdListWhoIsOnlyInOneGroup(groupId);
    return IterableUtils
        .toList(userAccountRepository.findAllById(accountIdList))
        .stream().map(UserAccount::getAccount)
        .collect(Collectors.toList());
  }
}
