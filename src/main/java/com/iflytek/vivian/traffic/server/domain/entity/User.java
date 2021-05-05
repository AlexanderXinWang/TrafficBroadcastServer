package com.iflytek.vivian.traffic.server.domain.entity;

import lombok.Data;
import lombok.Generated;
import org.aspectj.lang.annotation.DeclareAnnotation;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName User
 * @Author xinwang41
 * @Date 2021/1/4 10:44
 **/
@Data
@Entity
@Table(name = "t_policeman")
public class User {
    /**
     * id
     */
    @Id
    @Column
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 名字
     */
    @Column
    private String name;

    /**
     * 密码
     */
    @Column
    private String password;

    /**
     * 是否为管理员身份
     */
    @Column(name = "is_admin")
    private Integer isAdmin;

    /**
     * 英文名
     */
    @Column(name="name_en")
    private String nameEN;

    /**
     * 年龄
     */
    @Column
    private int age;

    /**
     *  所属部门
     */
    @Column
    private String department;

    /**
     * 职位
     */
    @Column
    private String role;

    /**
     * 创建时间
     */
    @Column(name="create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name="update_time")
    private Date updateTime;

    /**
     * 头像图片地址
     */
    @Column(name = "image_url")
    private String imageUrl;

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *  所处位置（地点）
     */
    @Column
    private String place;

    /**
     * 状态
     */
    @Column
    private String status;
}
