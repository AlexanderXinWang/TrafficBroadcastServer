package com.iflytek.vivian.traffic.server.constants;


/**
 * @ClassName ErrorCode
 * @Description 错误定义类
 * @Author xinwang41
 * @Date 2021/1/4 10:42
 * @Version 1.0
 **/
public enum ErrorCode {
    SUCCESS(0, "成功"),

    FAIL(1, "失败");


    private int code;
    private String msg;

    private ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
