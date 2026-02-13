package com.blog.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具（用于生成初始密码）
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 生成密码：admin123
        String password = "admin123";
        String encodedPassword = encoder.encode(password);

        System.out.println("原始密码：" + password);
        System.out.println("加密后密码：" + encodedPassword);

        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("验证结果：" + matches);
    }
}
