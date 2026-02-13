<template>
  <div class="dashboard-container">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="dashboard-header">
        <div class="header-left">
          <h2>博客管理系统</h2>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="currentUser?.avatar" />
              <span class="username">{{ currentUser?.nickname || currentUser?.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item disabled>
                  {{ currentUser?.role }}
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主体内容 -->
      <el-main class="dashboard-main">
        <el-card>
          <h1>欢迎使用博客管理系统</h1>
          <p>用户登录功能已实现，您可以开始开发其他功能模块。</p>
          <el-divider />
          <div class="user-detail">
            <h3>当前用户信息：</h3>
            <p><strong>用户ID：</strong>{{ currentUser?.id }}</p>
            <p><strong>用户名：</strong>{{ currentUser?.username }}</p>
            <p><strong>昵称：</strong>{{ currentUser?.nickname }}</p>
            <p><strong>邮箱：</strong>{{ currentUser?.email || '未设置' }}</p>
            <p><strong>角色：</strong>{{ currentUser?.role }}</p>
            <p>
              <strong>最后登录：</strong
              >{{ currentUser?.lastLoginTime || '刚刚' }}
            </p>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useAuth } from '@/composables/useAuth'
import { ElMessageBox } from 'element-plus'

const { currentUser, logout, fetchUserInfo } = useAuth()

// 组件挂载时获取用户信息
onMounted(async () => {
  if (!currentUser.value) {
    try {
      await fetchUserInfo()
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }
})

/**
 * 处理下拉菜单命令
 */
const handleCommand = async (command: string) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await logout()
    } catch (error) {
      console.log('取消退出')
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  width: 100%;
  height: 100vh;
  background: #f5f5f5;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  padding: 0 20px;

  .header-left {
    h2 {
      margin: 0;
      font-size: 20px;
      color: #333;
    }
  }

  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;

      .username {
        margin-left: 10px;
        font-size: 14px;
        color: #333;
      }
    }
  }
}

.dashboard-main {
  padding: 20px;

  h1 {
    font-size: 24px;
    margin-bottom: 10px;
  }

  p {
    color: #666;
    line-height: 1.8;
  }

  .user-detail {
    margin-top: 20px;

    h3 {
      font-size: 18px;
      margin-bottom: 15px;
    }

    p {
      margin: 8px 0;
      font-size: 14px;

      strong {
        display: inline-block;
        width: 100px;
        color: #333;
      }
    }
  }
}
</style>
