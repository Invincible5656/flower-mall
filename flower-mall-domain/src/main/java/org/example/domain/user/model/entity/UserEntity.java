package org.example.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description 用户领域实体
 * @create 2025-12-10 10:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private Integer id;

    private String account;

    private String name;

    private String password;

    private String phone;

    private String address;

    private String role;

    // 业务行为：校验密码
    public boolean checkPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }

    // 业务行为：初始化普通用户
    public void initAsNormalUser() {
        this.role = "user";
    }

    // 业务行为：更新信息
    public void updateInfo(String name, String password, String phone, String address) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

}
