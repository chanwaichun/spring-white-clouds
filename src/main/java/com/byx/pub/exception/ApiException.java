package com.byx.pub.exception;


import com.byx.pub.enums.IErrorCode;

/**
 * 自定义API异常
 * Created by zyjk on 2020/2/27.
 */
public class ApiException extends RuntimeException {
    private IErrorCode errorCode;

    private Long  code;

    private String  message;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(Long code,String message) {
        this.code = code;
        this.message = message;
    }

    public ApiException(String message) {
        this.code = 500L;
        this.message = message;
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
    public String getMessage() {
        return message;
    }
    public Long getCode() {
        return code;
    }
}
