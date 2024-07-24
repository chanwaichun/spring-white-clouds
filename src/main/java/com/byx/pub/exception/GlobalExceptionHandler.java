package com.byx.pub.exception;

import com.byx.pub.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * Created by zyjk on 2020/2/27.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResult handle(ApiException e) {
        log.error("", e);
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        if(null != e.getCode()) {
            return CommonResult.failed(e.getCode(),e.getMessage());
        }
        return CommonResult.failed(e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult handleValidException(MethodArgumentNotValidException e) {
        log.error("", e);
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getDefaultMessage();
            }
        }
        return CommonResult.failed(message);
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public CommonResult handleValidException(BindException e) {
        log.error("", e);
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getDefaultMessage();
            }
        }
        return CommonResult.failed(message);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResult handleMessageNotReadableException(Exception ex, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletWebRequest.getNativeRequest();

        log.error("序列化异常：url = {}", httpServletRequest.getRequestURI());
        log.error("序列化异常", ex);
        return CommonResult.failed("未知异常");
        //return CommonResult.validateFailed("请求参数序列化异常");
    }

    @ResponseBody
    @ExceptionHandler(value = NullPointerException.class)
    public CommonResult NullPointerException(NullPointerException ex, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletWebRequest.getNativeRequest();

        log.error("参数异常：url = {}", httpServletRequest.getRequestURI());
        log.error("空指针异常", ex);
        return CommonResult.failed("未知异常");
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult exception(Exception ex, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletWebRequest.getNativeRequest();

        log.error("异常：url = {}", httpServletRequest.getRequestURI());
        log.error("", ex);
        return CommonResult.failed("未知异常");
    }
}
