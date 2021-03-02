package com.iflytek.vivian.traffic.server.dto.tts;

import lombok.Data;

/**
 * @ClassName TtsResponse
 * @Author xinwang41
 * @Date 2021/2/19 15:01
 **/

@Data
public class TtsResponse {
    TtsResponseState state;

    TtsSvcResult body;
}
