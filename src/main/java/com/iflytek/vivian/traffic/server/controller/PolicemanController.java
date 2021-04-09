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
    public Result<User> login(UserDto userDto){
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
    public Result<User> savePoliceman(UserDto userDto,
                                      @RequestPart("file")MultipartFile file){
        return policemanService.savePoliceman(userDto, file);
    }

    /**
     * 警员移除
     * @param userDto
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public Result<Boolean> removePoliceman(UserDto userDto){
        return policemanService.removePoliceman(userDto);
    }

    /**
     * 查询警员信息
     * @param userDto
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public Result<List<User>> listPoliceman(UserDto userDto){
        return policemanService.listPoliceman(userDto);
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

    /*@PostMapping("/removeAllImage")
    @ResponseBody
    public Result removeAllImage(){
        return policemanService.removeAllImage();
    }*/
}
