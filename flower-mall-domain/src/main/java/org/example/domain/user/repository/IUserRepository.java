package org.example.domain.user.repository;

import org.example.domain.user.model.entity.UserEntity;

import java.util.List;

/**
 * @author lanjiajun
 * @description
 * 定义用户领域的数据持久化操作
 * 领域层只负责定义接口，剩下的交给基础设施层实现
 * @create 2025-12-10 10:29
 */
public interface IUserRepository {

    /**
     * 保存用户
     * 逻辑：有ID就是更新，没ID就是新增
     */
    void save (UserEntity userentity);

    /**
     * 删除用户
     * 对应原 Service 的：delete
     */
    void delete (Integer id);

    /**
     * 根据账号查询详情
     */
    UserEntity findByAccount(String account);

    /**
     * 根据ID查询
     */
    UserEntity findById (Integer id);

    /**
     * 列表搜索
     */
    List<UserEntity> search(String searchKey);
}
