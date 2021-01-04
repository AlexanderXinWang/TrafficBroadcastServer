package com.iflytek.vivian.traffic.server.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName Constants
 * @Description 事件常量
 * @Author xinwang41
 * @Date 2021/1/4 10:42
 * @Version 1.0
 **/
public class Constants {
    public static final String FACE_DB_NAME="tts-demo";

    public enum EventState{

        EventOk("0","已处理"),
        EventReport("1","以上报"),
        EventIsPlay("0","未播报"),
        Unknown("","错误状态");

        private String value,desc;
        EventState(String value,String desc){
            this.value=value;
            this.desc=desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static EventState ofValue(String value){
            if (StringUtils.isBlank(value)){
                return Unknown;
            }
            for(EventState state:values()){
                if (state.getValue().equals(value)){
                    return state;
                }
            }
            return Unknown;
        }
    }
}
