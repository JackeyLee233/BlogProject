import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResponse } from '@/types/api'
import router from '@/router'

/**
 * 创建 Axios 实例
 */
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

/**
 * 请求拦截器
 */
service.interceptors.request.use(
  config => {
    // 自动携带 Token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

/**
 * 响应拦截器
 */
service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const res = response.data

    // 成功响应
    if (res.code === 200) {
      return res
    }

    // 业务错误
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  error => {
    console.error('响应错误：', error)

    // 处理 HTTP 状态码错误
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message

      switch (status) {
        case 401:
          ElMessage.error('未登录或登录已过期，请重新登录')
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          router.push('/admin/login')
          break
        case 403:
          ElMessage.error(message || '权限不足')
          break
        case 404:
          ElMessage.error(message || '请求的资源不存在')
          break
        case 500:
          ElMessage.error(message || '服务器错误')
          break
        default:
          ElMessage.error(message || '请求失败')
      }
    } else if (error.request) {
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      ElMessage.error(error.message || '请求失败')
    }

    return Promise.reject(error)
  }
)

/**
 * 封装请求方法
 */
const request = {
  get<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.get(url, config).then(res => res.data as T)
  },

  post<T = unknown>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return service.post(url, data, config).then(res => res.data as T)
  },

  put<T = unknown>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return service.put(url, data, config).then(res => res.data as T)
  },

  delete<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.delete(url, config).then(res => res.data as T)
  }
}

export default request
