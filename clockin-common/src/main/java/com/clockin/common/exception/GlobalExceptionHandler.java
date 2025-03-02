package com.clockin.common.exception;

import com.clockin.common.response.Result;
import com.clockin.common.response.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Set;
import java.util.UUID;

/**
 * 全局異常處理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理自定義基礎異常
     */
    @ExceptionHandler(BaseException.class)
    public Result<Object> handleBaseException(BaseException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("Base Exception: [{}] {}, RequestId: {}", e.getCode(), e.getMessage(), requestId, e);
        
        Result<Object> result = Result.error(e.getCode(), e.getMessage());
        result.setRequestId(requestId);
        if (e.getData() != null) {
            result.setData(e.getData());
        }
        return result;
    }

    /**
     * 處理業務異常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("Business Exception: [{}] {}, RequestId: {}", e.getCode(), e.getMessage(), requestId, e);
        
        Result<Object> result = Result.error(e.getCode(), e.getMessage());
        result.setRequestId(requestId);
        if (e.getData() != null) {
            result.setData(e.getData());
        }
        return result;
    }

    /**
     * 處理參數校驗異常 (MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            message.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        
        String errorMsg = message.toString();
        if (StringUtils.isNotEmpty(errorMsg)) {
            errorMsg = errorMsg.substring(0, errorMsg.length() - 2);
        }
        
        log.error("Parameter Validation Exception: {}, RequestId: {}", errorMsg, requestId, e);
        
        Result<Object> result = Result.error(ResultCode.VALIDATE_FAILED.getCode(), errorMsg);
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理參數綁定異常 (BindException)
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleBindException(BindException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            message.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        
        String errorMsg = message.toString();
        if (StringUtils.isNotEmpty(errorMsg)) {
            errorMsg = errorMsg.substring(0, errorMsg.length() - 2);
        }
        
        log.error("Parameter Binding Exception: {}, RequestId: {}", errorMsg, requestId, e);
        
        Result<Object> result = Result.error(ResultCode.VALIDATE_FAILED.getCode(), errorMsg);
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理參數驗證異常 (ConstraintViolationException)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
        }
        
        String errorMsg = message.toString();
        if (StringUtils.isNotEmpty(errorMsg)) {
            errorMsg = errorMsg.substring(0, errorMsg.length() - 2);
        }
        
        log.error("Constraint Violation Exception: {}, RequestId: {}", errorMsg, requestId, e);
        
        Result<Object> result = Result.error(ResultCode.VALIDATE_FAILED.getCode(), errorMsg);
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理請求參數類型不匹配異常
     */
    @ExceptionHandler({TypeMismatchException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleTypeMismatchException(Exception e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("Type Mismatch Exception, RequestId: {}", requestId, e);
        
        Result<Object> result = Result.error(ResultCode.PARAM_ERROR.getCode(), "請求參數類型不匹配");
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理缺少請求參數異常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("Missing Request Parameter Exception, RequestId: {}", requestId, e);
        
        Result<Object> result = Result.error(ResultCode.PARAM_ERROR.getCode(),
                "缺少必要的請求參數: " + e.getParameterName());
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理請求體不可讀異常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("Message Not Readable Exception, RequestId: {}", requestId, e);
        
        Result<Object> result = Result.error(ResultCode.PARAM_ERROR.getCode(), "請求體不可讀");
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理不支持的請求方法異常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("Request Method Not Supported Exception, RequestId: {}", requestId, e);
        
        Result<Object> result = Result.error(ResultCode.METHOD_NOT_ALLOWED.getCode(),
                "不支持的請求方法: " + e.getMethod());
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理不支持的媒體類型異常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Result<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("Media Type Not Supported Exception, RequestId: {}", requestId, e);
        
        Result<Object> result = Result.error(415, "不支持的媒體類型: " + e.getContentType());
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理資源不存在異常
     */
    @ExceptionHandler({ResourceNotFoundException.class, NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Object> handleResourceNotFoundException(Exception e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        String message = "請求的資源不存在";
        if (e instanceof ResourceNotFoundException) {
            message = e.getMessage();
        } else if (e instanceof NoHandlerFoundException) {
            NoHandlerFoundException ex = (NoHandlerFoundException) e;
            message = "找不到路徑 [" + ex.getRequestURL() + "] 的處理方法";
        }
        
        log.error("Resource Not Found Exception: {}, RequestId: {}", message, requestId, e);
        
        Result<Object> result = Result.error(ResultCode.NOT_FOUND.getCode(), message);
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理認證異常
     */
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Object> handleAuthException(AuthException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("Authentication Exception: {}, RequestId: {}", e.getMessage(), requestId, e);
        
        Result<Object> result = Result.error(e.getCode(), e.getMessage());
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理權限異常
     */
    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> handlePermissionException(PermissionException e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("Permission Exception: {}, RequestId: {}", e.getMessage(), requestId, e);
        
        Result<Object> result = Result.error(e.getCode(), e.getMessage());
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 處理所有未捕獲的異常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleException(Exception e, HttpServletRequest request) {
        String requestId = getRequestId(request);
        log.error("System Exception, RequestId: {}", requestId, e);
        
        Result<Object> result = Result.error(ResultCode.ERROR.getCode(), "系統繁忙，請稍後再試");
        result.setRequestId(requestId);
        return result;
    }

    /**
     * 獲取請求標識符
     *
     * @param request HTTP請求
     * @return 請求標識符
     */
    private String getRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-Id");
        if (StringUtils.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString().replace("-", "");
        }
        return requestId;
    }
}
