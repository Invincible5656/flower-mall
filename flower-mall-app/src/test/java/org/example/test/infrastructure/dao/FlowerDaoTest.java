package org.example.test.infrastructure.dao;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.dao.IFlowerDao;
import org.example.infrastructure.dao.po.Flower;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-09 10:23
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowerDaoTest {

    @Resource
    private IFlowerDao flowerDao;

    @Test
    public void test_queryFlowerList() {
        String searchKey = "";

        List<Flower> flowerList = flowerDao.findAll(searchKey);

        log.info("查询结果数量: {}", flowerList.size());
        log.info("查询结果详情: {}", JSON.toJSONString(flowerList));
    }
}
