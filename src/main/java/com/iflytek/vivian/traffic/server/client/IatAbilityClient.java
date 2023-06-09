package com.iflytek.vivian.traffic.server.client;

/**
 * @ClassName IatAbilityClient
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/3/29 11:17
 **/

import com.google.gson.Gson;
import com.iflytek.ast.sdk.session.data.LatticeItem;
import com.iflytek.ast.sdk.session.data.ResultItem;
import com.iflytek.ast.sdk.session.data.ResultWordItem;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.iat.IatDecoder;
import com.iflytek.vivian.traffic.server.dto.iat.IatResponseData;
import com.iflytek.vivian.traffic.server.dto.iat.IatText;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 语音听写流式 WebAPI 接口调用示例 接口文档（必看）：https://doc.xfyun.cn/rest_api/语音听写（流式版）.html
 * webapi 听写服务参考帖子（必看）：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=38947&extra=
 * 语音听写流式WebAPI 服务，热词使用方式：登陆开放平台https://www.xfyun.cn/后，找到控制台--我的应用---语音听写---个性化热词，上传热词
 * 注意：热词只能在识别的时候会增加热词的识别权重，需要注意的是增加相应词条的识别率，但并不是绝对的，具体效果以您测试为准。
 * 错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 * 语音听写流式WebAPI 服务，方言或小语种试用方法：登陆开放平台https://www.xfyun.cn/后，在控制台--语音听写（流式）--方言/语种处添加
 * 添加后会显示该方言/语种的参数值
 */
@Service
@Slf4j
public class IatAbilityClient {
    /*@Value("${ability.iat.hostUrl")
    private static String hostUrl; //中英文，http url 不支持解析 ws/wss schema
    @Value("${ability.iat.appId")
    private static String appId;
    @Value("${ability.iat.apiSecret")
    private static String apiSecret;
    @Value("${ability.iat.apiKey")
    private static String apiKey;*/
    private static final String hostUrl = "https://iat-api.xfyun.cn/v2/iat"; //中英文，http url 不支持解析 ws/wss schema
    private static final String appId = "60346977"; //在控制台-我的应用获取
    private static final String apiSecret = "6dafbf23712da829593bc7141a202b93"; //在控制台-我的应用-语音听写（流式版）获取
    private static final String apiKey = "61581ff635d25cac9edc3eb101743a7d"; //在控制台-我的应用-语音听写（流式版）获取
//    private static final String file = "src\\main\\resources\\iat\\news.pcm"; // 中文
    public static final int StatusFirstFrame = 0;
    public static final int StatusContinueFrame = 1;
    public static final int StatusLastFrame = 2;
    public static final Gson json = new Gson();
    private static StringBuilder result = new StringBuilder();

    IatDecoder decoder = new IatDecoder();
    // 开始时间
    private static Date dateBegin = new Date();
    // 结束时间
    private static Date dateEnd = new Date();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm");

    /**
     * spring初始化时调用，实现bean的初始化
     */
    @PostConstruct
    public  void init() {}

