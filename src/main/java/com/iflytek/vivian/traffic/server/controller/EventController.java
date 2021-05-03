package com.iflytek.vivian.traffic.server.controller;

import com.alibaba.fastjson.JSON;
import com.iflytek.vivian.traffic.server.client.IatAbilityClient;
import com.iflytek.vivian.traffic.server.client.TtsAbilityClient;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.entity.User;
import com.iflytek.vivian.traffic.server.domain.service.EventService;
import com.iflytek.vivian.traffic.server.domain.service.NlpService;
import com.iflytek.vivian.traffic.server.dto.EventDto;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.TtsDto;
import com.iflytek.vivian.traffic.server.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.RescaleOp;
import java.io.File;
import java.security.PrivateKey;
import java.util.List;
import java.util.PrimitiveIterator;

/**
 * @ClassName EventController
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 10:42
 * @Version 1.0
 **/

@RestController
@RequestMapping("/event")
@Slf4j
public class EventController {

    @Autowired
    private IatAbilityClient iatAbilityClient;

    @Autowired
    private NlpService nlpService;

    @Autowired
    private EventService eventService;

    @Autowired
    private TtsAbilityClient ttsAbilityClient;

    /**
     * 警情上报
     * 数据单句音频，输出解析文本
     * @param file
     * @return
     */
    @PostMapping("/iat")
    @ResponseBody
    public Result<EventDto> iatEvent(@RequestPart("file") MultipartFile file) {
        try {
            Result<String> iatResult = iatAbilityClient.iat(file);
            EventDto eventDto = nlpService.getEventCase(iatResult.getData());
            eventDto.setIatResult(iatResult.getData());
            log.info("nlp event = {}", JSON.toJSONString(eventDto));
            return Result.success(eventDto);
        } catch (Exception e) {
            log.info("语音识别失败");
            return Result.fail("识别失败");
        }
    }

    /**
     * 保存事件
     * @param eventDto
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result<Event> saveEvent(@RequestBody EventDto eventDto) {
        return eventService.saveEvent(eventDto);
    }

    /**
     * 批量删除事件
     * @param eventIds
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Result<Boolean> deleteEvent(@RequestBody List<String> eventIds) {
        return eventService.deleteEvent(eventIds);
    }

    /**
     * 更新警情事件
     * @param eventDto
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<Event> updateEvent(@RequestBody EventDto eventDto) {
        return eventService.updateEvent(eventDto);
    }

    /**
     * 查询所有警情事件
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<List<Event>> listEvent() {
        return eventService.listEvent();
    }

    /**
     * 查询单个警情事件的详情信息
     * @param eventId
     * @return
     */
    @PostMapping("/detail/{eventId}")
    @ResponseBody
    public Result<Event> selectEvent(@PathVariable String eventId) {
        return eventService.selectEvent(eventId);
    }

    /**
     * 查询当前警员上报的所有事件
     * @param userId
     * @return
     */
    @PostMapping("/{userId}/record")
    @ResponseBody
    public Result<List<Event>> getReportedEventList(@PathVariable String userId) {
        return eventService.getPolicemanRecord(userId);
    }

    @GetMapping("/list/time/asc")
    @ResponseBody
    public Result<List<Event>> listEventByTimeAsc(){
        return eventService.listEventByTimeAsc();
    }

    @GetMapping("/list/time/desc")
    @ResponseBody
    public Result<List<Event>> listEventByTimeDesc(){
        return eventService.listEventByTimeDesc();
    }

    @GetMapping("/list/event/asc")
    @ResponseBody
    public Result<List<Event>> listEventByEventAsc(){
        return eventService.listEventByEventAsc();
    }

    @GetMapping("/list/event/desc")
    @ResponseBody
    public Result<List<Event>> listEventByEventDesc(){
        return eventService.listEventByEventDesc();
    }

    @GetMapping("/list/location/asc")
    @ResponseBody
    public Result<List<Event>> listEventByLocationAsc(){
        return eventService.listEventByLocationAsc();
    }

    @GetMapping("/list/location/desc")
    @ResponseBody
    public Result<List<Event>> listEventByLocationDesc(){
        return eventService.listEventByLocationDesc();
    }

    @GetMapping("/list/name/asc")
    @ResponseBody
    public Result<List<Event>> listEventByNameAsc(){
        return eventService.listEventByNameAsc();
    }

    @GetMapping("/list/name/desc")
    @ResponseBody
    public Result<List<Event>> listEventByNameDesc(){
        return eventService.listEventByNameDesc();
    }

    /**
     * 播放警情事件（开/关）
     * @param ttsDto
     * @return
     */
    @PostMapping("playEvent")
    @ResponseBody
    public Result<String> playEvent(TtsDto ttsDto) {
        if (ttsDto.isPlay()) {
            ttsDto.setPlay(false);
            eventService.closeEventNotify();
        } else {
            ttsDto.setPlay(true);
            eventService.openEventNotify();
        }
        return null;
    }

    @PostMapping("/tts")
    @ResponseBody
    public Result<File> tts() {
        try {
            String text = "地点是望江西路666号，车辆类型是两个电动车，事件是两车相撞，现在人在医院，电动车在现场，有人员头部受伤，事件结果是撞的挺严重，不清醒，在医院接受治疗。";
            String fileName = "ttsTest";
            return ttsAbilityClient.tts(text, fileName);
        } catch (Exception e) {
            return Result.fail(ErrorCode.FAIL);
        }
    }

}
