package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.PageQueryDO;
import ocean.acs.models.data_object.portal.PortalIssuerBankDO;
import ocean.acs.models.sql_server.entity.IssuerBank;

@Log4j2
@Repository
@AllArgsConstructor
public class IssuerBankDAOImpl implements IssuerBankDAO {

    private final ocean.acs.models.sql_server.repository.IssuerBankRepository repo;

    @Override
    public Optional<IssuerBankDO> findById(Long id) {
        return repo.findById(id).map(IssuerBankDO::valueOf);
    }

    @Override
    public List<IssuerBankDO> findByIds(List<Long> idList) throws DatabaseException {
        try {
            return repo.findByIdInAndDeleteFlagFalse(idList).stream().map(IssuerBankDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByIds] unknown exception, idList={}", idList, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<IssuerBankDO> findByCode(String bankCode) throws DatabaseException {
        try {
            return repo.findByCode(bankCode.trim()).map(IssuerBankDO::valueOf);
        } catch (Exception e) {
            log.error("[findByCode] unknown exception, bankCode={}", StringUtils.normalizeSpace(bankCode), e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }
    //00003 allow control asc/3ds oper id by user
    @Override
    public Optional<IssuerBankDO> findByAcsOperatorId(String acsOperatorId) throws DatabaseException {
        try
        {
            return repo.findByAcsOperatorId(acsOperatorId.trim()).map(IssuerBankDO::valueOf);
        } catch (Exception e) {
            log.error("[findByAcsOperatorId] unknown exception, acsOperatorId={}", StringUtils.normalizeSpace(acsOperatorId), e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean existsByCodeAndDeleteFlagIsFalse(String bankCode) throws DatabaseException {
        try {
            return repo.existsByCodeAndDeleteFlagIsFalse(bankCode.trim());
        } catch (Exception e) {
            log.error("[existsByCodeAndDeleteFlagIsFalse] unknown exception, bankCode={}",
                StringUtils.normalizeSpace(bankCode),
                    e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Page<IssuerBankDO> findAll(PageQueryDO pageQueryDO) throws DatabaseException {
        Pageable pageable = PageRequest.of(pageQueryDO.getPage() - 1, pageQueryDO.getPageSize(),
                Sort.Direction.DESC, "createMillis");
        try {
            return repo.findAll(pageable).map(IssuerBankDO::valueOf);
        } catch (Exception e) {
            log.error("[findAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public List<IssuerBankDO> findAll() throws DatabaseException {
        try {
            List<IssuerBank> issuerBankList = repo.findAll();
            return issuerBankList.stream().map(IssuerBankDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public List<Long> findIdAll() throws DatabaseException {
        try {
            return repo.findIdAll();
        } catch (Exception e) {
            log.error("[findIdAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<IssuerBankDO> add(PortalIssuerBankDO issuerBankDO) {
        if (issuerBankDO == null) {
            log.error("[add] Failed in add issuer bank due to missing issuer bank content");
            throw new IllegalArgumentException(
                    "Failed in add issuer bank due to missing issuer bank content.");
        }

        IssuerBank issuerBank = repo.save(IssuerBank.valueOf(issuerBankDO));
        return Optional.ofNullable(issuerBank).map(IssuerBankDO::valueOf);
    }

    @Override
    public Optional<IssuerBankDO> update(PortalIssuerBankDO issuerBankDO) {
        if (issuerBankDO == null) {
            log.error("[update] Failed in update issuer bank due to missing issuer bank content");
            throw new IllegalArgumentException(
                    "Failed in update issuer bank due to missing issuer bank content.");
        }

        IssuerBank issuerBank = repo.findById(issuerBankDO.getId()).map(e -> {
            // e.setCode(issuerBankDto.getCode()); // 銀行代碼不可修改，若要修改只能刪除後再重建，避免資料關聯出錯
            e.setName(issuerBankDO.getName());
            e.setAuditStatus(issuerBankDO.getAuditStatus().getSymbol());
            e.setUpdater(issuerBankDO.getUser());
            e.setUpdateMillis(System.currentTimeMillis());
            return e;
        }).map(repo::save).orElseThrow(() -> {
            log.warn(
                    "[update] Failed in update issuer bank due to unknown issuer bank content with id={}",
                    issuerBankDO.getId());
            return new OceanExceptionForPortal("Command failed in missing target content.");
        });

        return Optional.of(IssuerBankDO.valueOf(issuerBank));
    }

    @Override
    public Optional<IssuerBankDO> delete(DeleteDataDO deleteDataDO) {
        return repo.findById(deleteDataDO.getId()).map(e -> {
            e.setAuditStatus(deleteDataDO.getAuditStatus().getSymbol());
            e.setDeleteFlag(true);
            e.setDeleter(deleteDataDO.getUser());
            e.setDeleteMillis(System.currentTimeMillis());
            return e;
        }).map(repo::save).map(IssuerBankDO::valueOf);
    }

    @Override
    public Long getAcsOperatorIdNextVal() {
        return null;
    }

}
