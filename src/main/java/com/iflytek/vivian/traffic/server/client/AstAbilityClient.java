package com.iflytek.vivian.traffic.server.client;

import com.iflytek.ast.sdk.session.AstSessionClient;
import com.iflytek.ast.sdk.session.AstSessionResponse;
import com.iflytek.ast.sdk.session.RequestParam;
import com.iflytek.ast.sdk.session.data.LatticeItem;
import com.iflytek.ast.sdk.session.data.ResultItem;
import com.iflytek.ast.sdk.session.data.ResultWordItem;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.dto.Result;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @ClassName AstAbilityClient
 * @Author xinwang41
 * @Date 2021/1/4 10:41
 * @Version 1.0
 **/
@Slf4j
@Service
public class AstAbilityClient {
    @Value("${ability.ast.hostUrl}")
    private String hostUrl;
    @Value("${ability.ast.host}")
    private String host;
    @Value("${ability.ast.path}")
    private String path;
    @Value("${ability.ast.apiKey}")
    private String apikey;
    @Value("${ability.ast.apiSecret}")
    private String apiSecret;

    private String astAbilityUrl;
    private StringBuilder result = new StringBuilder();

    /**
     * spring初始化时调用，实现bean的初始化
     */
    @PostConstruct
    public  void init() {}

    /**
     * 在线语音识别服务的能力使用
     * @param audioFile
     * @return
     */
    public Result<String> ast(MultipartFile audioFile) {
        try {
            long startTime = System.currentTimeMillis();
            // 清空缓存
            result.setLength(0);
            File toFile = null;
            if (audioFile.equals("") || audioFile.getSize()<=0) {
                audioFile = null;
            } else {
                InputStream ins = null;
                ins = audioFile.getInputStream();
                toFile = new File(audioFile.getOriginalFilename());
                inputStreamToFile(ins, toFile);
                ins.close();
            }

            if(!toFile.exists()) {
                log.error("目录[ {} ]不存在", toFile.getAbsolutePath());
            }
            ast(toFile);
            long endTime = System.currentTimeMillis();
            log.info(">>>>>>ast解析服务，耗时{}ms.", endTime - startTime);
            return Result.success(result.toString());
        } catch (Exception e) {
            log.error("ast识别失败", e.getMessage());
            return Result.fail(ErrorCode.FAIL, "ast识别错误" + e.getMessage());
        }
    }

    /**
     * 单条转写测试
     */
    public void ast(File d) throws Exception {
        RequestParam requestParam = new RequestParam("bizId001", UUID.randomUUID().toString(), "WebSocket");
        requestParam.setDomain("2001");
        AstSessionClient astSessionClient = new AstSessionClient(astAbilityUrl, requestParam);
        // 接收服务器端返回的数据
        AstSessionResponse astSessionResponse = new AstSessionResponse() {
            @Override
            public void onCallback(LatticeItem latticeItem) {
                if ("sentence".equals(latticeItem.getMsgtype())) {
                    result.append(getSentence(latticeItem));
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }

            @Override
            public void onClosed(String reason) {
                log.info("closed : reason: {} " + reason);
            }
        };

        boolean isConnect = astSessionClient.connect(astSessionResponse);
        if (isConnect) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(d, "r");
                byte[] buffer = new byte[100*32];
                while (true) {
                    int ret = randomAccessFile.read(buffer);
                    if (ret > 0) {
                        astSessionClient.post(buffer);
                        Thread.sleep(100);
                    } else {
                        break;
                    }
                }
                randomAccessFile.close();
                astSessionClient.end();
            } catch (Exception e) {
                e.printStackTrace();
                astSessionClient.close();
            }
        }
        // 结束会话
        astSessionClient.end();
        // 关闭客户端
        astSessionClient.close();
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

    public String getSentence(LatticeItem latticeItem) {
        StringBuilder stringBuilder = new StringBuilder();
        ResultItem[] resultItems = latticeItem.getWs();
        if (null != resultItems) {
            for (ResultItem resultItem : resultItems) {
                ResultWordItem[] resultWordItems = resultItem.getCw();
                System.err.print(resultWordItems[0].getW());
                for (ResultWordItem resultWordItem : resultWordItems) {
                    stringBuilder.append(resultWordItem.getW());
                }
            }
        }
        return stringBuilder.toString();
    }
}
