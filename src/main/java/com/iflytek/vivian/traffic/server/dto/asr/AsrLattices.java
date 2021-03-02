package com.iflytek.vivian.traffic.server.dto.asr;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

/**
 * @ClassName AsrLattices
 * @Author xinwang41
 * @Date 2021/3/2 9:26
 * @Description：片段内容
 */

@Data
public class AsrLattices {
    /**
     * 片段 ID
     */
    private int lid;
    /**
     * 说话人序号
     */
    private int spk;
    /**
     * 句子起始位置，单位为帧
     */
    private int begin;
    /**
     * 	句子结束位置，单位为帧
     */
    private int end;
    /**
     * 句子置信度
     */
    private double sc;
    /**
     * 一句话的连续识别 cn json
     */
    private JSONArray json_cn;
    /**
     * 一句话的最佳内容 onebest json
     */
    private JSONArray json_1best;

    /**
     * 一句话的最佳内容
     */
    private String onebest;
}
