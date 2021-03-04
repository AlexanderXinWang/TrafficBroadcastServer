package com.iflytek.vivian.traffic.server.utils;

import com.iflytek.vivian.traffic.server.dto.face.FdbResultResponse;

import java.util.List;

/**
 * @ClassName FdbResponseUtil
 * @Author xinwang41
 * @Date 2021/1/4 11:00
 **/
public class FdbResponseUtil {
    public static boolean isSuccess(FdbResultResponse rst){
        return  (rst!=null&&"success".equals(rst.getResult()));
    }

    public static <T> T getDataFromDataList(FdbResultResponse<List<T>> rst, Class<T> tClass, int index){
        if (rst!=null&&rst.getData()!=null){
            List<T> list=rst.getData();
            if (list.size()>index){
                return list.get(index);
            }
        }
        return null;
    }
}
