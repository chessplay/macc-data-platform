package com.ruijie.cloud.dc.core.exception.handler;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/04/07 13:55
 **/




import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.ruijie.cloud.dc.core.response.BaseResultCode;
import com.ruijie.cloud.dc.core.exception.base.BaseException;
import com.ruijie.cloud.dc.core.response.BaseResult;
import com.ruijie.cloud.dc.core.response.ResultCode;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return BaseResult.build(BaseResultCode.METHOD_NOT_SUPPORTED, e.getMethod());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public BaseResult handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',数据库中已存在记录'{}'", requestURI, e.getMessage());
        return BaseResult.build(BaseResultCode.DUPLICATE_KEY);
    }

//    @ExceptionHandler(MyBatisSystemException.class)
//    public BaseResult handleCannotFindDataSourceException(MyBatisSystemException e, HttpServletRequest request) {
//        String requestURI = request.getRequestURI();
//        log.error("请求地址'{}', Mybatis系统异常", requestURI, e);
//        return BaseResult.build(BaseResultCode.MYBATIS_SYSTEM_ERROR);
//    }

    @ExceptionHandler(MissingPathVariableException.class)
    public BaseResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
        return BaseResult.build(BaseResultCode.MISSING_PATH_VARIABLE, e.getVariableName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public BaseResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
        return BaseResult.build(BaseResultCode.ARGUMENT_TYPE_MISMATCH, e.getName(), e.getRequiredType().getName(), e.getValue());
    }

    @ExceptionHandler(BaseException.class)
    public BaseResult handleBaseException(BaseException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);


        return BaseResult.build(e.getCode(),e.getDefaultMessage(),e.getArgs());
    }

    @ExceptionHandler(Exception.class)
    public BaseResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return BaseResult.build(BaseResultCode.SYSTEM_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public BaseResult handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        return BaseResult.build(BaseResultCode.VALIDATION_ERROR, e.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult constraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return BaseResult.build(BaseResultCode.VALIDATION_ERROR, e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return BaseResult.build(BaseResultCode.VALIDATION_ERROR, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResult handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("Error reading HTTP message", e);
        if (e.getCause() instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException cause = (UnrecognizedPropertyException) e.getCause();
            String unrecognizedFieldName = cause.getPropertyName();
            String message = "请求中包含未知属性: " + unrecognizedFieldName;
            return BaseResult.build(BaseResultCode.MESSAGE_NOT_READABLE, message);
        } else {
            return BaseResult.build(BaseResultCode.MESSAGE_NOT_READABLE);
        }
    }


}
