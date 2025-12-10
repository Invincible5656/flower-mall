package org.example.test.infrastructure.dao;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.dao.IUserDao;
import org.example.infrastructure.dao.po.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-10 12:19
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Resource
    private IUserDao userMapper;

    /**
     * 测试新增用户
     * 验证 add 方法和 useGeneratedKeys 回填ID是否生效
     */
    @Test
    public void test_add() {
        System.out.println("UserMapper is: " + userMapper); // 加上这行

        User user = new User();
        user.setAccount("testuser001");
        user.setName("测试用户"); // 假设 PO 里的字段叫 username
        user.setPassword("123456"); // 真实项目应加密
        user.setPhone("18812345678");
        user.setAddress("测试地址");

        int result = userMapper.add(user);

        log.info("插入影响行数: {}", result);
        log.info("插入后回填的ID: {}", user.getId()); // 如果打印出ID，说明XML配置正确
    }

    /**
     * 测试列表查询 (对应你之前的测试)
     * 验证 find 方法
     */
    @Test
    public void test_find() {
        // 1. 先插入一条数据，保证能查到
        test_add();

        // 2. 按关键字查询
        String searchKey = "测试"; // 模糊搜索 "测试用户"

        List<User> userList = userMapper.find(searchKey);

        log.info("查询结果数量: {}", userList.size());
        log.info("查询结果详情: {}", JSON.toJSONString(userList));
    }

    /**
     * 测试按账号查询
     * 验证 queryInfo 方法
     */
    @Test
    public void test_queryInfo() {
        String account = "testuser001"; // 假设数据库有这个用户
        User user = userMapper.queryInfo(account);
        log.info("按账号查询结果: {}", JSON.toJSONString(user));
    }

}
