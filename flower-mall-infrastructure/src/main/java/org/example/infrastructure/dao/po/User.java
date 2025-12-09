package org.example.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author lanjiajun
 * @description
 * 用户PO对象，对应数据库表users
 * @create 2025-12-09 09:42
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String account;
    private String name;
    private String password;
    private String phone;
    private String address;
    private String role;
}
