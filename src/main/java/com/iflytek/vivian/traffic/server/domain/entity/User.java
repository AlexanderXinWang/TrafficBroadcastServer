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
     * 名字
     */
    @Column
    private String name;

    /**
     * 英文名
     */
    @Column(name="name_en")
    private String nameEN;

    /**
     * 职位
     */
    @Column
    private String role;

    /**
     * 年龄
     */
    @Column
    private int age;

    /**
     *  所处位置（地点）
     */
    @Column
    private String place;

    /**
     *  所属部门
     */
    @Column
    private String department;

    /**
     * 状态
     */
    @Column
    private String status;

    /**
     * 编号
     */
//    @Column
//    private String number;

    /**
     * imageId,人脸库图片id
     */
//    @Column(name="image_id")
//    private String imageId;

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

}
