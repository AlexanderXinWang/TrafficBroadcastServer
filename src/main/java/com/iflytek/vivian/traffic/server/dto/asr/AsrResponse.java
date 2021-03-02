package com.iflytek.vivian.traffic.server.dto.asr;

import lombok.Data;

/**
 * @ClassName AsrResponse
 * @Author xinwang41
 * @Date 2021/3/2 9:29
 **/

@Data
public class AsrResponse {
    AsrResponseState state;
    AsrResponseBody body;
}
