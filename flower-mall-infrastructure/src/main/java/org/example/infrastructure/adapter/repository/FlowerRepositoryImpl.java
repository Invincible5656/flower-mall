package org.example.infrastructure.adapter.repository;

import org.example.domain.flower.adapter.repository.IFlowerRepository;
import org.example.domain.flower.model.entity.FlowerEntity;
import org.example.infrastructure.dao.IFlowerDao;
import org.example.infrastructure.dao.po.Flower;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-09 11:39
 */
@Repository
public class FlowerRepositoryImpl implements IFlowerRepository {

    @Resource
    private IFlowerDao flowerDao;

    // ================== 核心接口实现 ==================
    @Override
    public void save(FlowerEntity flower) {

        //1.先将领域对象转化为数据库对象
        Flower po = convertToFlowerPO(flower);

        //2.判断是新增还是更新（domain层里面就不用判断了）
        //根据ID是否为空判断
        if (po.getId() == null) {
            flowerDao.insert(po);

            // 数据库插入后会生成自增 ID，必须把这个 ID 回填给领域对象
            // 否则业务层不知道刚插入的数据 ID 是多少
            flower.setId(po.getId());
        } else {
            //更新逻辑
            flowerDao.update(po);
        }
    }

    @Override
    public void delete(FlowerEntity flower) {
        if (flower != null && flower.getId() != null) {
            flowerDao.delete(flower.getId());
        }
    }

    @Override
    public FlowerEntity findById(Integer id) {
        //1.从数据库里面查出PO
        Flower po=flowerDao.selectById(id);

        //2.将PO转换为实体类返回给业务层
        if (po == null) {
            return null;
        }
        return convertToFlowerEntity(po);
    }

    @Override
    public List<FlowerEntity> queryList(String searchKey, String searchType) {
        // 1. 从 DAO 查出 PO 列表
        List<Flower> poList=flowerDao.find(searchKey, searchType);
        if(poList==null|| poList.isEmpty()){
            return Collections.emptyList();
        }

        // 2. 使用 Stream 流将 List<PO> 转换成 List<Entity>
        return poList.stream()
                .map(this::convertToFlowerEntity) // 调用下面的转换方法
                .collect(Collectors.toList());
    }

    /**
     * 将 领域对象 转为 数据库对象
     */
    private Flower convertToFlowerPO(FlowerEntity entity) {
        if (entity == null) {
            return null;
        }
        Flower po = new Flower();
        po.setId(entity.getId()); // 注意类型匹配
        po.setName(entity.getName());
        po.setPrice(entity.getPrice());
        po.setSpeciesName(entity.getSpeciesName());
        po.setDetail(entity.getDetail());
        po.setImgGuid(entity.getImgGuid());
        po.setState(entity.getState());
        return po;
    }

    /**
     * 将 数据库对象 转为 领域对象
     */
    private FlowerEntity convertToFlowerEntity(Flower po) {
        if (po == null) {
            return null;
        }
        FlowerEntity entity = new FlowerEntity();
        entity.setId(po.getId());
        entity.setName(po.getName());
        entity.setPrice(po.getPrice());
        entity.setSpeciesName(po.getSpeciesName());
        entity.setDetail(po.getDetail());
        entity.setImgGuid(po.getImgGuid());
        return entity;
    }
}