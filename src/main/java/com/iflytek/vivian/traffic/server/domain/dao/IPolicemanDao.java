package com.iflytek.vivian.traffic.server.domain.dao;

import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName IPolicemanDao
 * @Author xinwang41
 * @Date 2021/1/4 10:44
 **/

public interface IPolicemanDao extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM `t_policeman` ORDER BY CONVERT(`name` USING gbk) COLLATE gbk_chinese_ci ASC", nativeQuery = true)
    List<User> orderByNameAsc();

    @Query(value = "SELECT * FROM `t_policeman` ORDER BY CONVERT(`name` USING gbk) COLLATE gbk_chinese_ci DESC", nativeQuery = true)
    List<User> orderByNameDesc();

    @Query(value = "SELECT * FROM `t_policeman` ORDER BY CONVERT(`department` USING gbk) COLLATE gbk_chinese_ci ASC", nativeQuery = true)
    List<User> orderByDepartmentAsc();

    @Query(value = "SELECT * FROM `t_policeman` ORDER BY CONVERT(`department` USING gbk) COLLATE gbk_chinese_ci DESC", nativeQuery = true)
    List<User> orderByDepartmentDesc();

    /**
     * 根据ImageId查找Policeman
     * @param imageId
     * @return
     */
//    User findUserByImageId(String imageId);
}
