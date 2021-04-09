package com.iflytek.vivian.traffic.server.controller;

import com.alibaba.fastjson.JSON;
import com.iflytek.vivian.traffic.server.client.AstAbilityClient;
import com.iflytek.vivian.traffic.server.client.IatAbilityClient;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.service.EventService;
import com.iflytek.vivian.traffic.server.domain.service.NlpService;
import com.iflytek.vivian.traffic.server.dto.EventDto;
import com.iflytek.vivian.traffic.server.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private AstAbilityClient astAbilityClient;

    @Autowired
    private IatAbilityClient iatAbilityClient;

    @Autowired
    private NlpService nlpService;

    @Autowired
    private EventService eventService;

    /**
     * 数据单句音频 输出解析文本
     * @param file
     * @return
     */
    @PostMapping("/ast")
    @ResponseBody
    public Result<Event> astEvent(@RequestPart("file") MultipartFile file) {
        try {
            Result<String> astResult = astAbilityClient.ast(file);
            Event event = nlpService.getEventCaseSimple(astResult.getData());
            event.setAstResult(astResult.getData());

            log.info("nlp event = {}", JSON.toJSONString(event));
            return Result.success(event);
        } catch (Exception e){
            log.info("语音识别失败");
            return Result.fail("识别失败");
        }
    }

    /**
     * 警情上报
     * 数据单句音频，输出解析文本
     * @param file
     * @return
     */
    @PostMapping("/iat")
    @ResponseBody
    public Result<Event> iatEvent(@RequestPart("file") MultipartFile file) {
        try {
            Result<String> iatResult = iatAbilityClient.iat(file);
            Event event = nlpService.getEventCaseSimple(iatResult.getData());
            event.setIatResult(iatResult.getData());

            log.info("nlp event = {}", JSON.toJSONString(event));
            return Result.success(event);
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
    public Result<Event> saveEventDeal(@RequestBody EventDto eventDto) {
        return eventService.saveEvent(eventDto);
    }

    /**
     * 查询未处理事件
     * @return
     */
    @PostMapping("listEvent")
    @ResponseBody
    public Result<List<Event>> listEvent(@RequestParam(name = "isPlay")String isPlay){
        return eventService.listEvent(isPlay);
    }

    /**
     * 查询未处理事件
     * @return
     */
    @PostMapping("setEventStatus")
    @ResponseBody
    public Result<List<Event>> setEventStatus(EventDto eventDto){
        return eventService.setEventStatus(eventDto);
    }
}
