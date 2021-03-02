package com.iflytek.vivian.traffic.server.dto.tts;

import lombok.Data;

/**
 * @ClassName TtsResponseState
 * @Author xinwang41
 * @Date 2021/2/19 15:01
 **/

@Data
public class TtsResponseState {

    String resultCode;

    boolean success;

    int ok;
}
