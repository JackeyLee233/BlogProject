package com.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（登录账号）
     */
    private String username;

    /**
     * 密码（BCrypt加密）
     */
    private String password;

    /**
     * 昵称（显示用）
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 角色：ADMIN-管理员
     */
    private String role;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-正常 1-删除
     */
    @TableLogic
    private Integer isDeleted;
}
