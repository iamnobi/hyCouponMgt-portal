package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.AuditLogDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * AuditLogDAO
 *
 * @author Alan Chen
 */
public interface AuditLogDAO {

    AuditLogDO save(AuditLogDO auditLogDO) throws DatabaseException;

    Page<AuditLogDO> findAll(Specification<AuditLogDO> spec, Pageable pageable)
      throws DatabaseException;
    List<AuditLogDO> findAll(Specification<AuditLogDO> spec, Sort sort) throws DatabaseException;

    List<AuditLogDO> findByIds(List<Long> ids) throws DatabaseException;

}
