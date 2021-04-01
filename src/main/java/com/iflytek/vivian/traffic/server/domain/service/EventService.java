package com.iflytek.vivian.traffic.server.domain.service;

import com.iflytek.vivian.traffic.server.constants.Constants;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.domain.dao.IEventDao;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.dto.EventDto;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.TtsActionParam;
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
    private TtsService ttsService;

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
     * 为处理事件的播报
     * @return
     */
    /*public List<TtsResponse> eventBroadcast(){
        List<TtsResponse> ttsResponseList = new ArrayList<>();
        // 上报完毕 未处理的事件
        List<Event> eventList = eventDao.findByStatusAndIsPlay(Constants.EventState.EventReport.getValue(), Constants.EventState.EventIsPlay.getValue());

        Map<Integer, Integer> ttsMap = new HashMap<>();
        ttsMap.put(3, 60020);
        ttsMap.put(6,2);
        for (Event event : eventList){
            TtsResponse ttsResponse = new TtsResponse();
            String ttsText = "";
            ttsText += "事故发生地是：" + event.getLocation();
            ttsText += "车辆类型是：" + event.getVehicle();
            ttsText += "发生事件是：" + event.getEvent();
            ttsText += "事件结果是：" + event.getEventResult();

            TtsActionParam ttsActionParam = new TtsActionParam();
            ttsActionParam.setParams(ttsMap);
            ttsActionParam.setText(ttsText);

            ttsResponse = ttsService.ttsMp3(ttsActionParam);
            ttsResponseList.add(ttsResponse);
        }
        return ttsResponseList;
    }*/

    /**
     * 查询未处理事件
     * @return
     */
    public Result<List<Event>> listEvent(String isPlay){
        Map<Integer, Integer> ttsMap = new HashMap<>();
        ttsMap.put(3, 60020);
        ttsMap.put(6,2);
        List<Event> eventList = new ArrayList<>();
        if ("0".equals(isPlay)){
            eventList = eventDao.findByStatusAndIsPlay(Constants.EventState.EventReport.getValue(), Constants.EventState.EventIsPlay.getValue());

            // 将查询出的事件 修改播放状态1 已经播报
            for (Event event : eventList){
                event.setIsPlay("1");
                eventDao.save(event);
            }
        }else {
            eventList = eventDao.findByStatus(Constants.EventState.EventReport.getValue());
        }

        for (Event event : eventList){
            String ttsText = "";
            if (!StringUtils.isEmpty(event.getLocation())){
                ttsText += "事故发生地是：" + event.getLocation();
            }
            if (!StringUtils.isEmpty(event.getVehicle())){
                ttsText += "车辆类型是：" + event.getVehicle();
            }
            if (!StringUtils.isEmpty(event.getEvent())){
                ttsText += "发生事件是：" + event.getEvent();
            }
            if (!StringUtils.isEmpty(event.getEventResult())){
                ttsText += "事件结果是：" + event.getEventResult();
            }

            TtsActionParam ttsActionParam = new TtsActionParam();
            ttsActionParam.setParams(ttsMap);
            ttsActionParam.setText(ttsText);

            // isHall == 0 是大厅展示，需要语音合成
            if ("0".equals(isPlay)){
                event.setMp3(ttsService.stream(ttsActionParam));
            }
        }
        return Result.success(eventList);
    }

    /**
     * 设置事件状态（已处理） 并返回未处理时间列表
     * @param eventDto
     * @return
     */
    public Result<List<Event>> setEventStatus(EventDto eventDto){
        if (StringUtils.isEmpty(eventDto.getId())){
            return Result.fail("事件信息错误");
        }
        Event event = eventDao.findOne(eventDto.getId());
        if (event == null || !eventDto.getId().equals(event.getId())){
            return Result.fail("未查找到相关事件");
        }

        // 设置事件已经处理
        event.setStatus(Constants.EventState.EventOk.getValue());
        eventDao.save(event);

        // 获取未处理事件  不是大厅1
        return listEvent("1");
    }
}
