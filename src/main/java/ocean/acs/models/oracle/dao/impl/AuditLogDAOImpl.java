package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.AuditLogDAO;
import ocean.acs.models.data_object.entity.AuditLogDO;
import ocean.acs.models.oracle.entity.AuditLog;

/**
 * AuditLogDAOImpl
 *
 * @author Alan Chen
 */
@Slf4j
@Repository
@AllArgsConstructor
public class AuditLogDAOImpl implements AuditLogDAO {

    private final ocean.acs.models.oracle.repository.AuditLogRepository auditLogRepository;

    @Override
    public AuditLogDO save(AuditLogDO auditLogDO) throws DatabaseException {
        try {
            return AuditLogDO.valueOf(auditLogRepository.save(AuditLog.valueOf(auditLogDO)));
        } catch (Exception e) {
            log.error("[save] system error, meLogs={}", auditLogDO, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Page<AuditLogDO> findAll(Specification spec, Pageable pageable)
      throws DatabaseException {
        try {
            Page<AuditLog> page = auditLogRepository.findAll(spec, pageable);
            return new PageImpl<>(page.getContent().stream()
              .map(AuditLogDO::valueOf).collect(Collectors.toList()),
              pageable, page.getTotalElements());
        } catch (Exception e) {
            log.error("[findAll] system error", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<AuditLogDO> findAll(Specification spec, Sort sort) throws DatabaseException {
        try {
            List<AuditLog> list = auditLogRepository.findAll(spec, sort);
            return list.stream().map(AuditLogDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAll] system error", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<AuditLogDO> findByIds(List<Long> ids) throws DatabaseException {
        try {
            List<AuditLog> list = auditLogRepository.findByIds(ids);
            return list.stream().map(AuditLogDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByIds] system error, ids={}", ids, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }
}
