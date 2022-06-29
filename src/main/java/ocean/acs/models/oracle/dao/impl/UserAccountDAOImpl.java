package ocean.acs.models.oracle.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.UserAccountDAO;
import ocean.acs.models.data_object.portal.IssuerBankAdminDO;
import ocean.acs.models.data_object.portal.PageQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.UserAccountDO;
import ocean.acs.models.data_object.portal.UserPageQueryDO;
import ocean.acs.models.oracle.entity.UserAccount;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class UserAccountDAOImpl implements UserAccountDAO {

    private final ocean.acs.models.oracle.repository.UserAccountRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public UserAccountDO save(UserAccountDO userAccountDO) {
        return UserAccountDO.valueOf(repo.save(UserAccount.valueOf(userAccountDO)));
    }

    @Override
    public PagingResultDO<UserAccountDO> findUser(UserPageQueryDO query, Integer tryFailCount)
      throws DatabaseException {
        try {
            PageRequest page = PageRequest.of(query.getPage() - 1, query.getPageSize(),
              Sort.Direction.ASC, "account", "createMillis");

            Page<UserAccount> queryResult =
              repo.findAll((userAccountRoot, criteriaQuery, criteriaBuilder) -> {
                  List<Predicate> predList = new ArrayList<>();
                  Predicate pred;

                  if (null != query.getIssuerBankId()) {
                      pred = criteriaBuilder.equal(userAccountRoot.get("issuerBankId"),
                        query.getIssuerBankId());
                      predList.add(pred);
                  }

                  if (StringUtils.isNotBlank(query.getAccount())) {
                      pred = criteriaBuilder.like(userAccountRoot.get("account"),
                        "%" + query.getAccount().trim() + "%");
                      predList.add(pred);
                  }

                  if (null != tryFailCount) {
                      pred = criteriaBuilder.or(
                          criteriaBuilder.greaterThanOrEqualTo(userAccountRoot.get("tryFailCount"), tryFailCount),
                          criteriaBuilder.greaterThan(userAccountRoot.get("forgetMimaCount"), query.getForgetMimaCount()),
                          criteriaBuilder.greaterThan(userAccountRoot.get("sendOtpCount"), query.getSendOtpCount()),
                          criteriaBuilder.greaterThan(userAccountRoot.get("verifyOtpCount"), query.getVerifyOtpCount()),
                          criteriaBuilder.lessThanOrEqualTo(
                              userAccountRoot.get("lastLoginMillis"),
                              System.currentTimeMillis() - 1000 * 60 * 60 * 24 * new Long(query.getAccountMaxIdleDay())
                          )
                      );
                      predList.add(pred);
                  }

                  pred = criteriaBuilder.equal(userAccountRoot.get("deleteFlag"), 0);
                  predList.add(pred);

                  Predicate[] predicates = new Predicate[predList.size()];
                  return criteriaBuilder.and(predList.toArray(predicates));
              }, page);
            if (queryResult.getTotalElements() == 0) {
                return PagingResultDO.empty();
            }
            List<UserAccountDO> userAccountList = queryResult.getContent().stream()
              .map(UserAccountDO::valueOf).collect(Collectors.toList());
            return PagingResultDO.valueOf(
              new PageImpl<>(userAccountList, page, queryResult.getTotalElements()));
        } catch (Exception e) {
            log.error("[findUser] unknown exception, UserListQueryRequest={}", query, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<UserAccountDO> findByIdAndDeleteFlagFalse(Long id) {
        Optional<UserAccount> userAccountOptional = repo.findByIdAndDeleteFlagFalse(id);
        return userAccountOptional.map(UserAccountDO::valueOf);
    }

    @Override
    public Optional<UserAccountDO> findById(Long userId)
      throws DatabaseException {
        try {
            Optional<UserAccount> userAccountOptional = repo.findById(userId);
            return userAccountOptional.map(UserAccountDO::valueOf);
        } catch (Exception e) {
            log.error("[findOneUserIncludeDeleted] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public UserAccountDO saveOrUpdate(UserAccountDO userAccountDO) throws DatabaseException {
        try {
            return UserAccountDO.valueOf(repo.save(UserAccount.valueOf(userAccountDO)));
        } catch (Exception e) {
            log.error("[saveOrUpdate] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public List<UserAccountDO> findUserListByIssuerBankId(long issuerBankId) {
        try {
            List<UserAccount> userAccountList = repo
              .findByIssuerBankIdAndDeleteFlagFalse(issuerBankId);
            return userAccountList.isEmpty() ? Collections.emptyList()
              : userAccountList.stream().map(UserAccountDO::valueOf)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findUserListByIssuerBankId] unknown exception, issuerBankId={}",
              issuerBankId, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<UserAccountDO> add(IssuerBankAdminDO issuerBankAdminDO) {
        if (issuerBankAdminDO == null) {
            log.error(
              "[add] Failed in add issuer bank admin due to missing issuer bank admin content");
            throw new IllegalArgumentException(
              "Failed in add issuer bank admin due to missing issuer bank content.");
        }

        try {
            return Optional.of(this.saveOrUpdate(UserAccountDO.valueOf(issuerBankAdminDO)));
        } catch (Exception e) {
            log.error("[add] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<UserAccountDO> findByIssuerBankIdAndAccountAndDeleteFlagFalse(Long issuerBankId,
      String account) {
        try {
            return repo.findByIssuerBankIdAndAccountAndDeleteFlagFalse(issuerBankId, account)
              .map(UserAccountDO::valueOf);
        } catch (Exception e) {
            log.error("[findByIssuerBankIdAndAccount] unknown exception, issuerBankId={}",
              issuerBankId, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<Long> findIdByIssuerBankIdAndAccountAndNotDelete(Long issuerBankId,
      String account) {
        try {
            return repo.findIdByIssuerBankIdAndAccountAndDeleteFlagFalse(issuerBankId, account);
        } catch (Exception e) {
            log.error(
              "[findIdByIssuerBankIdAndAccountAndNotDelete] unknown exception, issuerBankId={}",
              issuerBankId, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByIssuerBankIdAndAccountAndDeleteFlagIsFalse(Long issuerBankId,
      String account) {
        try {
            return repo.existsByIssuerBankIdAndAccountAndDeleteFlagIsFalse(issuerBankId,
              account.trim());
        } catch (Exception e) {
            log.error(
              "[existsByIssuerBankIdAndAccountAndDeleteFlagIsFalse] unknown exception, issuerBankId={}",
              issuerBankId, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByAccountNotSelfAndAvailable(Long userAccountId, String account)
      throws OceanExceptionForPortal {
        try {
            return repo.existsByIdNotAndAccountAndDeleteFlagIsFalse(userAccountId, account.trim());
        } catch (Exception e) {
            log.error("[existsByAccountNotSelfAndAvailable] unknown exception, userAccountId={}",
              userAccountId, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Page<Object[]> findByUserGroupIdAndNotDelete(Long userGroupId,
      PageQueryDO pageQueryDto) {
        try {
            Pageable pageable = PageRequest.of(pageQueryDto.getPage() - 1,
              pageQueryDto.getPageSize(), Sort.Direction.ASC, "id");
            return repo.findByUserGroupIdAndNotDelete(userGroupId, pageable);
        } catch (Exception e) {
            log.error("[findByUserGroupIdAndNotDelete] unknown exception, userGropuId={}",
              userGroupId, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<UserAccountDO> update(IssuerBankAdminDO dto) {
        if (dto == null) {
            log.error(
              "[update] Failed in update issuer bank admin due to missing issuer bank admin content");
            throw new IllegalArgumentException(
              "Failed in update issuer bank admin due to missing issuer bank content.");
        }

        try {
            UserAccount userAccount = repo.findById(dto.getId()).map(e -> {
                // e.setAccount(dto.getAccount()); // 帳號不可以修改，若要修改需要先刪除後再重建，避免資料關聯錯誤
                e.setUsername(dto.getUserName());
                e.setDepartment(dto.getDepartment());
                e.setPhone(dto.getPhone());
                e.setExt(dto.getExt());
                e.setEmail(dto.getEmail());
                e.setAuditStatus(dto.getAuditStatus().getSymbol());
                e.setUpdater(dto.getUser());
                e.setUpdateMillis(System.currentTimeMillis());
                return e;
            }).map(repo::save).orElseThrow(() -> {
                log.error(
                  "[update] Failed in update issuer bank admin due to unknown issuer bank admin content with id={}",
                  dto.getId());
                return new OceanExceptionForPortal("Command failed in missing target content.");
            });

            return Optional.of(UserAccountDO.valueOf(userAccount));
        } catch (Exception e) {
            log.error("[update] unknown exception, userAccountId={}", dto.getId(), e);
            throw new OceanExceptionForPortal(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public int updateAuditStatusById(IssuerBankAdminDO issuerBankAdminDto) {
        return repo.updateAuditStatusById(issuerBankAdminDto.getId(), issuerBankAdminDto.getUser(),
          System.currentTimeMillis(), issuerBankAdminDto.getAuditStatus().getSymbol());
    }

    @Override
    public UserAccountDO delete(UserAccountDO userAccountDO) {
        UserAccount userAccount = UserAccount.valueOf(userAccountDO);
        userAccount.setDeleteFlag(true);
        userAccount.setDeleteMillis(System.currentTimeMillis());
        userAccount.setDeleter(userAccountDO.getDeleter());
        try {
            return UserAccountDO.valueOf(repo.save(userAccount));
        } catch (Exception e) {
            log.error("[delete] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public List<UserAccountDO> findBankAdminListByIssuerBankId(Long issuerBankId) {
        if (issuerBankId == null) {
            return Collections.emptyList();
        }
        return repo.findBankAdminListByIssuerBankId(issuerBankId).stream()
          .map(UserAccountDO::valueOf).collect(Collectors.toList());
    }

    @Override
    public Optional<String> findAccountNameById(Long id) {
        return repo.findAccountById(id);
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(UserAccount.class, issuerBankId, deleter, deleteMillis);
    }

    @Override
    public void increaseForgetMimaCount(long id, String updater, long updateMillis) {
        repo.increaseForgetMimaCount(id, updater, updateMillis);
    }
}
