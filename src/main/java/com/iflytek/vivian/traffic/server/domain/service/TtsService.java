package com.iflytek.vivian.traffic.server.domain.service;

import com.iflytek.vivian.traffic.server.client.TtsAbilityClient;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.dto.TtsActionParam;
import com.iflytek.vivian.traffic.server.dto.tts.TtsResponse;
import com.iflytek.vivian.traffic.server.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

/**
 * @ClassName TtsService
 * @Author xinwang41
 * @Date 2021/1/4 10:45
 * @Version 1.0
 **/
@Service
@Slf4j
public class TtsService {
    @Autowired
    private TtsAbilityClient ttsAbilityClient;

    /**
     * 获取mp3语音下载地址
     * @param ttsActionParam
     */
    public TtsResponse ttsMp3(TtsActionParam ttsActionParam) {
        try {
            return ttsAbilityClient.ttsMp3(ttsActionParam);
        } catch (Exception e) {
            log.error("识别发生错误:{}", e.getMessage(), e);
            throw new BaseException(ErrorCode.FAIL, "识别错误:" + e.getMessage());
        }
    }

    /**
     *  获取二进制数据
     * @param ttsActionParam
     * @return
     */
    public byte[] stream(TtsActionParam ttsActionParam){

        try {
            return ttsAbilityClient.stream(ttsActionParam);
        }catch (Exception e){
            log.error("识别发生错误:{}",e.getMessage(),e);
            throw new BaseException(ErrorCode.FAIL, "识别错误:" + e.getMessage());
        }
    }
}
