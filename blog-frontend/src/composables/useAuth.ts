import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * 认证相关组合式函数
 */
export function useAuth() {
  const userStore = useUserStore()

  // 是否已登录
  const isLoggedIn = computed(() => userStore.isLoggedIn())

  // 当前用户信息
  const currentUser = computed(() => userStore.userInfo)

  // Token
  const token = computed(() => userStore.token)

  // 登录
  const login = userStore.login

  // 登出
  const logout = userStore.logout

  // 获取用户信息
  const fetchUserInfo = userStore.fetchUserInfo

  return {
    isLoggedIn,
    currentUser,
    token,
    login,
    logout,
    fetchUserInfo
  }
}
