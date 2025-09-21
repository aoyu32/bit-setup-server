package com.aoyu.bitsetup.common.exception;

import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.result.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName：GlobalExceptionHandle
 * @Author: aoyu
 * @Date: 2025-09-20 13:21
 * @Description: 全局异常处理类
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    /**
     * 处理其他所有未预期的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: {}", e);
        return Result.error(ResultCode.ERROR);
    }

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: code={}, message={}", e.getCode(), e.getMsg());
        return Result.error(e.getCode(), e.getMsg());
    }


    @ExceptionHandler(MailException.class)
    public Result<?> handleMailException(MailException e) {
        log.info("邮箱服务异常：msg:{},cause:{}", e.getMessage(), e.getCause());
        if (e.getMessage().contains("550")) {
            return Result.error(600, "邮箱不存在或不可达");
        }
        return Result.error(ResultCode.ERROR);
    }

    /**
     * 处理请求体参数校验异常（@RequestBody + @Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("参数校验异常: {}", errorMessage);
        return Result.error(400, errorMessage);
    }

    /**
     * 处理路径参数和请求参数校验异常（@PathVariable、@RequestParam + @Valid）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        String errorMessage = violations.stream()
                .map(violation -> {
                    String propertyPath = violation.getPropertyPath().toString();
                    // 提取参数名（去掉方法名前缀）
                    String fieldName = propertyPath.contains(".") ?
                            propertyPath.substring(propertyPath.lastIndexOf('.') + 1) :
                            propertyPath;
                    return fieldName + ": " + violation.getMessage();
                })
                .collect(Collectors.joining(", "));

        log.warn("参数校验异常: {}", errorMessage);
        return Result.error(400, errorMessage);
    }

    /**
     * 处理参数类型转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errorMessage = String.format("参数 '%s' 类型错误，期望类型: %s",
                e.getName(),
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");

        log.warn("参数类型转换异常: {}", errorMessage);
        return Result.error(400, errorMessage);
    }

    /**
     * 处理Spring 6.x新的方法级别参数校验异常（@PathVariable、@RequestParam + 校验注解）
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Result<Void> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        StringBuilder errorMessage = new StringBuilder();

        e.getValueResults().forEach(result -> {
            String parameterName = result.getMethodParameter().getParameterName();
            result.getResolvableErrors().forEach(error -> {
                if (!errorMessage.isEmpty()) {
                    errorMessage.append(", ");
                }
                errorMessage.append(parameterName).append(": ").append(error.getDefaultMessage());
            });
        });

        log.warn("方法参数校验异常: {}", errorMessage.toString());
        return Result.error(400, errorMessage.toString());
    }
}
