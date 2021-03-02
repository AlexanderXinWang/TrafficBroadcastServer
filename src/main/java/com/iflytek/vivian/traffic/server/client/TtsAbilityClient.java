package com.iflytek.vivian.traffic.server.client;


import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.iflytek.vivian.traffic.server.dto.TtsActionParam;
import com.iflytek.vivian.traffic.server.dto.tts.TtsResponse;
import com.iflytek.vivian.traffic.server.exceptions.FdbRequestException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.IOException;

import static org.reflections.Reflections.log;

/**
 * @ClassName TtsAbilityClient
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 10:42
 * @Version 1.0
 **/
@Service
@Slf4j
public class TtsAbilityClient {
    private String ttsAbilityUrl;

    public String getString() {
        return this.ttsAbilityUrl;
    }

    public TtsResponse ttsMp3(TtsActionParam ttsActionParam) {
        String url = ttsAbilityUrl + "/tuling/tts/v2/mp3";
        long startTime = System.currentTimeMillis();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String ttsTextStr = com.alibaba.fastjson.JSON.toJSONString(ttsActionParam);
        RequestBody partBody = RequestBody.create(JSON, ttsTextStr);

        String bodyResult = post(url, partBody);
        long endTime = System.currentTimeMillis();
        log.info(">>>>>>>>语音合成服务 tts，耗时{}ms.", endTime - startTime);
        return com.alibaba.fastjson.JSON.parseObject(bodyResult, TtsResponse.class);
    }

    /**
     * post请求，获取回应String
     * @param url
     * @param body
     * @return
     */
    private String post(String url, RequestBody body) {
        Request request = new Request.Builder()
                .url(url).post(body).build();

        OkHttpClient okHttpClient = new OkHttpClient();

        StopWatch stopWatch = new StopWatch();

        boolean success = false;

        try {
            stopWatch.start();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                success = true;
                return response.body().string();
            } else {
                throw new FdbRequestException("请求错误:" + response.message());
            }
        } catch (IOException e) {
            throw new FdbRequestException("请求错误:" + e.getMessage());
        } finally {
            stopWatch.stop();
            log.info("请求接口[{}] {} ,耗时:{}s", success? "S" : "F", url, stopWatch.getTotalTimeSeconds() );
        }
    }
}
