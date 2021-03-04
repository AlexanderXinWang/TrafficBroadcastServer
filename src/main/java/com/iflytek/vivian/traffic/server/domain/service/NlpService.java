package com.iflytek.vivian.traffic.server.domain.service;

import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.dto.Position;
import com.iflytek.vivian.traffic.server.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName NlpService
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 10:45
 **/

@Service
@Slf4j
public class NlpService {
    @Autowired
    private StringUtil stringUtil;

    public Event getEventCaseSimple(String text) {
        long startTime = System.currentTimeMillis();

        // 去空格
        text = StringUtil.textPreprocess(text);
        Map<String, Position> map = stringUtil.getPoliceCaseElement(text);

        Event event = new Event();

        Position positionLocation = map.get("location");
        if (null != positionLocation) {
            event.setLocation(text.substring(positionLocation.getStartPos(), positionLocation.getEndPos()));
        }

        // 车辆类型
        Position positionVehicle = map.get("vehicle");
        if (null != positionVehicle) {
            event.setVehicle(text.substring(positionVehicle.getStartPos(), positionVehicle.getEndPos()));
        }

        // 事件
        Position positionEvent = map.get("event");
        if (null != positionEvent) {
            event.setEvent(text.substring(positionEvent.getStartPos(), positionEvent.getEndPos()));
        }

        Position positionEventResult = map.get("eventResult");
        if (null != positionEventResult) {
            event.setEventResult(text.substring(positionEventResult.getStartPos(), positionEventResult.getEndPos()));
        }

        long endTime = System.currentTimeMillis();
        log.info(">>>>>>>>>nlp解析服务，警情概要生成，耗时{}ms.", endTime - startTime);

        return event;
    }
}
