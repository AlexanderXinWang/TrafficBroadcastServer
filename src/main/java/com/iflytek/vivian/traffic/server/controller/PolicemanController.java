package com.iflytek.vivian.traffic.server.controller;

import com.iflytek.vivian.traffic.server.domain.entity.User;
import com.iflytek.vivian.traffic.server.domain.service.PolicemanService;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.UserDto;
import com.iflytek.vivian.traffic.server.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName PolicemanController
 * @Author xinwang41
 * @Date 2021/1/4 10:43
 **/

@RestController
@RequestMapping("/user")
@Slf4j
public class PolicemanController {
    @Autowired
    private PolicemanService policemanService;

    /**
     * 警员登陆
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Result<User> userLogin(UserDto userDto){
        try {
            return policemanService.login(userDto);
        } catch (BaseException e){
            return Result.fail(e);
        }
    }

    /**
     * 警员添加
     * @param userDto
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result<User> savePoliceman(UserDto userDto){
        return policemanService.savePoliceman(userDto);
    }

    /**
     * 批量警员移除
     * @param userDtoList
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Result<Boolean> deletePoliceman(List<UserDto> userDtoList){
        return policemanService.deletePoliceman(userDtoList);
    }

    /**
     * 更新警员信息
     * @param userDto
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<User> updatePoliceman(UserDto userDto){
        return policemanService.updatePoliceman(userDto);
    }

    /**
     * 查询所有警员信息
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public Result<List<User>> listPoliceman(){
        return policemanService.listPoliceman();
    }

    /**
     * 查询单个警员详情信息
     * @param userDto
     * @return
     */
    @PostMapping("/select")
    @ResponseBody
    public Result<User> selectPoliceman(UserDto userDto) {
        return policemanService.selectPoliceman(userDto);
    }


}
