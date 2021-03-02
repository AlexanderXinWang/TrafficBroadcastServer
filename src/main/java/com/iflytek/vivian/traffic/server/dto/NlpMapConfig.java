package com.iflytek.vivian.traffic.server.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NlpMapConfig
 * @Author xinwang41
 * @Date 2021/3/2 9:38
 **/

@Component
@ConfigurationProperties(prefix = "nlp")
public class NlpMapConfig {
    private Map<String, String> mapProps = new HashMap<>(); //接收prop1里面的属性值

    public Map<String, String> getMapProps() {
        return mapProps;
    }

    public void setMapProps(Map<String, String> mapProps) {
        this.mapProps = mapProps;
    }
}
