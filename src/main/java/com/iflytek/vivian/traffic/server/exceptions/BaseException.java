package com.iflytek.vivian.traffic.server.exceptions;

import com.iflytek.vivian.traffic.server.constants.ErrorCode;

/**
 * @ClassName BaseException
 * @Description 基础错误类型定义
 * @Author xinwang41
 * @Date 2021/1/4 10:50
 **/
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 8944431786441920015L;
    protected int errorCode;
    protected String message;
    protected Throwable e;

    public BaseException(int errorCode, String message){
        super(message);
        this.errorCode=errorCode;
        this.message=message;
    }

    public BaseException(int errorCode, String message, Throwable e){
        super(message,e);
        this.errorCode=errorCode;
        this.message=message;
        this.e=e;
    }

    public BaseException(ErrorCode errorCode){
        super(errorCode.getMsg());
        this.errorCode=errorCode.getCode();
        this.message=errorCode.getMsg();
    }

    public BaseException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode.getCode();
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }
}
