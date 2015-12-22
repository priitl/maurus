package com.priitlaht.ppwebtv.frontend.common.errors;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

  @ResponseBody
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(ConcurrencyFailureException.class)
  public ErrorDTO processConcurrencyError(ConcurrencyFailureException ex) {
    return new ErrorDTO(ErrorConstants.ERR_CONCURRENCY_FAILURE);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorDTO processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    return processFieldErrors(fieldErrors);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(CustomParameterizedException.class)
  public ParameterizedErrorDTO processParameterizedValidationError(CustomParameterizedException ex) {
    return ex.getErrorDTO();
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  public ErrorDTO processAccessDeniedExcepetion(AccessDeniedException e) {
    return new ErrorDTO(ErrorConstants.ERR_ACCESS_DENIED, e.getMessage());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ErrorDTO processMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
    return new ErrorDTO(ErrorConstants.ERR_METHOD_NOT_SUPPORTED, exception.getMessage());
  }

  private ErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
    ErrorDTO dto = new ErrorDTO(ErrorConstants.ERR_VALIDATION);
    for (FieldError fieldError : fieldErrors) {
      dto.add(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode());
    }
    return dto;
  }
}
