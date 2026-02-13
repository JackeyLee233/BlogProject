package com.blog.service;

import com.blog.model.dto.LoginDTO;
import com.blog.model.vo.LoginVO;
import com.blog.model.vo.UserInfoVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录参数
     * @return 登录响应（包含 Token）
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户登出
     *
     * @param userId 用户ID
     */
    void logout(Long userId);

    /**
     * 获取当前用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVO getCurrentUser(Long userId);

    /**
     * 更新最后登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     */
    void updateLastLogin(Long userId, String loginIp);
}
