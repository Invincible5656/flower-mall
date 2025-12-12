package org.example.domain.user.service;

import org.example.domain.user.model.entity.UserEntity;
import org.example.domain.user.model.entity.UserLoginEntity;
import org.example.domain.user.repository.IUserRepository;
import org.example.types.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lanjiajun
 * @description
 * 用户领域服务
 * 注册校验，分页处理，信息修改
 * @create 2025-12-10 20:25
 */
@Service
public class UserApplicationService {
    @Resource
    private IUserRepository userRepository;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 登录业务
     */
    public UserLoginEntity login(String account, String password, String role) {
        // 1. 通过仓储查询用户
        UserEntity user = userRepository.findByAccount(account);

        // 2. 校验是否存在
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 3. 校验角色
        if (!role.equals(user.getRole())) {
            throw new RuntimeException("角色不匹配");
        }

        // 4. 校验密码 (核心业务逻辑在实体中)
        if (!user.checkPassword(password)) {
            throw new RuntimeException("密码错误");
        }

        // 5. 生成 JWT Token
        String token = jwtUtil.createToken(
                user.getId().toString(),
                user.getAccount(), // 这里的 Account 就是用户名
                user.getRole()
        );

        // 6. 构建返回结果
        user.setPassword(null);

        return new UserLoginEntity(token, user);
    }

    /**
     * 注册业务
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(UserEntity userEntity) {
        // 1. 防重校验
        UserEntity exist = userRepository.findByAccount(userEntity.getAccount());
        if (exist != null) {
            throw new RuntimeException("账号已存在");
        }

        // 2. 初始化数据
        userEntity.initAsNormalUser();

        // 3. 保存
        userRepository.save(userEntity);
    }

    /**
     * 更新业务
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(UserEntity userEntity) {
        // 1. 查旧数据
        UserEntity oldUser = userRepository.findById(userEntity.getId());
        if (oldUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 更新数据 (调用实体的行为)
        oldUser.updateInfo(
                userEntity.getName(),
                userEntity.getPassword(),
                userEntity.getPhone(),
                userEntity.getAddress()
        );

        // 3. 保存 (Repo 实现里会判断 ID 存在即 update)
        userRepository.save(oldUser);
    }

    /**
     * 删除业务
     */
    public void delete(Integer id) {
        userRepository.delete(id);
    }

    /**
     * 查询个人信息
     */
    public UserEntity queryInfo(String account) {
        return userRepository.findByAccount(account);
    }

    /**
     * 分页查询 (原 Controller 中的逻辑下沉到这里)
     */
    public Map<String, Object> findByPage(int page, String searchKey) {
        int pageSize = 10; // 固定页大小

        // 1. 查所有符合条件的数据
        List<UserEntity> allUsers = userRepository.search(searchKey);

        // 2. 内存分页计算
        int totalCount = allUsers.size();
        int fromIndex = (page - 1) * pageSize;

        if (fromIndex >= totalCount) {
            Map<String, Object> emptyMap = new HashMap<>();
            emptyMap.put("items", Collections.emptyList());
            emptyMap.put("len", (totalCount + pageSize - 1) / pageSize);
            return emptyMap;
        }

        int toIndex = Math.min(fromIndex + pageSize, totalCount);
        List<UserEntity> pageItems = allUsers.subList(fromIndex, toIndex);

        // 3. 计算总页数
        int totalPages = (totalCount + pageSize - 1) / pageSize;

        Map<String, Object> result = new HashMap<>();
        result.put("items", pageItems);
        result.put("len", totalPages);

        return result;
    }
}
