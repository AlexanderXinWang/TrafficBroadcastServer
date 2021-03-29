package com.iflytek.vivian.traffic.server.dto.iat;

import com.iflytek.vivian.traffic.server.dto.Result;

/**
 * @ClassName IatData
 * @Description 听写结果信息
 * @Author xinwang41
 * @Date 2021/3/29 11:33
 **/
public class IatData {
    /**
     * 识别结果是否结束标识：
     * 0：识别的第一块结果
     * 1：识别中间结果
     * 2：识别最后一块结果
     */
    private int status;
    /**
     * 听写识别结果
     */
    private IatResult result;

    public int getStatus() {
        return status;
    }
    public IatResult getResult() {
        return result;
    }
}
