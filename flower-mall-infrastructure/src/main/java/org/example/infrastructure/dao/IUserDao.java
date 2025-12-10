package org.example.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.dao.po.User;

import java.util.List;

/**
 * @author lanjiajun
 * @description
 * 用户数据访问接口
 * @create 2025-12-10 10:44
 */
@Mapper
public interface IUserDao {
    // 对应原 find
    List<User> find(@Param("searchKey") String searchKey);

    // 对应原 queryById (ID改为Long)
    User queryById(Integer id);

    // 对应原 queryInfo
    User queryInfo(@Param("account") String account);

    // 对应原 update (ID改为Long)
    int update(User user);

    // 对应原 delete
    int delete(Integer id);

    // 对应原 add
    int add(User user);



}
