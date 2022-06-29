package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiPageResponse<T> extends BaseResDTO {
  private Long total;
  private Integer totalPages;
  private Integer currentPage;
  private List<T> data;
}
