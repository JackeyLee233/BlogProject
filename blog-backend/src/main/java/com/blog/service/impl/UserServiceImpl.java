package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.exception.BusinessException;
import com.blog.mapper.UserMapper;
import com.blog.model.dto.LoginDTO;
import com.blog.model.entity.User;
import com.blog.model.vo.LoginVO;
import com.blog.model.vo.UserInfoVO;
import com.blog.service.UserService;
import com.blog.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String REDIS_TOKEN_KEY_PREFIX = "blog:user:token:";

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 1. 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 4. 生成 Token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        // 5. 将 Token 存入 Redis（支持强制下线和单点登录）
        String redisKey = REDIS_TOKEN_KEY_PREFIX + user.getId();
        redisTemplate.opsForValue().set(
                redisKey,
                token,
                jwtUtils.getExpiration(),
                TimeUnit.MILLISECONDS
        );

        // 6. 构建用户信息 VO
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .lastLoginTime(user.getLastLoginTime())
                .build();

        // 7. 返回登录响应
        return LoginVO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getExpiration())
                .userInfo(userInfoVO)
                .build();
    }

    @Override
    public void logout(Long userId) {
        // 从 Redis 删除 Token，实现登出
        String redisKey = REDIS_TOKEN_KEY_PREFIX + userId;
        redisTemplate.delete(redisKey);
        log.info("用户 {} 已登出", userId);
    }

    @Override
    public UserInfoVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .lastLoginTime(user.getLastLoginTime())
                .build();
    }

    @Override
    public void updateLastLogin(Long userId, String loginIp) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(loginIp);
        userMapper.updateById(user);
    }
}
