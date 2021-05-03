package com.iflytek.vivian.traffic.server.controller;

import com.iflytek.vivian.traffic.server.domain.dao.IEventDao;
import com.iflytek.vivian.traffic.server.domain.dao.IPolicemanDao;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.IfPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Autowired
    IEventDao eventDao;
    @Autowired
    IPolicemanDao policemanDao;

    @PostMapping("/{s}")
    @ResponseBody
    public Result<String> search(@PathVariable String s) {
        List<Event> eventList = eventDao.findEventsByEventLike(s);

        return null;
    }
}