    /**
     * 单条转写测试
     */
    public void iat(File file) throws IOException {
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        log.info("response={}", response);

        final CountDownLatch latch = new CountDownLatch(1);

        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                new Thread(()->{
                    //连接成功，开始发送数据
                    int frameSize = 1280; //每一帧音频的大小,建议每 40ms 发送 122B
                    int interval = 40;
                    int status = 0;  // 音频的状态
                    try (FileInputStream fs = new FileInputStream(file)) {
                        byte[] buffer = new byte[frameSize];
                        // 发送音频
                        end:
                        while (true) {
                            int len = fs.read(buffer);
                            if (len == -1) {
                                status = StatusLastFrame;  //文件读完，改变status 为 2
                            }
                            switch (status) {
                                case StatusFirstFrame:   // 第一帧音频status = 0
                                    JsonObject frame = new JsonObject();
                                    JsonObject business = new JsonObject();  //第一帧必须发送
                                    JsonObject common = new JsonObject();  //第一帧必须发送
                                    JsonObject data = new JsonObject();  //每一帧都要发送
                                    // 填充common
                                    common.addProperty("app_id", appId);
                                    //填充business
                                    business.addProperty("language", "zh_cn");
                                    business.addProperty("domain", "iat");
                                    business.addProperty("accent", "mandarin");//中文方言请在控制台添加试用，添加后即展示相应参数值
                                    //business.addProperty("nunum", 0);
                                    //business.addProperty("ptt", 0);//标点符号
                                    //business.addProperty("rlang", "zh-hk"); // zh-cn :简体中文（默认值）zh-hk :繁体香港(若未授权不生效，在控制台可免费开通)
                                    //business.addProperty("vinfo", 1);
                                    business.addProperty("dwa", "wpgs");//动态修正(若未授权不生效，在控制台可免费开通)
                                    //business.addProperty("nbest", 5);// 句子多候选(若未授权不生效，在控制台可免费开通)
                                    //business.addProperty("wbest", 3);// 词级多候选(若未授权不生效，在控制台可免费开通)
                                    //填充data
                                    data.addProperty("status", StatusFirstFrame);
                                    data.addProperty("format", "audio/L16;rate=16000");
                                    data.addProperty("encoding", "raw");
                                    data.addProperty("audio", Base64.getEncoder().encodeToString(Arrays.copyOf(buffer, len)));
                                    //填充frame
                                    frame.add("common", common);
                                    frame.add("business", business);
                                    frame.add("data", data);
                                    webSocket.send(frame.toString());
                                    status = StatusContinueFrame;  // 发送完第一帧改变status 为 1
                                    break;
                                case StatusContinueFrame:  //中间帧status = 1
                                    JsonObject frame1 = new JsonObject();
                                    JsonObject data1 = new JsonObject();
                                    data1.addProperty("status", StatusContinueFrame);
                                    data1.addProperty("format", "audio/L16;rate=16000");
                                    data1.addProperty("encoding", "raw");
                                    data1.addProperty("audio", Base64.getEncoder().encodeToString(Arrays.copyOf(buffer, len)));
                                    frame1.add("data", data1);
                                    webSocket.send(frame1.toString());
                                    // System.out.println("send continue");
                                    break;
                                case StatusLastFrame:    // 最后一帧音频status = 2 ，标志音频发送结束
                                    JsonObject frame2 = new JsonObject();
                                    JsonObject data2 = new JsonObject();
                                    data2.addProperty("status", StatusLastFrame);
                                    data2.addProperty("audio", "");
                                    data2.addProperty("format", "audio/L16;rate=16000");
                                    data2.addProperty("encoding", "raw");
                                    frame2.add("data", data2);
                                    webSocket.send(frame2.toString());
                                    log.info("sendlast");
                                    break end;
                            }
                            Thread.sleep(interval); //模拟音频采样延时
                        }
                        System.out.println("all data is send");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                IatResponseData resp = json.fromJson(text, IatResponseData.class);
                if (resp != null) {
                    if (resp.getCode() != 0) {
                        log.info( "code=>" + resp.getCode() + " error=>" + resp.getMessage() + " sid=" + resp.getSid());
                        log.info( "错误码查询链接：https://www.xfyun.cn/document/error-code");
                        return;
                    }
                    if (resp.getData() != null) {
                        if (resp.getData().getResult() != null) {
                            IatText te = resp.getData().getResult().getText();
                            //System.out.println(te.toString());
                            try {
                                decoder.decode(te);
                                log.info("中间识别结果 ==》" + decoder.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (resp.getData().getStatus() == 2) {
                            // todo  resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
                            log.info("session end ");
                            dateEnd = new Date();
                            log.info(sdf.format(dateBegin) + "开始");
                            log.info(sdf.format(dateEnd) + "结束");
                            log.info("耗时:" + (dateEnd.getTime() - dateBegin.getTime()) + "ms");
                            log.info("最终识别结果 ==》" + decoder.toString());
                            log.info("本次识别sid ==》" + resp.getSid());
                            result.append(decoder.toString());
                            log.info("result=" + result);
                            decoder.discard();
                            webSocket.close(1000, "");
                            latch.countDown();
                        } else {
                            // todo 根据返回的数据处理
                        }
                    }
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                response.close();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                try {
                    if (null != response) {
                        int code = response.code();
                        log.error("onFailure code:" + code);
                        log.error("onFailure body:" + response.body().string());
                        if (101 != code) {
                            log.error("connection failed");
                            System.exit(0);
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        try {
            latch.await(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("iat服务请求超时", e.getMessage());
        }
    }

    /**
     * 在线语音识别能力
     * @param audioFile
     * @return
     */
    public Result<String> iat(MultipartFile audioFile) {
        try {
            result.setLength(0);
            File toFile = null;
            if (audioFile.equals("") || audioFile.getSize()<=0) {
                audioFile = null;
            } else {
                InputStream is = null;
                is = audioFile.getInputStream();
                toFile = new File(audioFile.getOriginalFilename());
                inputStreamToFile(is, toFile);
                is.close();
            }

            if (!toFile.exists()) {
                log.error("转换文件失败", toFile.getAbsolutePath());
            }

            iat(toFile);

            log.info(result.toString());

            return Result.success(result.toString());
        } catch (Exception e) {
            log.error("iat识别失败", e.getMessage());
            return Result.fail(ErrorCode.FAIL,"iat识别错误" + e.getMessage());
        }
    }

    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) {
        URL url = null;
        try {
            url = new URL(hostUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").//
                append("date: ").append(date).append("\n").//
                append("GET ").append(url.getPath()).append(" HTTP/1.1");
        //System.out.println(builder);
        Charset charset = Charset.forName("UTF-8");
        Mac mac = null;
        try {
            mac = Mac.getInstance("hmacsha256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        try {
            mac.init(spec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);

        //System.out.println(sha);
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        //System.out.println(authorization);
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder().
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();
        return httpUrl.toString();
    }

    public void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
