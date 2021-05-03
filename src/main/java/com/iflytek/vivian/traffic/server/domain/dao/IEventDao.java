package com.iflytek.vivian.traffic.server.domain.dao;

import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.service.EventService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName IEventDao
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 10:44
 * @Version 1.0
 **/

public interface IEventDao extends JpaRepository<Event, String> {

    @Query(value = "SELECT * FROM `t_event` ORDER BY CONVERT(`event` USING gbk) COLLATE gbk_chinese_ci ASC", nativeQuery = true)
    List<Event> orderByEventAsc();

    @Query(value = "SELECT * FROM `t_event` ORDER BY CONVERT(`event` USING gbk) COLLATE gbk_chinese_ci DESC", nativeQuery = true)
    List<Event> orderByEventDesc();

    @Query(value = "SELECT * FROM `t_event` ORDER BY CONVERT(`location` USING gbk) COLLATE gbk_chinese_ci ASC", nativeQuery = true)
    List<Event> orderByLocationAsc();

    @Query(value = "SELECT * FROM `t_event` ORDER BY CONVERT(`location` USING gbk) COLLATE gbk_chinese_ci DESC", nativeQuery = true)
    List<Event> orderByLocationDesc();

    @Query(value = "SELECT * FROM `t_event` ORDER BY CONVERT(`policeman_name` USING gbk) COLLATE gbk_chinese_ci ASC", nativeQuery = true)
    List<Event> orderByNameAsc();

    @Query(value = "SELECT * FROM `t_event` ORDER BY CONVERT(`policeman_name` USING gbk) COLLATE gbk_chinese_ci DESC", nativeQuery = true)
    List<Event> orderByNameDesc();

    /**
     * 根据警员id查找事件
     * * @param policemanId
     * @return
     */
    List<Event> findEventsByPolicemanId(String policemanId);

    List<Event> findEventsByEventLike(String s);
    List<Event> findEventsByLocationLike(String s);
    List<Event> findEventsByPolicemanNameLike(String s);

    /**
     * 根据警员id和事件状态查找事件
     * @param policemanId
     * @param status
     * @return
     */
//    Event findByPolicemanIdAndStatus(String policemanId , String status);

    /**
     * 查询未处理的上报事件 且未播报的事件
     * @param status
     * @return
     */
    List<Event> findByStatusAndIsPlay(String status, String isPlay);

    /**
     * 查询未处理的上报事件
     * @param status
     * @return
     */
    List<Event> findByStatus(String status);
}
