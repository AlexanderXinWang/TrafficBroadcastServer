package com.iflytek.vivian.traffic.server.utils;

import com.iflytek.vivian.traffic.server.client.TtsAbilityClient;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.lang.model.element.ElementVisitor;
import java.text.DateFormat;

/**
 * 语音合成工具
 */
@Component
public class TtsUtil {
    @Autowired
    TtsAbilityClient ttsAbilityClient;

    public String generatePcmUrl(Event event) {
        String text = event.getStartTime() + event.getLocation() + "发生事件" + event.getEvent()
                + "车辆类型为" + event.getVehicle() + "事件结果是" + event.getEventResult() ;
        String fileName = event.getId();

        ttsAbilityClient.tts(text, fileName);

        String pcmUrl = "http://1.15.78.72:8080/pcm/" + event.getId() + ".pcm";
        return pcmUrl;
    }
}
