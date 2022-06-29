package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.utils.PageQuerySqlUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Builder.Default
    @NotNull(message = "{column.notempty}")
    private Integer page = 1;

    @Builder.Default
    private Integer pageSize = 20;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
