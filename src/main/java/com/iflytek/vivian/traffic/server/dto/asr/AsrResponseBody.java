package com.iflytek.vivian.traffic.server.dto.asr;

import lombok.Data;

import java.util.List;

/**
 * @ClassName AsrResponseBody
 * @Author xinwang41
 * @Date 2021/3/2 9:30
 **/

@Data
public class AsrResponseBody {
    String audio;
    List<AsrResponseBodyResult> result;
}
