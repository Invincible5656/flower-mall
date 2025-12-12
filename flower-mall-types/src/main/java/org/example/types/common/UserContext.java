package org.example.types.common;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-12 22:46
 */
public class UserContext {
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>(); // 新增


    public static void set(String userId, String username, String role) {
        USER_ID.set(userId);
        USERNAME.set(username);
        ROLE.set(role);
    }

    public static String getUserId() { return USER_ID.get(); }
    public static String getUsername() { return USERNAME.get(); }
    public static String getRole() { return ROLE.get(); }

    public static void remove() {
        USER_ID.remove();
        USERNAME.remove();
        ROLE.remove();
    }
}
