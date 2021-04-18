package com.iflytek.vivian.traffic.server.domain.service;

import com.iflytek.vivian.traffic.server.client.TtsAbilityClient;
import com.iflytek.vivian.traffic.server.constants.Constants;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.domain.dao.IEventDao;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.dto.*;
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

//            event.setStatus(Constants.EventState.EventReport.getValue());
            event.setIsPlay("0");
            event.setUpdateTime(new Date());

            eventDao.save(event);

            return Result.success(event);
        } catch (Exception e) {
            return Result.fail(ErrorCode.FAIL, "事件上报发生错误：" + e.getMessage());
        }
    }

    /**
     * 批量删除警情事件
     * @param eventDtoList
     * @return
     */
    public Result<Boolean> deleteEvent(List<EventDto> eventDtoList) {
        try {
            List<Event> eventList = new ArrayList<>();
            for (EventDto eventDto : eventDtoList) {
                if (StringUtils.isEmpty(eventDto.getId())) {
                    return Result.fail("删除警情事件信息错误");
                }
                Event event = eventDao.findOne(eventDto.getId());
                if (null == event || !eventDto.getId().equals(event.getId())) {
                    return Result.fail("未查找到指定事件");
                }
                eventList.add(event);
            }
            eventDao.deleteInBatch(eventList);
            return Result.success(true);
        } catch (Exception e) {
            return Result.fail("删除事件错误：" + e.getMessage());
        }
    }

    /**
     * 更新警情事件信息
     * @param eventDto
     * @return
     */
    public Result<Event> updateEvent(EventDto eventDto) {
        try {
            if (StringUtils.isEmpty(eventDto.getId())) {
                return Result.fail("事件信息错误");
            }
            Event event = eventDao.findOne(eventDto.getId());
            if (null == event || !eventDto.getId().equals(event.getId())) {
                return Result.fail("未查找到指定事件");
            }
            // 赋值
            event.setPolicemanId(eventDto.getPolicemanId());
            event.setLocation(eventDto.getLocation());
            event.setVehicle(eventDto.getVehicle());
            event.setEvent(event.getEvent());
            event.setEventResult(event.getEventResult());
            event.setStatus(event.getStatus());

            eventDao.save(event);

            return Result.success(event);
        } catch (Exception e) {
            return Result.fail("更新警情事件信息错误：" + e.getMessage());
        }
    }

    /**
     * 查询所有警情事件
     * @return
     */
    public Result<List<Event>> listEvent() {
        return Result.success(eventDao.findAll());
    }

    /**
     * 查询单个警情事件的详情信息
     * @param userDto
     * @return
     */
    public Result<Event> selectEvent(UserDto userDto) {
        return Result.success(eventDao.findOne(userDto.getId()));
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
