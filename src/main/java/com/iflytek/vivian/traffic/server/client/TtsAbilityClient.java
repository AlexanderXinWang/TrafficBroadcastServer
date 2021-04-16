package com.iflytek.vivian.traffic.server.client;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.tts.TtsResponseData;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.ws.RealWebSocket;
import okio.ByteString;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.ErrorManager;

/**
 * 语音合成流式 WebAPI 接口调用示例 接口文档（必看）：https://www.xfyun.cn/doc/tts/online_tts/API.html
 * 语音合成流式WebAPI 服务，发音人使用方式：登陆开放平台https://www.xfyun.cn/后，到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值
 * 错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 * 小语种需要传输小语种文本、使用小语种发音人vcn、tte=unicode以及修改文本编码方式
 */

/**
 * @ClassName TtsAbilityClient
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 10:42
 **/
@Service
@Slf4j
public class TtsAbilityClient {
    /*@Value("${ability.tts.hostUrl")
    private static String hostUrl; //中英文，http url 不支持解析 ws/wss schema
    @Value("${ability.tts.appId")
    private static String appId;
    @Value("${ability.tts.apiSecret")
    private static String apiSecret;
    @Value("${ability.tts.apiKey")
    private static String apiKey;*/
    private static final String hostUrl = "https://tts-api.xfyun.cn/v2/tts"; //http url 不支持解析 ws/wss schema
    private static final String appId = "60346977";//到控制台-语音合成页面获取
    private static final String apiSecret = "6dafbf23712da829593bc7141a202b93";//到控制台-语音合成页面获取
    private static final String apiKey = "61581ff635d25cac9edc3eb101743a7d";//到控制台-语音合成页面获取
    private static final String text = "讯飞开放平台";
    public static final Gson json = new Gson();


    /**
     * 语音合成能力调用
     * @param text
     * @param fileName
     * @return
     */
    public Result<File> tts(String text, String fileName) {
        try {
            // 构建鉴权url
            String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
            OkHttpClient client = new OkHttpClient.Builder().build();
            //将url中的 schema http://和https://分别替换为ws:// 和 wss://
            String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
            Request request = new Request.Builder().url(url).build();
            // 存放音频的文件
            File file = new File("src/main/resources/tts/" + fileName + ".pcm");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(file);

            WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    super.onOpen(webSocket, response);
                    try {
                        System.out.println(response.body().string());
                    } catch (IOException e) {
                        log.error("responseBody获取失败", e.getMessage());
                    }
                    //发送数据
                    JsonObject frame = new JsonObject();
                    JsonObject business = new JsonObject();
                    JsonObject common = new JsonObject();
                    JsonObject data = new JsonObject();
                    // 填充common
                    common.addProperty("app_id", appId);
                    //填充business
                    business.addProperty("aue", "raw");
                    business.addProperty("tte", "UTF8");//小语种必须使用UNICODE编码
                    business.addProperty("vcn", "xiaoyan");//到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值，若试用未添加的发音人会报错11200
                    business.addProperty("pitch", 50);
                    business.addProperty("speed", 50);
                    //填充data
                    data.addProperty("status", 2);//固定位2
                    try {
                        data.addProperty("text", Base64.getEncoder().encodeToString(text.getBytes("utf8")));
                        //使用小语种须使用下面的代码，此处的unicode指的是 utf16小端的编码方式，即"UTF-16LE"”
                        //data.addProperty("text", Base64.getEncoder().encodeToString(text.getBytes("UTF-16LE")));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        log.error("注入文本数据编码失败", e.getMessage());
                    }
                    //填充frame
                    frame.add("common", common);
                    frame.add("business", business);
                    frame.add("data", data);
                    webSocket.send(frame.toString());
                }
                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    super.onMessage(webSocket, text);
                    //处理返回数据
                    System.out.println("receive=>" + text);
                    TtsResponseData resp = null;
                    try {
                        resp = json.fromJson(text, TtsResponseData.class);
                    } catch (Exception e) {
                        log.error("返回数据获取失败", e.getMessage());
                    }
                    if (resp != null) {
                        if (resp.getCode() != 0) {
                            System.out.println("error=>" + resp.getMessage() + " sid=" + resp.getSid());
                            return;
                        }
                        if (resp.getData() != null) {
                            String result = resp.getData().audio;
                            byte[] audio = Base64.getDecoder().decode(result);
                            try {
                                os.write(audio);
                                os.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (resp.getData().status == 2) {
                                // todo  resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
                                System.out.println("session end ");
                                System.out.println("合成的音频文件保存在：" + file.getPath());
                                webSocket.close(1000, "");
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                    log.info("socket closing");
                }
                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    super.onClosed(webSocket, code, reason);
                    log.info("socket closed");
                }
                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    super.onFailure(webSocket, t, response);
                    log.error("connection failed");
                }
            });

            //TODO 线程等待
            Thread.sleep(1000*10);

            return Result.success(file);
        } catch (Exception e) {
            log.error("tts语音合成失败", e.getMessage());
            return Result.fail(ErrorCode.FAIL, "tts语音合成失败" + e.getMessage());
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
        String authorization = String.format("hmac username=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();
        return httpUrl.toString();
    }
}
