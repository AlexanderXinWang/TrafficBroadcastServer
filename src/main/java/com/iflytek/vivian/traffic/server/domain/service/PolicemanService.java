package com.iflytek.vivian.traffic.server.domain.service;

import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.domain.dao.IPolicemanDao;
import com.iflytek.vivian.traffic.server.domain.entity.User;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.UserDto;
import com.iflytek.vivian.traffic.server.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        if (user.getPassword().equals(userDto.getPassword())) {
            return Result.success(user);
        }else {
            return Result.fail("警员信息查询失败");
        }
    }

    /**
     *  新增警员信息
     * @param userDto
     * @return
     */
    public Result<User> savePoliceman(UserDto userDto){

        try {
            User user = null;

            if (StringUtils.isEmpty(userDto.getId())) {
                return Result.fail("用户必填id为空");
            } else {
                user = policemanDao.findOne(userDto.getId());

                if (user != null) {
                    return Result.fail("此用户已经存在");
                } else {
                    user = new User();
                    user.setId(userDto.getId());
                }
            }

            user.setName(userDto.getName());
            user.setNameEN(userDto.getNameEN());
//            user.setNumber(userDto.getNumber());
            user.setPassword(userDto.getPassword());
//            user.setPlace(userDto.getPlace());
//            user.setStatus(userDto.getStatus());
            user.setRole(userDto.getRole());
            user.setCreateTime(new Date());
            user.setAge(userDto.getAge());
            user.setDepartment(userDto.getDepartment());
//            user.setImageId(fdbImage.getImageId());
            user.setRole(userDto.getRole());
            if (StringUtils.isEmpty(userDto.getIsAdmin())) {
                user.setIsAdmin(0);
            } else {
                user.setIsAdmin(userDto.getIsAdmin());
            }
//            policemanDao.save(user);
//            policemanDao.flush();
            policemanDao.saveAndFlush(user);
            return Result.success(user);
        }catch (Exception e){
            return Result.fail(ErrorCode.FAIL,"注册警员发生错误:" + e.getMessage());
        }
    }

    /**
     *  批量移除警员信息
     * @param userIds
     * @return
     */
    public Result<Boolean> deletePoliceman(List<String> userIds){
        try {
            List<User> userList = new ArrayList<>();
            for (String userId : userIds) {
                if (StringUtils.isEmpty(userId)){
                    return Result.fail("警员信息错误");
                }
                User user = policemanDao.findOne(userId);
                if (null == user || !userId.equals(user.getId())){
                    return Result.fail("未查找到指定警员");
                }
                userList.add(user);
            }
            policemanDao.deleteInBatch(userList);
            return Result.success(true);
        }catch (Exception e){
            return Result.fail("删除警员错误:" + e.getMessage());
        }
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



            user.setPassword(userDto.getPassword());
            user.setName(userDto.getName());

            if (StringUtils.isEmpty(userDto.getIsAdmin())) {
                user.setIsAdmin(0);
            } else {
                user.setIsAdmin(userDto.getIsAdmin());
            }

            user.setNameEN(userDto.getNameEN());
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

    /**
     *  查询所有警员信息
     * @return
     */
    public Result<List<User>> listPoliceman(){
        Sort sort = new Sort(Sort.Direction.ASC, "name");
        return Result.success(policemanDao.findAll());
    }

    /**
     * 查询单个警员详情信息
     * @param userId
     * @return
     */
    public Result<User> selectPoliceman(String userId) {
        return Result.success(policemanDao.findOne(userId));
    }



}
