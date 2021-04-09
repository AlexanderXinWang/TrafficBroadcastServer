package com.iflytek.vivian.traffic.server.domain.dao;

import com.iflytek.vivian.traffic.server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @ClassName IPolicemanDao
 * @Author xinwang41
 * @Date 2021/1/4 10:44
 **/

public interface IPolicemanDao extends JpaRepository<User, String> {
    /**
     * 根据ImageId查找Policeman
     * @param imageId
     * @return
     */
    User findUserByImageId(String imageId);
}
