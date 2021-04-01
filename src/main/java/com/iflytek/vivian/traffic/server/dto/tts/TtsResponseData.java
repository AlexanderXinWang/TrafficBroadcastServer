package com.iflytek.vivian.traffic.server.dto.tts;

/**
 * @ClassName TtsResponseData
 * @Author xinwang41
 * @Date 2021/4/1 16:27
 **/
public class TtsResponseData {
    private int code;
    private String message;
    private String sid;
    private TtsData data;
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return this.message;
    }
    public String getSid() {
        return sid;
    }
    public TtsData getData() {
        return data;
    }
}
