package com.iflytek.vivian.traffic.server.controller;

import com.iflytek.vivian.traffic.server.domain.service.TtsService;
import org.springframework.beans.factory.annotation.Autowired;

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
}
