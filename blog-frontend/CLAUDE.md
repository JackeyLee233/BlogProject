# 博客系统 - 前端应用

## 项目概述

基于 Vue 3 的个人博客系统前端应用，包含博客门户（用户端）和后台管理系统（管理端），通过 RESTful API 与后端 Spring Boot 服务交互。

## 技术栈

- **框架**: Vue 3.x + TypeScript
- **构建工具**: Vite 5.x
- **状态管理**: Pinia
- **路由**: Vue Router 4.x
- **UI 组件库**: Element Plus（管理端）/ 自定义主题（门户端）
- **HTTP 请求**: Axios
- **CSS 预处理**: SCSS
- **代码规范**: ESLint + Prettier
- **包管理**: pnpm

## 常用命令

```bash
# 安装依赖
pnpm install

# 启动开发服务（默认代理到后端 http://localhost:8080）
pnpm dev

# 构建生产包
pnpm build

# 代码格式化
pnpm lint --fix

# 类型检查
pnpm type-check
```

## 项目结构

```
blog-frontend/
├── src/
│   ├── api/              # 接口请求（按模块拆分：article.ts, user.ts, comment.ts）
│   ├── assets/           # 静态资源（图片、字体、全局样式）
│   ├── components/       # 公共组件（与业务无关的通用组件）
│   ├── composables/      # 组合式函数（可复用逻辑：useAuth、usePagination）
│   ├── layouts/          # 布局组件（PortalLayout、AdminLayout）
│   ├── router/           # 路由配置
│   │   ├── index.ts
│   │   └── modules/      # 路由模块（portal.ts、admin.ts）
│   ├── stores/           # Pinia 状态管理
│   │   ├── user.ts       # 用户状态
│   │   ├── app.ts        # 应用全局状态
│   │   └── article.ts    # 文章状态
│   ├── styles/           # 全局样式
│   │   ├── variables.scss
│   │   └── global.scss
│   ├── types/            # TypeScript 类型定义
│   │   ├── api.d.ts      # 接口响应类型
│   │   └── model.d.ts    # 业务模型类型
│   ├── utils/            # 工具函数（request.ts 封装 Axios、auth.ts、format.ts）
│   ├── views/            # 页面组件
│   │   ├── portal/       # 门户端页面（首页、文章详情、分类、标签、搜索、关于）
│   │   └── admin/        # 管理端页面（仪表盘、文章管理、分类管理、评论管理、用户管理）
│   ├── App.vue
│   └── main.ts
├── public/
├── index.html
├── vite.config.ts
├── tsconfig.json
├── .eslintrc.cjs
├── .prettierrc
├── package.json
├── .gitignore
└── CLAUDE.md
```

## 代码规范

- 组件使用 `<script setup lang="ts">` 语法
- 组件文件名使用 PascalCase，如 `ArticleList.vue`、`CommentForm.vue`
- 组合式函数以 `use` 开头，如 `useAuth.ts`、`usePagination.ts`
- 类型定义统一放 `types/` 目录，接口返回类型和业务模型分文件
- props 必须定义类型和默认值，使用 `defineProps<T>()` 泛型写法
- 事件触发使用 `defineEmits<T>()`
- IMPORTANT: 禁止使用 any 类型，必须明确定义类型
- IMPORTANT: 禁止在组件中直接调用 axios，统一通过 `api/` 目录封装

## 接口请求规范

- Axios 已在 `utils/request.ts` 统一封装，包含：
  - baseURL 配置（开发环境代理、生产环境实际地址）
  - 请求拦截器：自动携带 JWT Token（从 localStorage 获取）
  - 响应拦截器：统一处理错误码，401 自动跳转登录
- 接口按模块拆分，每个文件对应后端一个 Controller
- 接口函数返回 Promise，类型与后端 VO 一一对应

```typescript
// 示例：api/article.ts
export function getArticleList(params: ArticleQuery): Promise<PageResult<ArticleVO>> {
  return request.get('/article/list', { params })
}
```

## 样式规范

- 组件样式使用 `<style lang="scss" scoped>`，防止样式污染
- 全局变量定义在 `styles/variables.scss`（主题色、字体、间距等）
- 响应式断点：移动端 768px、平板 1024px、桌面 1200px
- 管理端使用 Element Plus 组件，门户端使用自定义组件 + 自定义主题

## 工作流程

- 新增页面时同步更新路由配置
- 修改代码后运行 `pnpm lint` 和 `pnpm type-check` 确认无错误
- Git 提交信息使用中文，格式：`类型: 描述`
  - feat: 新功能 | fix: 修复 | style: 样式 | refactor: 重构
- IMPORTANT: 不要自行安装新的 npm 包，先告知我确认
- IMPORTANT: 不要修改 `utils/request.ts` 中的拦截器逻辑，除非明确讨论
- 参考 @README.md 了解项目背景
- 后端接口文档地址：http://localhost:8080/doc.html（开发环境）