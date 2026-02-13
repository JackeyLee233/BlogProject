/**
 * 业务模型类型定义
 */

/**
 * 用户信息
 */
export interface User {
  id: number
  username: string
  nickname: string
  email?: string
  avatar?: string
  role: string
  lastLoginTime?: string
}

/**
 * 登录表单
 */
export interface LoginForm {
  username: string
  password: string
}

/**
 * 登录响应
 */
export interface LoginResponse {
  token: string
  tokenType: string
  expiresIn: number
  userInfo: User
}
