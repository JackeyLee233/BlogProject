package com.blog.config;

import com.blog.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String REDIS_TOKEN_KEY_PREFIX = "blog:user:token:";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. 从请求头获取 Token
            String token = getTokenFromRequest(request);

            // 2. 验证 Token
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                // 3. 从 Token 中获取用户信息
                Long userId = jwtUtils.getUserIdFromToken(token);
                String username = jwtUtils.getUsernameFromToken(token);

                if (userId != null && username != null) {
                    // 4. 校验 Redis 中的 Token（防止使用过期或被强制下线的 Token）
                    String redisKey = REDIS_TOKEN_KEY_PREFIX + userId;
                    String redisToken = redisTemplate.opsForValue().get(redisKey);

                    if (token.equals(redisToken)) {
                        // 5. 创建认证对象并设置到 Security 上下文
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userId,
                                        null,
                                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        log.debug("用户 {} 认证成功", username);
                    } else {
                        log.warn("Token 已失效或被强制下线，userId: {}", userId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JWT 认证失败", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中获取 Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
