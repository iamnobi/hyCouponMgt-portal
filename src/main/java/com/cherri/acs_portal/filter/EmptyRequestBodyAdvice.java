package com.cherri.acs_portal.filter;

import com.cherri.acs_portal.constant.MessageConstants;
import java.lang.reflect.Type;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

@ControllerAdvice
public class EmptyRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(
      MethodParameter methodParameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(
      Object body,
      HttpInputMessage inputMessage,
      MethodParameter parameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
        throw new OceanException(
          ResultStatus.COLUMN_NOT_EMPTY, "Request body " + MessageConstants.get(MessageConstants.COLUMN_NOT_EMPTY));
    }
}
