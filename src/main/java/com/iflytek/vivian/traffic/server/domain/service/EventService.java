package com.iflytek.vivian.traffic.server.domain.service;

import com.iflytek.vivian.traffic.server.constants.Constants;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.domain.dao.IEventDao;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.dto.*;
import com.iflytek.vivian.traffic.server.utils.TtsUtil;
import com.iflytek.vivian.traffic.server.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    private TtsUtil ttsUtil;

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
            event.setPolicemanName(eventDto.getPolicemanName());
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

            event.setIatResult(eventDto.getIatResult());
//            event.setStatus(Constants.EventState.EventReport.getValue());
            event.setIsPlay("0");
            event.setPcm(ttsUtil.generatePcmUrl(event));

            eventDao.save(event);

            return Result.success(event);
        } catch (Exception e) {
            return Result.fail(ErrorCode.FAIL, "事件上报发生错误：" + e.getMessage());
        }
    }

    /**
     * 批量删除警情事件
     * @param eventIds
     * @return
     */
    public Result<Boolean> deleteEvent(List<String> eventIds) {
        try {
            List<Event> eventList = new ArrayList<>();
            for (String eventId : eventIds) {
                if (StringUtils.isEmpty(eventId)) {
                    return Result.fail("删除警情事件信息错误");
                }
                Event event = eventDao.findOne(eventId);
                if (null == event || !eventId.equals(event.getId())) {
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
            event.setPolicemanName(eventDto.getPolicemanName());
            event.setLocation(eventDto.getLocation());
            event.setVehicle(eventDto.getVehicle());
            event.setEvent(event.getEvent());
            event.setEventResult(event.getEventResult());
            event.setUpdateTime(new Date());
            event.setStatus(event.getStatus());
            event.setIsPlay(eventDto.getIsPlay());
//          TODO  event.setDesc();
            event.setIatResult(eventDto.getIatResult());
            event.setPcm(ttsUtil.generatePcmUrl(event));

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
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        return Result.success(eventDao.findAll(sort));
    }

    /**
     * 查询单个警情事件的详情信息
     * @param eventId
     * @return
     */
    public Result<Event> selectEvent(String eventId) {
        return Result.success(eventDao.findOne(eventId));
    }

    /**
     * 根据警员id查找事件
     * @param userId
     * @return
     */
    public Result<List<Event>> listEventByPolicemanId(String userId) {
        return Result.success(eventDao.findEventsByPolicemanId(userId));
    }

    /**
     * 查询所有警情（时间升序）
     * @return
     */
    public Result<List<Event>> listEventByTimeAsc() {
        Sort sort = new Sort(Sort.Direction.ASC, "startTime");
        return Result.success(eventDao.findAll(sort));
    }

    /**
     * 查询所有警情（时间降序）
     * @return
     */
    public Result<List<Event>> listEventByTimeDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        return Result.success(eventDao.findAll(sort));
    }

    public Result<List<Event>> listEventByEventAsc() {
        return Result.success(eventDao.orderByEventAsc());
    }

    public Result<List<Event>> listEventByEventDesc() {
        return Result.success(eventDao.orderByEventDesc());
    }

    public Result<List<Event>> listEventByLocationAsc() {
        return Result.success(eventDao.orderByLocationAsc());
    }

    public Result<List<Event>> listEventByLocationDesc() {
        return Result.success(eventDao.orderByLocationDesc());
    }

    public Result<List<Event>> listEventByNameAsc() {
        return Result.success(eventDao.orderByNameAsc());
    }

    public Result<List<Event>> listEventByNameDesc() {
        return Result.success(eventDao.orderByNameDesc());
    }

    public Result<List<String>> playEvent() {
        List<Event> eventList = eventDao.findEventsByIsPlay("0");

        if (eventList == null) {
            return Result.fail("未查询到未播放事件");
        }

        List<String> pcmList = new ArrayList<>();
        for (Event event : eventList) {
            pcmList.add(event.getPcm());
        }
        if (pcmList == null) {
            return Result.fail("未查询到未播放事件");
        }
        return Result.success(pcmList);
    }
}
