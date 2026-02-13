package com.blog.controller;

import com.blog.common.Result;
import com.blog.model.dto.LoginDTO;
import com.blog.model.vo.LoginVO;
import com.blog.model.vo.UserInfoVO;
import com.blog.service.UserService;
import com.blog.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户登录、登出、获取当前用户信息")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码登录，返回 JWT Token")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        log.info("用户 {} 尝试登录", loginDTO.getUsername());

        // 执行登录
        LoginVO loginVO = userService.login(loginDTO);

        // 更新最后登录信息
        String loginIp = getClientIp(request);
        userService.updateLastLogin(loginVO.getUserInfo().getId(), loginIp);

        log.info("用户 {} 登录成功", loginDTO.getUsername());
        return Result.success(loginVO);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "清除 Token，退出登录")
    public Result<Void> logout(@RequestHeader("Authorization") @Parameter(description = "JWT Token") String token) {
        // 从 Token 中获取用户ID
        String actualToken = token.replace("Bearer ", "");
        Long userId = jwtUtils.getUserIdFromToken(actualToken);

        if (userId != null) {
            userService.logout(userId);
            log.info("用户 {} 已登出", userId);
        }

        return Result.success("登出成功", null);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    @Operation(summary = "获取当前用户信息", description = "根据 Token 获取当前登录用户的详细信息")
    public Result<UserInfoVO> getCurrentUser(@RequestHeader("Authorization") @Parameter(description = "JWT Token") String token) {
        // 从 Token 中获取用户ID
        String actualToken = token.replace("Bearer ", "");
        Long userId = jwtUtils.getUserIdFromToken(actualToken);

        if (userId == null) {
            return Result.error(401, "未登录或 Token 无效");
        }

        UserInfoVO userInfo = userService.getCurrentUser(userId);
        return Result.success(userInfo);
    }

    /**
     * 获取客户端真实 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果有多个代理，取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
