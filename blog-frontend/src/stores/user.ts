import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User, LoginForm } from '@/types/model'
import * as userApi from '@/api/user'
import router from '@/router'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<User | null>(
    localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')!) : null
  )

  /**
   * 用户登录
   */
  const login = async (loginForm: LoginForm) => {
    try {
      const response = await userApi.login(loginForm)

      // 保存 Token 和用户信息
      token.value = response.token
      userInfo.value = response.userInfo

      localStorage.setItem('token', response.token)
      localStorage.setItem('userInfo', JSON.stringify(response.userInfo))

      ElMessage.success('登录成功')

      // 跳转到管理后台首页
      router.push('/admin/dashboard')
    } catch (error) {
      console.error('登录失败：', error)
      throw error
    }
  }

  /**
   * 用户登出
   */
  const logout = async () => {
    try {
      await userApi.logout()
    } catch (error) {
      console.error('登出失败：', error)
    } finally {
      // 清除本地数据
      token.value = ''
      userInfo.value = null
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')

      // 跳转到登录页
      router.push('/admin/login')
      ElMessage.success('已退出登录')
    }
  }

  /**
   * 获取当前用户信息
   */
  const fetchUserInfo = async () => {
    try {
      const user = await userApi.getCurrentUser()
      userInfo.value = user
      localStorage.setItem('userInfo', JSON.stringify(user))
    } catch (error) {
      console.error('获取用户信息失败：', error)
      throw error
    }
  }

  /**
   * 判断是否已登录
   */
  const isLoggedIn = () => {
    return !!token.value
  }

  return {
    token,
    userInfo,
    login,
    logout,
    fetchUserInfo,
    isLoggedIn
  }
})
