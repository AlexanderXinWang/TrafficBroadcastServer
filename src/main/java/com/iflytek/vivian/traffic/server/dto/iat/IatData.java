package com.iflytek.vivian.traffic.server.dto.iat;

import com.iflytek.vivian.traffic.server.dto.Result;

/**
 * @ClassName IatData
 * @Author xinwang41
 * @Date 2021/3/29 11:33
 **/
public class IatData {
    private int status;
    private IatResult result;
    public int getStatus() {
        return status;
    }
    public IatResult getResult() {
        return result;
    }
}
