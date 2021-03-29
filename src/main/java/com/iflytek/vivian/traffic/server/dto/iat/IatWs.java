package com.iflytek.vivian.traffic.server.dto.iat;

/**
 * @ClassName IatWs
 * @Description 听写结果
 * @Author xinwang41
 * @Date 2021/3/29 11:35
 **/
public class IatWs {
    /**
     * 中文分词
     */
    IatCw[] cw;
    /**
     * 起始的端点帧偏移值，单位：帧（1帧=10ms）
     * 注：以下两种情况下bg=0，无参考意义：
     * 1)返回结果为标点符号或者为空；2)本次返回结果过长。
     */
    int bg;
    /**
     * 保留字段
     */
    int ed;
}
