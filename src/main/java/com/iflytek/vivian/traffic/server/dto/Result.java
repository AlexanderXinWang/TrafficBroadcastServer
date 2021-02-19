package com.iflytek.vivian.traffic.server.dto;

import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.exceptions.BaseException;
import io.netty.util.internal.ThrowableUtil;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @ClassName Result
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/2/9 8:38
 * @Version 1.0
 **/
public class Result<T> {

    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 错误代码，非0为错误
     */
    private int errorCode = ErrorCode.SUCCESS.getCode();
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 包装的数据对象
     * 使用模板类包装比Object更好
     */
    private T data;

    public boolean isSuccess() {return success;}

    public void setSuccess(boolean success) {this.success = success;}

    public String getErrorMessage() {return errorMessage;}

    public void setErrorMessage(String errorMessage) {this.errorMessage = errorMessage;}

    public int getErrorCode() {return errorCode;}

    public void setErrorCode(int errorCode) {this.errorCode = errorCode;}

    public T getData() {return data;}

    public void setData(T data) {this.data = data;}

    /**
     * 根据错误枚举生成返回结果
     * @param code
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(ErrorCode code) {return fail(code.getCode(), code.getMsg());}

    public static <T> Result<T> fail(ErrorCode code, String msg) {return fail(code.getCode(), msg);}

    /**
     * 根据BaseException生成输出结果
     * @param e
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(BaseException e) {return fail(e.getErrorCode(), e.getMessage());}

    /**
     * 根据错误代码代码值，和错误提示信息生成返沪结果
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.errorCode = code;
        result.success = false;
        result.errorMessage = message; //SecLib.xss.handle(message,EncType.HTML);
        return result;
    }

    /**
     * 只有错误返回结果，错误代码统一为Fail
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(String message) {return fail(ErrorCode.FAIL.getCode(), message);}

    public static <T> Result<T> success(T value) {
        Result<T> result = new Result<>();
        result.setData(value);
        result.success = true;
        return result;
    }
}
