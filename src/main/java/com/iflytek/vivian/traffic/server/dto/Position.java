package com.iflytek.vivian.traffic.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Position() {
    }

    public Position(int startPos, String text) {
        this.startPos = startPos;
        this.text = text;
    }

    public Position(String text, int endPos) {
        this.text = text;
        this.endPos = endPos;
    }

    public Position(int startPos, int endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }
}
