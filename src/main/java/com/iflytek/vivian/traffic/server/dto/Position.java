package com.iflytek.vivian.traffic.server.dto;

import lombok.Data;

/**
 * @ClassName Position
 * @Author xinwang41
 * @Date 2021/3/2 9:39
 **/

@Data
public class Position {
    private int sentenceStartPos;
    private int startPos;
    private String text;
    private int endPos;
}
