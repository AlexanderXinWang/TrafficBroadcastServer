package com.iflytek.vivian.traffic.server.dto.iat;

import com.google.gson.JsonObject;

/**
 * @ClassName IatResult
 * @Description 听写识别结果
 * @Author xinwang41
 * @Date 2021/3/29 11:33
 **/
public class IatResult {
    int bg;
    int ed;
    String pgs;
    int[] rg;
    /**
     * 返回结果的序号
     */
    int sn;
    /**
     * 听写结果
     */
    IatWs[] ws;
    /**
     * 是否是最后一片结果
     */
    boolean ls;
    JsonObject vad;

    public IatText getText() {
        IatText text = new IatText();
        StringBuilder sb = new StringBuilder();
        for (IatWs ws : this.ws) {
            sb.append(ws.cw[0].w);
        }
        text.sn = this.sn;
        text.text = sb.toString();
        text.sn = this.sn;
        text.rg = this.rg;
        text.pgs = this.pgs;
        text.bg = this.bg;
        text.ed = this.ed;
        text.ls = this.ls;
        text.vad = this.vad==null ? null : this.vad;
        return text;
    }
}
