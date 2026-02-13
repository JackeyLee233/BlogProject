import request from '@/utils/request'
import type { LoginForm, LoginResponse, User } from '@/types/model'

/**
 * 用户登录
 */
export function login(data: LoginForm): Promise<LoginResponse> {
  return request.post('/auth/login', data)
}

/**
 * 用户登出
 */
export function logout(): Promise<void> {
  return request.post('/auth/logout')
}

/**
 * 获取当前用户信息
 */
export function getCurrentUser(): Promise<User> {
  return request.get('/auth/current')
}
