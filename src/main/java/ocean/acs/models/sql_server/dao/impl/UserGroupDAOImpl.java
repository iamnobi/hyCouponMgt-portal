package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.UserGroupDAO;
import ocean.acs.models.data_object.entity.UserGroupDO;
import ocean.acs.models.sql_server.entity.UserGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class UserGroupDAOImpl implements UserGroupDAO {

    private final ocean.acs.models.sql_server.repository.UserGroupRepository repo;

    @Override
    public Optional<UserGroupDO> save(UserGroupDO userGroupDO) {
        Objects.requireNonNull(userGroupDO);

        UserGroup userGroup = UserGroup.valueOf(userGroupDO);
        return Optional.ofNullable(repo.save(userGroup)).map(UserGroupDO::valueOf);
    }

    @Override
    public boolean existsByIdAndNotDelete(Long id) {
        try {
            Optional<Integer> existOpt = repo.existsByIdAndNotDelete(id);
            return existOpt.isPresent();
        } catch (Exception e) {
            log.error("[existsByIdAndNotDelete] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean existsByIssuerBankIdAndNameAndNotDelete(Long issuerBankId, String name) {
        try {
            Optional<Integer> existOpt =
              repo.existsByIssuerBankIdAndNameAndNotDelete(issuerBankId, name);
            return existOpt.isPresent();
        } catch (Exception e) {
            log.error("[existsByIssuerBankIdAndNameAndNotDelete] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<UserGroupDO> findByIdAndNotDelete(Long id) {
        try {
            return repo.findByIdAndNotDelete(id).map(UserGroupDO::valueOf);
        } catch (Exception e) {
            log.error("[findByIdAndNotDelete] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public List<UserGroupDO> findByUserAccountIdAndNotDelete(Long userAccountId) {
        try {
            return repo.findByUserAccountIdAndNotDelete(userAccountId).stream()
              .map(UserGroupDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByUserAccountIdAndNotDelete] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<UserGroupDO> findSysDefaultBankAdminByIssuerBankId(Long issuerBankId) {
        if (issuerBankId == null) {
            return Optional.empty();
        }

        UserGroup userGroup = repo.findSysDefaultBankAdminUserGroupByIssuerBankId(issuerBankId);
        return Optional.ofNullable(userGroup).map(UserGroupDO::valueOf);
    }

    @Override
    public Optional<UserGroupDO> findByIssuerBankIdAndName(Long issuerBankId, String name) {
        if (issuerBankId == null || StringUtils.isEmpty(name)) {
            return Optional.empty();
        }

        UserGroup userGroup = repo.findByIssuerBankIdAndName(issuerBankId, name);
        return Optional.ofNullable(userGroup).map(UserGroupDO::valueOf);
    }

    @Override
    public List<UserGroupDO> findUserGroupListByIssuerBankIdAndNotDelete(long issuerBankId) {
        try {
            return repo.findByIssuerBankIdAndNotDelete(issuerBankId).stream()
              .map(UserGroupDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findUserGroupListByIssuerBankIdAndNotDelete] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public List<UserGroupDO> findUserGroupListByIssuerBankId(Long issuerBankId) {
        try {
            return repo.findByIssuerBankIdAndDeleteFlagFalse(issuerBankId).stream()
              .map(UserGroupDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findUserGroupListByIssuerBankId] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public UserGroupDO delete(UserGroupDO userGroupDO, String deleter) {
        UserGroup userGroup = UserGroup.valueOf(userGroupDO);
        userGroup.setDeleteFlag(true);
        userGroup.setDeleteMillis(System.currentTimeMillis());
        userGroup.setDeleter(deleter);

        try {
            return UserGroupDO.valueOf(repo.save(userGroup));
        } catch (Exception e) {
            log.error("[delete] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<String> findGroupNameById(@Param("id") Long id) {
        return repo.findGroupNameById(id);
    }

    @Override
    public List<UserGroupDO> findUserGroup(Long userAccountId) {
        return repo.findUserGroup(userAccountId).stream()
          .map(UserGroupDO::valueOf).collect(Collectors.toList());
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        //TODO Implement method
        return 0;
    }


}
