package com.iflytek.vivian.traffic.server.dto;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName TtsActionParam
 * @Author xinwang41
 * @Date 2021/2/19 14:59
 **/

@Data
public class TtsActionParam {
    private String trackId;

    private String text;

    private Map<Integer, Integer> params;
}
