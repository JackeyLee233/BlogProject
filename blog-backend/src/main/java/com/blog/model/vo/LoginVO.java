package com.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录响应")
public class LoginVO {

    @Schema(description = "JWT Token")
    private String token;

    @Schema(description = "Token 类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "过期时间（毫秒）")
    private Long expiresIn;

    @Schema(description = "用户信息")
    private UserInfoVO userInfo;
}
