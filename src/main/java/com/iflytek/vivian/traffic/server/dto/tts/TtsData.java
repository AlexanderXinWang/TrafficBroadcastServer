package com.iflytek.vivian.traffic.server.dto.tts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName TtsData
 * @Author xinwang41
 * @Date 2021/4/1 16:27
 **/

public class TtsData {
    public int status;  //标志音频是否返回结束  status=1，表示后续还有音频返回，status=2表示所有的音频已经返回
    public String audio;  //返回的音频，base64 编码
    private String ced;
}
