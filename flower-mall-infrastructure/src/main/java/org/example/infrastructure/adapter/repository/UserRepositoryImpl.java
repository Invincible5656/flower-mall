package org.example.infrastructure.adapter.repository;

import org.example.domain.user.model.entity.UserEntity;
import org.example.domain.user.repository.IUserRepository;
import org.example.infrastructure.dao.IUserDao;
import org.example.infrastructure.dao.po.User;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lanjiajun
 * @description
 * 用户数据访问接口
 * @create 2025-12-10 10:47
 */
@Repository
public class UserRepositoryImpl implements IUserRepository {

    @Resource
    private IUserDao userDao;

    @Override
    public void save(UserEntity userEntity) {
        // 1. Domain -> PO
        User userPO = convertToUserPO(userEntity);

        // 2. 判断新增还是更新
        if (userPO.getId() == null) {
            // 对应原 add
            try {
                userDao.add(userPO);
                // 回填ID
                userEntity.setId(userPO.getId());
            } catch (Exception e) {
                throw new RuntimeException("添加用户失败", e);
            }
        } else {
            // 对应原 update
            userDao.update(userPO);
        }
    }

    @Override
    public void delete(Integer id) {
        // 对应原 delete
        userDao.delete(id);
    }

    @Override
    public UserEntity findByAccount(String account) {
        User userPO = userDao.queryInfo(account);
        return convertToUserEntity(userPO);
    }

    @Override
    public UserEntity findById(Integer id) {
        if (id == null) {
            return null;
        }
        // 1. 调用 Mapper 从数据库查出 PO 对象
        User userPO = userDao.queryById(id);

        // 2. 将 PO 对象转换为 Domain Entity 对象
        return convertToUserEntity(userPO);
    }

    @Override
    public List<UserEntity> search(String searchKey) {
        List<User> poList = userDao.find(searchKey);

        if (poList == null) return Collections.emptyList();

        return poList.stream()
                .map(this::convertToUserEntity)
                .collect(Collectors.toList());
    }

    /**
     * 领域对象(UserEntity) -> 数据库对象(User)
     */
    private User convertToUserPO(UserEntity userEntity) {
        if (userEntity == null) {
            return null;}

        User po = new User();
        // 属性拷贝
        po.setId(userEntity.getId());
        po.setAccount(userEntity.getAccount());
        po.setName(userEntity.getName());
        po.setPassword(userEntity.getPassword());
        po.setPhone(userEntity.getPhone());
        po.setAddress(userEntity.getAddress());
        po.setRole(userEntity.getRole());
        return po;
    }

    /**
     * 数据库对象(User) -> 领域对象(UserEntity)
     */
    private UserEntity convertToUserEntity(User po) {
        if (po == null) return null;
        UserEntity entity = new UserEntity();
        // 属性拷贝
        entity.setId(po.getId());
        entity.setName(po.getName());
        entity.setAccount(po.getAccount());
        entity.setPassword(po.getPassword());
        entity.setPhone(po.getPhone());
        entity.setAddress(po.getAddress());
        entity.setRole(po.getRole());
        return entity;
    }
}
