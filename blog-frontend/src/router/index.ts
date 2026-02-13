import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

/**
 * 路由配置
 */
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/admin/login'
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/views/admin/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/admin',
    redirect: '/admin/dashboard',
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: {
          title: '仪表盘',
          requiresAuth: true
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

/**
 * 路由守卫
 */
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 博客管理系统` : '博客管理系统'

  // 判断是否需要登录
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      // 未登录，跳转到登录页
      next('/admin/login')
    } else {
      next()
    }
  } else {
    // 不需要登录，直接放行
    next()
  }
})

export default router
