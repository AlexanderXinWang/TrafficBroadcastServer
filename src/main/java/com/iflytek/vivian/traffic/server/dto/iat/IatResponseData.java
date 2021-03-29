package com.iflytek.vivian.traffic.server.dto.iat;

import lombok.Data;

/**
 * @ClassName IatResponseData
 * @Author xinwang41
 * @Date 2021/3/29 11:31
 **/
public class IatResponseData {
    private int code;
    private String message;
    private String sid;
    private IatData data;
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return this.message;
    }
    public String getSid() {
        return sid;
    }
    public IatData getData() {
        return data;
    }
}
