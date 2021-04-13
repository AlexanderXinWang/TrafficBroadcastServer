package com.iflytek.vivian.traffic.server.controller;

import com.alibaba.fastjson.JSON;
import com.iflytek.vivian.traffic.server.client.IatAbilityClient;
import com.iflytek.vivian.traffic.server.client.TtsAbilityClient;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.service.EventService;
import com.iflytek.vivian.traffic.server.domain.service.NlpService;
import com.iflytek.vivian.traffic.server.dto.EventDto;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.TtsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * 数据单句音频 输出解析文本
     * @param
     * @return
*/
    /*@PostMapping("/ast")
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
    }*/

    @GetMapping("/test")
    @ResponseBody
    public Result<String> test() {
        return Result.success("成功");
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
    @PostMapping("/saveEvent")
    @ResponseBody
    public Result<Event> saveEventDeal(@RequestBody EventDto eventDto) {
        return eventService.saveEvent(eventDto);
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
            String text = "今天天气真好";
            String fileName = "ttsTest";
            return ttsAbilityClient.tts(text, fileName);
        } catch (Exception e) {
            return Result.fail(ErrorCode.FAIL);
        }
    }

}
