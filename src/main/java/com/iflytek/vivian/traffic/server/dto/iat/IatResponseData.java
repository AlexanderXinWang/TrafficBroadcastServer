package com.iflytek.vivian.traffic.server.dto.iat;

import lombok.Data;

/**
 * @ClassName IatResponseData
 * @Description 返回参数
 * @Author xinwang41
 * @Date 2021/3/29 11:31
 **/
public class IatResponseData {
    /**
     * 返回码，0表示成功，其它表示异常
     */
    private int code;
    /**
     * 错误描述
     */
    private String message;
    /**
     * 本次会话的id，只在握手成功后第一帧请求时返回
     */
    private String sid;
    /**
     * 听写结果信息
     */
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
