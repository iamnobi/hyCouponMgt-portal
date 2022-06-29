package ocean.acs.models.data_object.portal;

import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.utils.PageQuerySqlUtils;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagingResultDO<T> {

    private static final PagingResultDO INSTANCE = new PagingResultDO<>(Collections.emptyList());
    private Long total = 0L;
    private Integer totalPages = 0;
    private Integer currentPage = 1;

    @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
    private List<T> data;

    public PagingResultDO(List<T> data) {
        this.data = data;
    }

    public PagingResultDO(PagingResultDO oldPaging, List<T> data) {
        this.data = data;
        this.total = oldPaging.getTotal();
        this.totalPages = oldPaging.getTotalPages();
        this.currentPage = oldPaging.getCurrentPage();
    }

    public PagingResultDO(Long total, PageQueryDO pageQueryDO, List<T> data) {
        this.total = total;
        this.totalPages = PageQuerySqlUtils.getTotalPage(total, pageQueryDO.getPageSize());
        this.currentPage = pageQueryDO.getPage();
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public static <T> PagingResultDO<T> valueOf(Page page) {
        int currentPage = page.getNumber() + 1;
        return new PagingResultDO<>(page.getTotalElements(), page.getTotalPages(), currentPage,
                page.getContent());
    }

    // @SuppressWarnings("unchecked")
    // public static <T> PagingResultDTO<T> valueOf(ApiPageResponse apiPageResponse) { //TODO
    // return new PagingResultDTO(apiPageResponse.getTotal(), apiPageResponse.getTotalPages(),
    // apiPageResponse.getCurrentPage(), apiPageResponse.getData());
    // }

    public static PagingResultDO empty() {
        return PagingResultDO.INSTANCE;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.data == null || total == 0;
    }

}
