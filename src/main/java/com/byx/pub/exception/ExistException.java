package com.byx.pub.exception;

import com.byx.pub.enums.IErrorCode;

/**
 * 自定义API异常
 * Created by zyjk on 2020/2/27.
 */
public class ExistException extends RuntimeException {
    private IErrorCode errorCode;

    public ExistException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ExistException(String message) {
        super(message);
    }

    public ExistException(Throwable cause) {
        super(cause);
    }

    public ExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
