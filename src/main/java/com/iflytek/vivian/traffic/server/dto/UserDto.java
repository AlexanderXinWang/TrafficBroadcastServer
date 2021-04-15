package com.iflytek.vivian.traffic.server.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName UserDto
 * @Author xinwang41
 * @Date 2021/3/2 9:41
 **/

@Data
public class UserDto {
    /**
     * id
     */
    private String id;
    /**
     * 密码
     */
    private String password;
    /**
     * 名字
     */
    private String name;
    /**
     * 英文名
     */
    private String nameEN;
    /**
     * 编号
     */
    private String number;
    /**
     * 职位 角色
     */
    private String role;
    /**
     * 部门
     */
    private String department;
    /**
     * 年龄
     */
    private int age;
    private String imageData;
    private String imageId;

    /**
     * 所处位置（地点）
     */
    private String place;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
