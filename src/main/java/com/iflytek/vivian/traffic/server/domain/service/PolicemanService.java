package com.iflytek.vivian.traffic.server.domain.service;

import com.iflytek.vivian.traffic.server.constants.ErrorCode;
import com.iflytek.vivian.traffic.server.domain.dao.IPolicemanDao;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.entity.User;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.UserDto;
import com.iflytek.vivian.traffic.server.utils.UUIDUtil;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName PolicemanService
 * @Author xinwang41
 * @Date 2021/1/4 10:45
 **/
@Slf4j
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
            if (!StringUtils.isEmpty(userDto.getImageUrl())) {
                user.setImageUrl(userDto.getImageUrl());
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
            if (!StringUtils.isEmpty(userDto.getPassword())) {
                user.setPassword(userDto.getPassword());
            }
            if (!StringUtils.isEmpty(userDto.getImageUrl())) {
                user.setImageUrl(userDto.getImageUrl());
            }
            if (!StringUtils.isEmpty(userDto.getImageUrl())) {
                user.setName(userDto.getName());
            }
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


    public Result<List<User>> listPolicemanByNameAsc() {
        return Result.success(policemanDao.orderByNameAsc());
    }

    public Result<List<User>> listPolicemanByNameDesc() {
        return Result.success(policemanDao.orderByNameDesc());
    }

    public Result<List<User>> listPolicemanByDepartmentAsc() {
        return Result.success(policemanDao.orderByDepartmentAsc());
    }

    public Result<List<User>> listPolicemanByDepartmentDesc() {
        return Result.success(policemanDao.orderByDepartmentDesc());
    }

    public Result<List<User>> listPolicemanByIdAsc() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return Result.success(policemanDao.findAll(sort));
    }

    public Result<List<User>> listPolicemanByIdDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return Result.success(policemanDao.findAll(sort));
    }

    public Result<List<User>> listPolicemanByAgeAsc() {
        Sort sort = new Sort(Sort.Direction.ASC, "age");
        return Result.success(policemanDao.findAll(sort));
    }

    public Result<List<User>> listPolicemanByAgeDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "age");
        return Result.success(policemanDao.findAll(sort));
    }

    public Result<List<User>> listPolicemanByTimeAsc() {
        Sort sort = new Sort(Sort.Direction.DESC, "create_time");
        return Result.success(policemanDao.findAll(sort));
    }

    public Result<List<User>> listPolicemanByTimeDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "create_time");
        return Result.success(policemanDao.findAll(sort));
    }

    public Result<String> uploadImage(MultipartFile image, String userId) {
        try {
//            File file = new File("C:\\Users\\AlexanderWang\\Pictures\\" + userId + ".jpg");
            File file = new File("/project/image/" + userId + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            image.transferTo(file);
        } catch (FileNotFoundException e) {
            log.error("未找到指定文件：" + e.getMessage());
            return Result.fail("未找到指定文件：" + e.getMessage());
        } catch (IOException e) {
            log.error("输入输出流出错：" + e.getMessage());
            return Result.fail("输入输出流出错：" + e.getMessage());
        }
        return Result.success("http://1.15.78.72:8080/images/" + userId + ".jpg");
    }

    public Result<String> getImageUrl(String userId) {
        User user = policemanDao.findOne(userId);
        if (user != null) {
            return Result.success(user.getImageUrl());
        } else {
            log.error("未查找到指定用户");
            return Result.fail("未查找到指定用户");
        }
    }
}
