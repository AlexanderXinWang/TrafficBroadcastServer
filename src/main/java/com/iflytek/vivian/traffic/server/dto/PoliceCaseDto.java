package com.iflytek.vivian.traffic.server.dto;

import lombok.Data;

/**
 * @ClassName PoliceCaseDto
 * @Author xinwang41
 * @Date 2021/3/2 9:39
 **/

@Data
public class PoliceCaseDto {
    /**
     * 发生地
     */
    private String location;

    /**
     * 车辆类型
     */
    private String vehicle;

    /**
     * 事故是
     */
    private String event;

    /**
     * 事故结果是
     */
    private String eventResult;
}
