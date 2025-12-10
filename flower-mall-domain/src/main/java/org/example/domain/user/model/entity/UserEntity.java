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

    /**
     * 校验密码
     */
    public boolean matchPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    /**
     * 修改个人信息
     */
    public void changeInfo(String phone, String address) {
        this.phone = phone;
        this.address = address;
    }

}
