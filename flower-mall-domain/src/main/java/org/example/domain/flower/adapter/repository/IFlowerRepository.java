package org.example.domain.flower.adapter.repository;

import org.example.domain.flower.model.entity.FlowerEntity;

import java.util.List;

/**
 * @author lanjiajun
 * @description
 * 定义花领域的数据持久化操作
 * 领域层只负责定义接口，剩下的交给基础设施层实现
 * 定义对“鲜花聚合根”的存取标准，不关心底层数据库是 MySQL 还是 Redis。
 * @create 2025-12-09 11:15
 */
public interface IFlowerRepository {
    /**
     * 保存鲜花
     * 对应add(新增) 和 update(修改) 以及 updateImg(修改图片)
     * DDD 原则：无论是新建还是修改，都是把一个“完整的对象”放回仓库。
     * 实现层会自动判断是 insert 还是 update。
     */
    void save (FlowerEntity flower);

    /**
     * 删除鲜花
     * 对应原接口的：delete
     */
    void delete (FlowerEntity flower);

    /**
     * 根据ID查找
     * 作用：修改数据前，必须先把它查出来，修改完属性后，再调 save()
     */
    FlowerEntity findById (Integer id);

    /**
     * 列表查询
     * 对应原接口的：find 和 findAll
     */
    List<FlowerEntity> queryList(String searchKey, String searchType);







}
