package com.iflytek.vivian.traffic.server.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iflytek.vivian.traffic.server.constants.Constants;
import com.iflytek.vivian.traffic.server.dto.face.FdbImage;
import com.iflytek.vivian.traffic.server.dto.face.FdbResultResponse;
import com.iflytek.vivian.traffic.server.dto.face.ImageSearchScore;
import com.iflytek.vivian.traffic.server.exceptions.FdbRequestException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName FaceAbilityClient
 * @Description 静态人脸检测服务调用端代码
 * @Author xinwang41
 * @Date 2021/1/4 10:41
 **/

@Service
@Slf4j
public class FaceAbilityClient {
    /**
     * 静态人脸识别服务的url地址,这个根据能力在application.properties中填写正确的地址
     */
    @Value("${ability.face.url}")
    private String faceAbilityUrl;

    /**
     * spring初始化时会调用此方法,实现bean初始化
     */
    @PostConstruct
    public void init(){
        try {
            FdbResultResponse rst=createDatabase(Constants.FACE_DB_NAME);
            log.info("创建人脸库结果:{}",rst);
        } catch (Exception e) {
            log.error("创建库失败:{}",e.getMessage(),e);
            throw new FdbRequestException("创建库失败:"+e.getMessage());
        }
    }


    /**
     * post 请求,获取回应string
     * @param url
     * @param body
     * @return
     */
    private String post(String url, RequestBody body){
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();

        StopWatch stopWatch=new StopWatch();
        boolean success=false;
        try {
            stopWatch.start();
            Response response =okHttpClient.newCall(request).execute();
            if (response.isSuccessful()){
                success=true;
                return response.body().string();
            }else{
                throw new FdbRequestException("请求错误:"+response.message());
            }
        } catch (IOException e) {
            throw new FdbRequestException("请求错误:"+e.getMessage());
        }finally {
            stopWatch.stop();
            log.info("请求接口[{}] {} ,耗时:{}S",success?"S":"F",url,stopWatch.getTotalTimeSeconds());
        }
    }




    /**
     * 新建人脸库
     * @param dbName 人脸库名称
     * @return
     */
    public FdbResultResponse createDatabase(String dbName){
        String url=faceAbilityUrl+"/tuling/face/v2/verify/target/add";
        RequestBody formBody = new FormBody.Builder().add("dbName",dbName).build();

        String body=post(url,formBody);
        return JSON.parseObject(body,FdbResultResponse.class);
    }

    /**
     * 添加人脸图片到人脸库
     * @param dbName 人脸库名称
     * @param imageDatas 图片数据
     * @param getFeature 是否获取图片中的人脸特征, 0 - 获取,1 - 不获取
     * @param qualityThreshold 质量阈值,当前为保留项,不起作用.
     * @param props 扩展属性,json串
     * @return
     */
    public FdbResultResponse<List<FdbImage>> addFaceImage(String dbName, byte[] imageDatas , String getFeature, String qualityThreshold, String props){
        String url=faceAbilityUrl+"/tuling/face/v2/verify/face/synAdd";

        MediaType MEDIA_TYPE_TEXT = MediaType.parse("image/png");

        RequestBody partBody = new MultipartBody.Builder()
                .addFormDataPart("dbName",dbName)
                .addFormDataPart("imageDatas","file",RequestBody.create(MEDIA_TYPE_TEXT,imageDatas))
                .addFormDataPart("getFeature",getFeature)
                .addFormDataPart("qualityThreshold",qualityThreshold)
                .addFormDataPart("props", StringUtils.isEmpty(props)?"":props)
                .build();
        String body=post(url,partBody);
        log.info("注册结果:{}",body);
        FdbResultResponse<List<FdbImage>> rst=JSON.parseObject(body,new TypeReference<FdbResultResponse<List<FdbImage>>>(){});
        return rst;
    }

    /**
     * 删除目标人脸库中指定图片
     * @param dbName
     * @return
     */
    public FdbResultResponse removeImage(String dbName,String imageId){
        String url=faceAbilityUrl+"/tuling/face/v2/verify/face/deletes";
        RequestBody formBody = new FormBody.Builder().add("dbName",dbName)
                .add("imageId",imageId).build();

        String body=post(url,formBody);
        return JSON.parseObject(body,FdbResultResponse.class);
    }


    /**
     * 清空目标人脸库中所有图片
     * @param dbName
     * @return
     */
    public FdbResultResponse removeAllImage(String dbName){
        String url=faceAbilityUrl+"/tuling/face/v2/verify/target/clear";
        RequestBody formBody = new FormBody.Builder().add("dbName",dbName).build();

        String body=post(url,formBody);
        return JSON.parseObject(body,FdbResultResponse.class);
    }

    /**
     * 根据图片在人脸库中找出前topN个相似度score~1之间的图片
     * @param dbName
     * @param imageDatas
     * @param topNum
     * @param score
     * @return
     */
    public FdbResultResponse<List<ImageSearchScore>> searchImage(String dbName, byte[] imageDatas, int topNum, double score){
        String url=faceAbilityUrl+"/tuling/face/v2/verify/face/search";
        MediaType MEDIA_TYPE_TEXT = MediaType.parse("image/png");
        RequestBody partBody = new MultipartBody.Builder()
                .addFormDataPart("dbName",dbName)
                .addFormDataPart("imageData","file",RequestBody.create(MEDIA_TYPE_TEXT,imageDatas))
                .addFormDataPart("topNum",String.valueOf(topNum))
                .addFormDataPart("score",String.valueOf(score))
                .build();
        String body=post(url,partBody);
        return JSON.parseObject(body,new TypeReference<FdbResultResponse<List<ImageSearchScore>>>(){});
    }
}
