package com.iflytek.vivian.traffic.server.utils;

import java.util.UUID;

/**
 * @ClassName UUIDUtil
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 11:20
 * @Version 1.0
 **/
public class UUIDUtil {
    /**
     * 创建uuid
     * @return
     */
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
