package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class PageQueryDto {
  private String page;
  private String pageSize;

  PageQueryDto(Integer page, Integer pageSize) {
    if (null != page) {
      this.page = String.valueOf(page);
    }
    if (null != pageSize) {
      this.pageSize = String.valueOf(pageSize);
      ;
    }
  }
}
