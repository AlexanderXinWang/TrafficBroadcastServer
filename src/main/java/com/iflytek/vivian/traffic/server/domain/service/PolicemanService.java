package com.iflytek.vivian.traffic.server.domain.service;

import com.alibaba.fastjson.JSON;
import com.iflytek.vivian.traffic.server.client.FaceAbilityClient;
import com.iflytek.vivian.traffic.server.constants.Constants;
import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.domain.dao.IPolicemanDao;
import com.iflytek.vivian.traffic.server.domain.entity.User;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.UserDto;
import com.iflytek.vivian.traffic.server.dto.face.FdbImage;
import com.iflytek.vivian.traffic.server.dto.face.FdbImageExtraProp;
import com.iflytek.vivian.traffic.server.dto.face.FdbResultResponse;
import com.iflytek.vivian.traffic.server.dto.face.ImageSearchScore;
import com.iflytek.vivian.traffic.server.exceptions.FdbRequestException;
import com.iflytek.vivian.traffic.server.utils.FdbResponseUtil;
import com.iflytek.vivian.traffic.server.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @ClassName PolicemanService
 * @Author xinwang41
 * @Date 2021/1/4 10:45
 **/

@Component
public class PolicemanService {
    @Autowired
    private IPolicemanDao policemanDao;

    @Autowired
    private FaceAbilityClient faceAbilityClient;

    @Value("${ability.face.defaultSearchScore:0.9}")
    private double defaultSearchScore;
    @Value("${ability.face.defaultSearchTopN:1}")
    private int defaultSearchTopN;

    /**
     *  警员登陆
     * @param userDto
     * @return
     */
    public Result<User> login(UserDto userDto){
        if (StringUtils.isEmpty(userDto.getId())){
            return Result.fail("警员信息错误");
        }
        User user = policemanDao.findOne(userDto.getId());
        if (null == user){
            return Result.fail("警员信息查询失败");
        }else {
            return Result.success(user);
        }
    }

    /**
     *  新增警员信息
     * @param userDto
     * @return
     */
    public Result<User> savePoliceman(UserDto userDto, MultipartFile file){

        try {
            User user = null;
            if (StringUtils.isEmpty(userDto.getId())){
                user = new User();
                user.setId(UUIDUtil.uuid());
            }else {
                user = policemanDao.findOne(userDto.getId());
                if (null == user || !userDto.getId().equals(user.getId())){
                    return Result.fail("参数错误");
                }
            }

            //设置图片的扩展属性,将生成用户的UUID作为人脸图片的扩展参数传入人脸库
            FdbImageExtraProp extraProp=new FdbImageExtraProp();
            extraProp.setUserId(userDto.getId());

            //请注意这里的事务需要业方自行处理,当添加图片成功后,添加用户失败需要删除添加的图片,否则会导致人脸库中有重复的图片,这样在搜索出来的人脸图片结果就会不正确
            FdbResultResponse<List<FdbImage>> rst = faceAbilityClient.addFaceImage(Constants.FACE_DB_NAME, file.getBytes(), "0", "0", JSON.toJSONString(extraProp));
            FdbImage fdbImage = null;
            //判断结果是否成功,并且有且只有一个人脸库记录
            if (FdbResponseUtil.isSuccess(rst) && rst.getSuccess() != null && rst.getSuccess().size() > 0) {
                fdbImage = rst.getSuccess().get(0);
            } else {
                throw new FdbRequestException("添加人脸失败," + rst.getErrorMessage());
            }

            user.setName(userDto.getName());
            user.setNameEN(userDto.getNameEN());
            user.setNumber(userDto.getNumber());
            user.setPlace(userDto.getPlace());
            user.setStatus(userDto.getStatus());
            user.setRole(userDto.getRole());
            user.setCreateTime(new Date());
            user.setAge(userDto.getAge());
            user.setDepartment(userDto.getDepartment());
            user.setImageId(fdbImage.getImageId());
            if (StringUtils.isEmpty(userDto.getRole())) {
                user.setRole("user");
            } else {
                user.setRole(userDto.getRole());
            }

            policemanDao.save(user);
            return Result.success(user);
        }catch (Exception e){
            return Result.fail(ErrorCode.FAIL,"注册警员发生错误:" + e.getMessage());
        }
    }

    /**
     * 用人脸图片查找用户,方法1,从人脸库中查找到imageid,在从本地库中查找对应的user信息.
     * @param imageDatas
     * @return
     */
    public Result<User> searchForImage(byte[] imageDatas) {
        FdbResultResponse<List<ImageSearchScore>> rst = faceAbilityClient.searchImage(Constants.FACE_DB_NAME, imageDatas, defaultSearchTopN, defaultSearchScore);
        if (!FdbResponseUtil.isSuccess(rst)) {
            return Result.fail("请求人脸检测接口失败");
        }
        ImageSearchScore searchScore = FdbResponseUtil.getDataFromDataList(rst, ImageSearchScore.class, 0);
        if (searchScore == null) {
            return Result.fail("查无此用户");
        }
        User user = policemanDao.findUserByImageId(searchScore.getImageId());
        return Result.success(user);
    }

    /**
     *  移除警员信息
     * @param userDto
     * @return
     */
    public Result<Boolean> removePoliceman(UserDto userDto){
        try {
            if (StringUtils.isEmpty(userDto.getId())){
                return Result.fail("警员信息错误");
            }
            User user = policemanDao.findOne(userDto.getId());
            if (null == user || !userDto.getId().equals(user.getId())){
                return Result.fail("为查找到指定警员");
            }
            faceAbilityClient.removeImage(Constants.FACE_DB_NAME, user.getImageId());
            policemanDao.delete(user.getId());
            return Result.success(true);
        }catch (Exception e){
            return Result.fail("删除警员错误:" + e.getMessage());
        }
    }

    /**
     *  查询警员信息
     * @param userDto
     * @return
     */
    public Result<List<User>> listPoliceman(UserDto userDto){
        return Result.success(policemanDao.findAll());
    }

    /**
     * 更新警员信息
     * @param userDto
     * @return
     */
    public Result<User> updatePoliceman(UserDto userDto){
        try {

            if (StringUtils.isEmpty(userDto.getId())){
                return Result.fail("警员信息错误");
            }
            User user = policemanDao.findOne(userDto.getId());
            if (null == user || !userDto.getId().equals(user.getId())){
                return Result.fail("未查找到指定警员");
            }

            user.setName(userDto.getName());
            user.setNameEN(userDto.getNameEN());
            user.setNumber(userDto.getNumber());
            user.setPlace(userDto.getPlace());
            user.setStatus(userDto.getStatus());
            user.setRole(userDto.getRole());
            user.setAge(userDto.getAge());
            user.setDepartment(userDto.getDepartment());
            user.setUpdateTime(new Date());

            policemanDao.save(user);
            return Result.success(user);
        }catch (Exception e){
            return Result.fail("更新警员信息错误:" + e.getMessage());
        }
    }

    public Result removeAllImage() {
        FdbResultResponse rst = faceAbilityClient.removeAllImage(Constants.FACE_DB_NAME);
        if (FdbResponseUtil.isSuccess(rst)) {
            return Result.success("true");
        } else {
            return Result.fail("删除失败");
        }
    }
}
