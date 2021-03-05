package com.iflytek.vivian.traffic.server.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName AssembleAuthUrlUtil
 * @Author xinwang41
 * @Date 2021/3/4 16:03
 **/

@Component
public class AssembleAuthUrlUtil {
    private AssembleAuthUrlUtil() {

    }

    @Autowired
    private StringUtil stringUtil;

    public String assembleAuthUrl(String hostUrl, String host, String path, String apiSecret, String apiKey) throws NoSuchAlgorithmException, InvalidKeyException {
        //签名时间
        String date = stringUtil.utcTime();
        //参与签名的字段 host ,date, request-line
        String[] signString = {"host: "+host, "date: "+date, "GET"+path+"HTTP/1.1"};
        //拼接签名字符串
        String sign = String.join("\n", signString);
        //签名结果
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(apiSecret.getBytes(), sign);
        sha256_HMAC.init(secretKey);
        String sha = Base64.encodeBase64String(sha256_HMAC.doFinal(sign.getBytes()));
        //构建请求参数
        String authUrl = "api_key="+"\""+apiKey+"\""+", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature="+"\""+sha+"\"";
        //将请求参数使用base64编码
        String authorization = Base64.encodeBase64String(authUrl.getBytes());
        //请求参数添加host与date
        String authorization_real = authorization + "&host=" + host + "&date=" +date;
        //将编码后的字符串url encode后添加到url后面
        String callUrl = hostUrl + "?" + authorization_real;

        return callUrl;
    }
}
