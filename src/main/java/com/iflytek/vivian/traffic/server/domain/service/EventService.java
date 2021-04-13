package com.iflytek.vivian.traffic.server.domain.service;

import com.iflytek.vivian.traffic.server.client.TtsAbilityClient;
import com.iflytek.vivian.traffic.server.constants.Constants;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.domain.dao.IEventDao;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.dto.EventDto;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.TtsActionParam;
import com.iflytek.vivian.traffic.server.dto.TtsDto;
import com.iflytek.vivian.traffic.server.utils.StringUtil;
import com.iflytek.vivian.traffic.server.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @ClassName EventService
 * @Author xinwang41
 * @Date 2021/1/4 10:44
 **/

@Component
@Slf4j
public class EventService {
    @Autowired
    private IEventDao eventDao;

    @Autowired
    private TtsAbilityClient ttsAbilityClient;


    /**
     * 事件上报入库
     * @param eventDto
     * @return
     */
    public Result<Event> saveEvent(EventDto eventDto) {
        try {
            // 当前警员下没有未处理事件，创建新事件
            Event event = null;
            if (StringUtils.isEmpty(eventDto.getId())) {
                event = new Event();
                event.setId(UUIDUtil.uuid());
            }
            event.setPolicemanId(eventDto.getPolicemanId());
            // 创建新事件 默认为未处理 赋值1
            eventDto.setStatus(Constants.EventState.EventReport.getValue());
            event.setStartTime(new Date());
            event.setCreateTime(new Date());

            // 字段为空或者传入不为空时—>赋值覆盖
            if (StringUtils.isEmpty(event.getLocation()) || !StringUtils.isEmpty(eventDto.getLocation())){
                event.setLocation(eventDto.getLocation());
            }
            if (StringUtils.isEmpty(event.getVehicle()) || !StringUtils.isEmpty(eventDto.getVehicle())){
                event.setVehicle(eventDto.getVehicle());
            }
            if (StringUtils.isEmpty(event.getEvent()) || !StringUtils.isEmpty(eventDto.getEvent())){
                event.setEvent(eventDto.getEvent());
            }
            if (StringUtils.isEmpty(event.getEventResult()) || !StringUtils.isEmpty(eventDto.getEventResult())){
                event.setEventResult(eventDto.getEventResult());
            }

            event.setStatus(Constants.EventState.EventReport.getValue());
            event.setIsPlay("0");
            event.setUpdateTime(new Date());

            eventDao.save(event);
            return Result.success(event);
        } catch (Exception e) {
            return Result.fail(ErrorCode.FAIL, "事件上报发生错误：" + e.getMessage());
        }
    }

    /**
     * 关闭消息推送服务
     * @Desciption TODO
     */
    public void closeEventNotify() {

    }

    /**
     * 开启消息推送服务
     * @Desciption TODO
     */
    public void openEventNotify() {

    }
}
