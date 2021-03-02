package com.iflytek.vivian.traffic.server.dto.asr;

import lombok.Data;

/**
 * @ClassName AsrResponseBodyResult
 * @Author xinwang41
 * @Date 2021/3/2 9:31
 **/

@Data
public class AsrResponseBodyResult {
    long begin;
    long end;
    String text;
}
