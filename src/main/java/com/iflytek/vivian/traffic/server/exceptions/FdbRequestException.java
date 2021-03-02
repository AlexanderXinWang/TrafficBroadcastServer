package com.iflytek.vivian.traffic.server.exceptions;

import com.iflytek.vivian.traffic.server.constants.ErrorCode;

/**
 * @ClassName FdbReuestException
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 10:51
 * @Version 1.0
 **/
public class FdbRequestException extends BaseException {
    public FdbRequestException(String msg){
        super(ErrorCode.FAIL.getCode(),msg);
    }
}
