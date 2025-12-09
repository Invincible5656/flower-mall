package org.example.domain.flower.service;

import org.example.domain.flower.adapter.repository.IFlowerRepository;
import org.example.domain.flower.model.entity.FlowerEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * app层只负责指挥流程
 * 负责把散落在各处的资源“串”起来，完成一个完整的用户请求。
 * 加上 @Transactional 注解，
 * 它保证在这个方法里的所有操作，要么一起提交，要么一起回滚。
 *
 * @create 2025-12-09 17:30
 */
@Service
public class FlowerApplicationService {

    @Resource
    private IFlowerRepository flowerRepository;

    /**
     * 场景：上架新鲜花
     * 对应原接口：add
     */
    @Transactional(rollbackFor = Exception.class)
    public void addFlower(String name, Float price,
                          String speciesName, String detail){
        // 1. 组装领域实体 (Entity)
        FlowerEntity flower = new FlowerEntity();
        flower.setName(name);
        flower.setPrice(price);
        flower.setSpeciesName(speciesName);
        flower.setDetail(detail);
        flower.setState(1);

        //2.调用仓储保存
        flowerRepository.save(flower);
    }

    /**
     * 场景：修改鲜花信息
     * 对应原接口：update
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateFlower(Integer id, String name, Float price, String speciesName) {
        // 1. 先查
        FlowerEntity flower = flowerRepository.findById(id);
        if (flower == null) {
            throw new RuntimeException("鲜花不存在");
        }

        // 2. 在内存中修改业务对象
        // 如果你的 FlowerEntity 里有 changeInfo() 方法，就调那个
        flower.setName(name);
        flower.setPrice(price);
        flower.setSpeciesName(speciesName);

        // 3. 落库
        flowerRepository.save(flower);
    }

    /**
     * 场景：修改鲜花图片
     * 对应原接口：updateImg
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateFlowerImage(Long id, String imgGuid) {
        // 1. 先查
        FlowerEntity flower = flowerRepository.findById(id.intValue());
        if (flower == null) {
            throw new RuntimeException("鲜花不存在");
        }

        // 2. 修改
        flower.setImgGuid(imgGuid);

        // 3. 落库
        flowerRepository.save(flower);
    }

    /**
     * 场景：查询列表
     */
    public List<FlowerEntity> searchFlowers(String key, String type) {
        // 直接调用仓储查询
        return flowerRepository.queryList(key, type);
    }

    /**
     * 场景：删除
     */
    public void deleteFlower(Integer id) {
        // 构造一个只有ID的实体去删除
        FlowerEntity entity = new FlowerEntity();
        entity.setId(id); // 注意类型匹配
        flowerRepository.delete(entity);
    }
}
