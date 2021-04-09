package com.iflytek.vivian.traffic.server.controller;

import com.iflytek.vivian.traffic.server.domain.service.TtsService;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.TtsActionParam;
import com.iflytek.vivian.traffic.server.dto.tts.TtsResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName PulicController
 * @Author xinwang41
 * @Date 2021/1/4 10:43
 **/
public class PublicController {
    @Autowired
    private TtsService ttsService;

    /*@PostMapping("/ast")
    public Result<String> ast(@RequestParam(value = "audioFile", required = true)MultipartFile audioFile) {
        return astAbilityClient.ast(audioFile);
    }*/

    /**
     * 获取MP3下载地址
     * @param ttsActionParam
     * @Description TODO
     * @return
     */
    @PostMapping("/ttsmp3")
    @ResponseBody
    public TtsResponseData ttsMp3(TtsActionParam ttsActionParam) {
//        return ttsService.ttsMp3(ttsActionParam);
        return null;
    }
}
