package com.iflytek.vivian.traffic.server.controller;

import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.entity.User;
import com.iflytek.vivian.traffic.server.domain.service.PolicemanService;
import com.iflytek.vivian.traffic.server.dto.Result;
import com.iflytek.vivian.traffic.server.dto.UserDto;
import com.iflytek.vivian.traffic.server.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
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
    public Result<User> userLogin(@RequestBody UserDto userDto){
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
    public Result<User> savePoliceman(@RequestBody UserDto userDto){
        return policemanService.savePoliceman(userDto);
    }

    /**
     * 批量警员移除
     * @param userIds
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Result<Boolean> deletePoliceman(@RequestBody List<String> userIds){
        return policemanService.deletePoliceman(userIds);
    }

    /**
     * 更新警员信息
     * @param userDto
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<User> updatePoliceman(@RequestBody UserDto userDto){
        return policemanService.updatePoliceman(userDto);
    }

    /**
     * 查询所有警员信息
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<List<User>> listPoliceman(){
        return policemanService.listPoliceman();
    }

    /**
     * 查询单个警员详情信息
     * @param userId
     * @return
     */
    @PostMapping("/detail/{userId}")
    @ResponseBody
    public Result<User> selectPoliceman(@PathVariable String userId) {
        return policemanService.selectPoliceman(userId);
    }

    @PostMapping("/{userId}/image/upload")
    @ResponseBody
    public Result<String> uploadImage(@RequestPart("image") MultipartFile image, @PathVariable String userId) {
        return policemanService.uploadImage(image, userId);
    }

    @PostMapping("/{userId}/image")
    @ResponseBody
    public Result<String> getImageUrl(@PathVariable String userId) {
        return policemanService.getImageUrl(userId);
    }

    @PostMapping("/image/update")
    @ResponseBody
    public Result<Boolean> updateUserImage(@RequestBody UserDto userDto) {
        return policemanService.changeUserImage(userDto);
    }

    @PostMapping("/password/check")
    @ResponseBody
    public Result<Boolean> checkOldPassword(@RequestBody UserDto userDto) {
        return policemanService.checkOldPassword(userDto);
    }

    @PostMapping("/password/update")
    @ResponseBody
    public Result<Boolean> updateUserPassword(@RequestBody UserDto userDto) {
        return policemanService.updateUserPassword(userDto);
    }

    @GetMapping("/list/id/asc")
    @ResponseBody
    public Result<List<User>> listUserByIdAsc(){
        return policemanService.listPolicemanByIdAsc();
    }

    @GetMapping("/list/id/desc")
    @ResponseBody
    public Result<List<User>> listUserByIdDesc(){
        return policemanService.listPolicemanByIdDesc();
    }

    @GetMapping("/list/name/asc")
    @ResponseBody
    public Result<List<User>> listUserByNameAsc(){
        return policemanService.listPolicemanByNameAsc();
    }

    @GetMapping("/list/name/desc")
    @ResponseBody
    public Result<List<User>> listUserByNameDesc(){
        return policemanService.listPolicemanByNameDesc();
    }

    @GetMapping("/list/age/asc")
    @ResponseBody
    public Result<List<User>> listEventByAgeAsc(){
        return policemanService.listPolicemanByAgeAsc();
    }

    @GetMapping("/list/age/desc")
    @ResponseBody
    public Result<List<User>> listEventByAgeDesc(){
        return policemanService.listPolicemanByAgeDesc();
    }

    @GetMapping("/list/department/asc")
    @ResponseBody
    public Result<List<User>> listEventByDepartmentAsc(){
        return policemanService.listPolicemanByDepartmentAsc();
    }

    @GetMapping("/list/department/desc")
    @ResponseBody
    public Result<List<User>> listEventByDepartmentDesc(){
        return policemanService.listPolicemanByDepartmentDesc();
    }

    @GetMapping("/list/time/asc")
    @ResponseBody
    public Result<List<User>> listEventByTimeAsc(){
        return policemanService.listPolicemanByTimeAsc();
    }

    @GetMapping("/list/time/desc")
    @ResponseBody
    public Result<List<User>> listEventByTimeDesc(){
        return policemanService.listPolicemanByTimeDesc();
    }




}
