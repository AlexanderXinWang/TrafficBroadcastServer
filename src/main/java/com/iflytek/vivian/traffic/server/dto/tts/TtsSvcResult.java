package com.iflytek.vivian.traffic.server.dto.tts;

import lombok.Data;

/**
 * @ClassName TtsSvcResult
 * @Author xinwang41
 * @Date 2021/2/19 15:02
 **/

@Data
public class TtsSvcResult {
    private String trackId;

    private String audioPath;

    private long procTime;

    private int txtLen;
}
