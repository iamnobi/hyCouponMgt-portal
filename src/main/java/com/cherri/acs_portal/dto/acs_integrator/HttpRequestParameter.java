package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

@Data
public class HttpRequestParameter {

  private String url;

  private String reqJson;

  private Class resDtoClz;

  private TypeReference resDtoTypeReference;

  public HttpRequestParameter(String url, String reqJson, Class resDtoClz) {
    this.url = url;
    this.reqJson = reqJson;
    this.resDtoClz = resDtoClz;
  }

  public HttpRequestParameter(String url, String reqJson, TypeReference resDtoTypeReference) {
    this.url = url;
    this.reqJson = reqJson;
    this.resDtoTypeReference = resDtoTypeReference;
  }
}
