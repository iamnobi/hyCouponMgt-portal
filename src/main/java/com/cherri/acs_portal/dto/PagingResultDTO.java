package com.cherri.acs_portal.dto;

import com.cherri.acs_portal.dto.acs_integrator.ApiPageResponse;
import com.cherri.acs_portal.util.PageQuerySqlUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagingResultDTO<T> {
  private static final PagingResultDTO INSTANCE = new PagingResultDTO<>(Collections.emptyList());
  private Long total = 0L;
  private Integer totalPages = 0;
  private Integer currentPage = 1;

  @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
  private List<T> data;

  public PagingResultDTO(List<T> data) {
    this.data = data;
  }

  public PagingResultDTO(PagingResultDTO oldPaging, List<T> data) {
    this.data = data;
    this.total = oldPaging.getTotal();
    this.totalPages = oldPaging.getTotalPages();
    this.currentPage = oldPaging.getCurrentPage();
  }

  public PagingResultDTO(Long total, PageQueryDTO pageQueryDto, List<T> data) {
    this.total = total;
    this.totalPages = PageQuerySqlUtils.getTotalPage(total, pageQueryDto.getPageSize());
    this.currentPage = pageQueryDto.getPage();
    this.data = data;
  }

  @SuppressWarnings("unchecked")
  public static <T> PagingResultDTO<T> valueOf(Page page) {
    int currentPage = page.getNumber() + 1;
    return new PagingResultDTO<>(
        page.getTotalElements(), page.getTotalPages(), currentPage, page.getContent());
  }

  @SuppressWarnings("unchecked")
  public static <T> PagingResultDTO<T> valueOf(ApiPageResponse apiPageResponse) {
    return new PagingResultDTO(
        apiPageResponse.getTotal(),
        apiPageResponse.getTotalPages(),
        apiPageResponse.getCurrentPage(),
        apiPageResponse.getData());
  }

  public static PagingResultDTO empty() {
    return PagingResultDTO.INSTANCE;
  }

  @JsonIgnore
  public boolean isEmpty() {
    return this.data == null || total == 0;
  }
}
