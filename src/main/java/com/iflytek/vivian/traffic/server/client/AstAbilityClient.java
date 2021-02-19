package com.iflytek.vivian.traffic.server.client;

import com.iflytek.ast.sdk.session.RequestParam;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.OverlappingFileLockException;
import java.util.logging.FileHandler;

/**
 * @ClassName AstAbilityClient
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 10:41
 * @Version 1.0
 **/
@Slf4j
@Service
public class AstAbilityClient {
    private String astAbilityUrl;

    private StringBuilder result = new StringBuilder();

    /**
     * spring初始化时调用，实现bean的初始化
     */
    @PostConstruct
    public  void init() {}

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
        RequestParam requestParam = new RequestParam();
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
