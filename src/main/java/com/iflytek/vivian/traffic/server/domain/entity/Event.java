package com.iflytek.vivian.traffic.server.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    /**
     * 事件类型
     */
    @Column
    private String type;

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
     *  事件影响
     */
    @Column(name="event_result")
    private String eventResult;

    @Column(name="iat_result")
    private String iatResult;
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
     * 状态
     */
    @Column
    private String status;

    /**
     * 是否播报
     */
    @Column(name="is_play")
    private String isPlay;

    /**
     * 时间描述
     */
    @Column(name = "event_desc")
    private String desc;

    @Column(name = "pcm")
    private byte[] pcm;

}
