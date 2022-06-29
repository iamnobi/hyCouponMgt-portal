package com.cherri.acs_portal.dto;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.util.PageQuerySqlUtils;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
public class PageQueryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "{column.notempty}")
  private Integer page = 1;

  private Integer pageSize = 20;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }

  public void setPage(Integer page) {
    this.page = (page == null || page < 1) ? 1 : page;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = (pageSize == null || pageSize < 1) ? 1 : pageSize;
  }

  public Pageable getPageable() {
    return PageRequest.of(page - 1, pageSize);
  }

  public long getStartRowNumber() {
    return PageQuerySqlUtils.getStartRowNumber(page, pageSize);
  }

  public long getLimit() {
    return PageQuerySqlUtils.getLimit(page, pageSize);
  }
}
