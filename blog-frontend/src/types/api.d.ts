/**
 * API 接口响应类型定义
 */

/**
 * 统一响应结果
 */
export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
}

/**
 * 分页查询参数
 */
export interface PageQuery {
  page: number
  size: number
}

/**
 * 分页结果
 */
export interface PageResult<T> {
  records: T[]
  total: number
  current: number
  size: number
}
