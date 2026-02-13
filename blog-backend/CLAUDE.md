# 博客系统 - 后端服务

## 项目概述

基于 Spring Boot 的个人博客系统后端 RESTful API 服务，采用前后端分离架构，为前端 Vue 应用提供数据接口。

## 技术栈

- **语言**: Java 17
- **框架**: Spring Boot 3.x + MyBatis-Plus 3.5.x
- **数据库**: MySQL 8.0
- **缓存**: Redis 7.x（会话、热点数据缓存、点赞计数）
- **消息队列**: RabbitMQ 3.x（异步通知、ES数据同步、邮件发送）
- **搜索引擎**: ElasticSearch 8.x（文章全文检索）
- **API文档**: Knife4j 4.x
- **安全**: Spring Security + JWT
- **构建**: Maven 3.9+
- **部署**: Ubuntu 24.04 + Docker + Nginx

## 常用命令

```bash
# 启动项目
mvn spring-boot:run

# 编译打包（跳过测试）
mvn clean package -DskipTests

# 运行测试（优先运行单个测试类，不要跑全量）
mvn test -Dtest=XxxServiceTest

# 代码格式化检查
mvn spotless:check

# 数据库变更后重新生成 MyBatis-Plus 代码
# 使用 MyBatis-Plus Generator，配置见 CodeGenerator.java
```

## 项目结构

```
blog-backend/
├── src/main/java/com/blog/
│   ├── config/           # 配置类（Redis、RabbitMQ、ES、Security、Swagger）
│   ├── controller/       # 控制器（仅做参数校验和调用 Service）
│   ├── service/          # 业务接口
│   │   └── impl/         # 业务实现
│   ├── mapper/           # MyBatis-Plus Mapper 接口
│   ├── model/
│   │   ├── entity/       # 数据库实体（对应表结构）
│   │   ├── dto/          # 前端请求参数对象
│   │   ├── vo/           # 返回给前端的视图对象
│   │   └── enums/        # 枚举类
│   ├── common/           # 公共类（统一响应、错误码、分页封装）
│   ├── exception/        # 全局异常处理
│   ├── interceptor/      # 拦截器（登录校验、限流等）
│   ├── mq/               # RabbitMQ 消息生产者和消费者
│   ├── es/               # ElasticSearch 文档和仓库
│   └── utils/            # 工具类
├── src/main/resources/
│   ├── application.yml            # 主配置
│   ├── application-dev.yml        # 开发环境配置
│   ├── application-prod.yml       # 生产环境配置
│   └── mapper/                    # MyBatis XML 映射文件
├── src/test/java/                 # 测试代码
├── sql/                           # 数据库建表和初始化脚本
├── pom.xml
├── .gitignore
└── CLAUDE.md
```

## 代码规范

- 遵循阿里巴巴 Java 开发手册
- 类名使用 UpperCamelCase，方法名和变量使用 lowerCamelCase，常量使用 UPPER_SNAKE_CASE
- Controller 层不写业务逻辑，只做参数校验 + 调用 Service + 封装返回
- Service 接口和实现分离，实现类加 `@Service` 注解
- Entity 使用 `@TableName` 注解对应表名，字段使用 `@TableField`
- DTO 用于接收前端参数并用 `@Valid` 校验，VO 用于返回前端数据
- IMPORTANT: Entity、DTO、VO 三者严格分离，禁止 Entity 直接返回前端
- 所有 API 使用统一响应体 `Result<T>`，格式：`{code, message, data}`
- 异常统一在 GlobalExceptionHandler 中处理，禁止 Controller 中 try-catch

## 数据库规范

- 表名使用 `t_` 前缀 + 小写下划线，如 `t_article`、`t_comment`
- 每张表必须有 `id`（主键自增）、`create_time`、`update_time`、`is_deleted`（逻辑删除）
- MyBatis-Plus 已配置自动填充 `create_time` 和 `update_time`
- MyBatis-Plus 已配置逻辑删除字段 `is_deleted`
- 复杂查询写在 Mapper XML 中，简单 CRUD 使用 MyBatis-Plus 内置方法

## 中间件使用约定

### Redis
- Key 命名格式：`blog:模块:业务:标识`，如 `blog:article:hot:list`、`blog:user:token:{userId}`
- 所有 Key 必须设置过期时间，禁止永久 Key
- 使用 RedisTemplate 操作，缓存逻辑封装在 Service 层

### RabbitMQ
- 交换机命名：`blog.exchange.模块`，如 `blog.exchange.article`
- 队列命名：`blog.queue.业务`，如 `blog.queue.es-sync`
- 路由键命名：`blog.routing.操作`，如 `blog.routing.article.publish`
- 消费者需做幂等处理，防止消息重复消费

### ElasticSearch
- 索引命名：`blog_模块`，如 `blog_article`
- 文章发布/更新/删除时，通过 RabbitMQ 异步同步到 ES
- 搜索接口使用 ES，其他接口使用 MySQL

## 工作流程

- 修改代码后运行相关测试，确认通过再提交
- Git 提交信息使用中文，格式：`类型: 描述`
  - feat: 新功能 | fix: 修复 | refactor: 重构 | docs: 文档 | test: 测试
- 新增表必须同时在 sql/ 目录维护建表 SQL
- IMPORTANT: 不要自行引入新的 Maven 依赖，先告知我确认
- IMPORTANT: 不要修改 application-prod.yml 中的敏感配置
- 参考 @README.md 了解项目背景
- 参考 @sql/ 目录了解现有表结构