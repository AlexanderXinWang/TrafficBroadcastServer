package com.iflytek.vivian.traffic.server.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @ClassName EventDto
 * @Author xinwang41
 * @Date 2021/3/2 9:37
 **/

@Data
public class EventDto {
    private String id;
    private String policemanId;
    private String policemanName;
    private String location;
    private String vehicle;
    private String event;
    private String eventResult;
    private String status;
    private String isPlay;
    private String iatResult;
}
