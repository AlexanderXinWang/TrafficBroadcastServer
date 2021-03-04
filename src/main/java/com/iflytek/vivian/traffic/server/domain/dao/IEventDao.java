package com.iflytek.vivian.traffic.server.domain.dao;

import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.service.EventService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName IEventDao
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/1/4 10:44
 * @Version 1.0
 **/

@Component
public interface IEventDao extends JpaRepository<Event, String> {
    /**
     * 根据 警员id和事件状态查找事件
     * @param policemanId
     * @param status
     * @return
     */
    Event findByPolicemanIdAndStatus(String policemanId , String status);

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
