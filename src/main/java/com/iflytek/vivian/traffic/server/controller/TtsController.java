package com.iflytek.vivian.traffic.server.controller;

import com.iflytek.vivian.traffic.server.domain.service.TtsService;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.tts.TtsResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName TtsController
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 10:43
 * @Version 1.0
 **/
public class TtsController {
    @Autowired
    private TtsService ttsService;

    /**
     * 获取MP3下载地址
     * @param ttsActionParam
     * @Description TODO
     * @return
     */
    /*@PostMapping("/ttsmp3")
    @ResponseBody
    public TtsResponse ttsMp3(TtsActionParam ttsActionParam){
        return ttsService.ttsMp3(ttsActionParam);
    }*/

    @PostMapping("/tts")
    @ResponseBody
    public Result<String> tts() {
        return null;
    }
}
