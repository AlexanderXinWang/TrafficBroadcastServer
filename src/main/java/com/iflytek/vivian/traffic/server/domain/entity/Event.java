package com.iflytek.vivian.traffic.server.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName Event
 * @Author xinwang41
 * @Date 2021/1/4 10:44
 **/
@Data
@Entity
@Table(name = "t_event")
public class Event {
    /**
     * id
     */
    @Id
    @Column
    private String id;

    @Column(name = "policeman_id")
    private String policemanId;

    @Column(name = "policeman_name")
    private String policemanName;

    @Column(name = "policeman_image")
    private String policemanImage;

    /**
     * 事件发生地
     */
    @Column
    private String location;

    /**
     *  车子类型
     */
    @Column
    private String vehicle;

    /**
     * 事件
     */
    @Column
    private String event;

    /**
     *  事件结果
     */
    @Column(name="event_result")
    private String eventResult;

    @Column(name="iat_result")
    private String iatResult;

    private String mp3;

    /**
     * 事件发生时间
     */
    @Column(name="start_time")
    private Date startTime;

    /**
     * 事件结束时间
     */
    @Column(name="end_time")
    private Date endTime;

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
     * 是否播报
     */
    @Column(name="is_play")
    private String isPlay;


    ////////////////////////////////////////////////////////////////////////////////////////////
    //待用字段

    /**
     * 状态
     */
    @Column
    private String status;



    /**
     * 事件备注
     */
    @Column(name = "event_desc")
    private String desc;

    /**
     * 事件类型
     */
    @Column
    private String type;

}
