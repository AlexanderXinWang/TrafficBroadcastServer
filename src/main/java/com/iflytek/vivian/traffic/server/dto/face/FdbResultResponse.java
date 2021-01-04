package com.iflytek.vivian.traffic.server.dto.face;

import lombok.Data;

/**
 * @ClassName FdbResultResponse
 * @Author xinwang41
 * @Version 1.0
 **/

@Data
public class FdbResultResponse<T> {
    private String result;
    private String errorMessage;
    private T success;
    private T fail;
    private T data;
}
