package com.iflytek.vivian.traffic.server.dto.iat;

import com.google.gson.JsonObject;

import java.util.Arrays;

/**
 * @ClassName IatText
 * @Author xinwang41
 * @Date 2021/3/29 11:34
 **/
public class IatText {
    int sn;
    int bg;
    int ed;
    String text;
    String pgs;
    int[] rg;
    boolean deleted;
    boolean ls;
    JsonObject vad;
    @Override
    public String toString() {
        return "Text{" +
                "bg=" + bg +
                ", ed=" + ed +
                ", ls=" + ls +
                ", sn=" + sn +
                ", text='" + text + '\'' +
                ", pgs=" + pgs +
                ", rg=" + Arrays.toString(rg) +
                ", deleted=" + deleted +
                ", vad=" + (vad==null ? "null" : vad.getAsJsonArray("ws").toString()) +
                '}';
    }
}
