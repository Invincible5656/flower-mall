package org.example.test.infrastructure.dao;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.dao.ICartDao;
import org.example.infrastructure.dao.po.Cart;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 13:19
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartDaoTest {

    @Resource
    private ICartDao cartDao;

    @Test
    public void test_cart_operations() {
        // 1. 准备测试数据 (模拟用户14买了一朵ID为7的花)
        Integer userId = 14;
        Cart cartPO = new Cart();
        cartPO.setUid(userId);
        cartPO.setFid(7);
        cartPO.setFlower("测试鲜花");
        cartPO.setPrice(99.0f);
        cartPO.setAmount(1);

        // 2. 测试插入
        int insertCount = cartDao.insert(cartPO);
        log.info("插入结果行数: {}", insertCount);
        log.info("回填的主键ID: {}", cartPO.getId());

        Assert.assertEquals(1, insertCount);
        Assert.assertNotNull(cartPO.getId()); // 确保 XML 里的 useGeneratedKeys 生效了

        // 3. 测试查询 (根据用户ID)
        List<Cart> list = cartDao.selectByUserId(userId);
        log.info("用户购物车列表: {}", JSON.toJSONString(list));
        Assert.assertFalse(list.isEmpty());

        // 4. 测试更新 (数量变 5)
        cartPO.setAmount(5);
        int updateCount = cartDao.updateAmount(cartPO);
        log.info("更新结果行数: {}", updateCount);
        Assert.assertEquals(1, updateCount);

        // 5. 再次查询验证更新是否成功
        List<Cart> updatedList = cartDao.selectByUserId(userId);
        log.info("更新后的列表: {}", JSON.toJSONString(updatedList));

        // 6. 测试删除
        int deleteCount = cartDao.deleteById(cartPO.getId());
        log.info("删除结果行数: {}", deleteCount);
        Assert.assertEquals(1, deleteCount);
    }


}
