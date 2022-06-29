package com.cherri.acs_portal.filter;

import com.cherri.acs_portal.dto.ApiResponse;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.ForceLogoutException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class APIExceptionAdvice extends ResponseEntityExceptionHandler {

    private final HttpSession httpSession;

    public APIExceptionAdvice(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * 錯誤處理情境：Ex:<br> Controller使用 @Valid @RequestBody對DTO物件內的參數驗證
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        String errorMessages =
          errors.stream()
            .map(this::getErrorMessage)
            .collect(Collectors.joining("; "));

        log.error(
          "[handleMethodArgumentNotValid] parameter validation error. error message={}",
          errorMessages);

        return new ResponseEntity<>(
          new ApiResponse(ResultStatus.ILLEGAL_ARGUMENT, errorMessages), HttpStatus.OK);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            String objName = error.getObjectName();
            String fieldName = ((FieldError) error).getField();
            return String.format("[%s.%s] %s", objName, fieldName, error.getDefaultMessage());
        }
        return error.getDefaultMessage();
    }

    /**
     * 錯誤處理情境：Ex:<br> Controller使用如@NotBlank校驗參數的錯誤訊息
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiResponse> constraintViolationExceptionHandler(
      ConstraintViolationException e) {
        log.error(
          "[constraintViolationExceptionHandler] parameter validation error. error message={}",
          e.getMessage());

        return new ResponseEntity<>(
          new ApiResponse(ResultStatus.ILLEGAL_ARGUMENT, e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler({NoSuchDataException.class})
    public ResponseEntity<ApiResponse> noSuchDataExceptionHandler(NoSuchDataException re) {
        ResultStatus errStatus = re.getResultStatus();
        String errorMessage = StringUtils.defaultString(re.getMessage(), errStatus.name());
        log.warn("[noSuchDataExceptionHandler] errorMessage={}", errorMessage);
        return new ResponseEntity<>(new ApiResponse(errStatus, errorMessage), HttpStatus.OK);
    }

    @ExceptionHandler({OceanException.class})
    public ResponseEntity<ApiResponse> oceanExceptionHandler(OceanException re) {
        log.error("[oceanExceptionHandler] message={}", re.getMessage(), re);

        ResultStatus errStatus = re.getResultStatus();
        errStatus = errStatus == null ? ResultStatus.SERVER_ERROR : errStatus;
        String errorMessage = StringUtils.defaultString(re.getMessage(), errStatus.name());

        // TAPPF-1828 do not show SQL error messages
        if (errStatus == ResultStatus.DB_SAVE_ERROR ||
            errStatus == ResultStatus.DB_READ_ERROR) {
            errorMessage = errStatus.name();
        }

        return new ResponseEntity<>(
          new ApiResponse(errStatus, errorMessage), HttpStatus.OK);
    }

    @ExceptionHandler({ForceLogoutException.class})
    public ResponseEntity<ApiResponse> forceLogoutHandler(ForceLogoutException e) {
        httpSession.invalidate();
        ResultStatus errStatus = e.getResultStatus();
        String errorMessage = StringUtils.defaultString(e.getMessage(), errStatus.name());
        return new ResponseEntity<>(
            new ApiResponse(errStatus, errorMessage), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public void accessDeniedHandler(AccessDeniedException re) {
        String msg = "Access is denied";
        log.warn("[accessDeniedHandler] {}", msg);
        throw new AccessDeniedException(msg);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResponse> exceptionHandler(Exception e) {
        log.error("[exceptionHandler] unknown exception", e);

        return new ResponseEntity<>(
          new ApiResponse(ResultStatus.SERVER_ERROR, "An unknown exception happened."), HttpStatus.OK);
    }

}
