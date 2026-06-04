# 🏢 Corp-Train-Stem — 企业培训管理系统

> 基于 Spring Boot + Vue 3 + uni-app 的企业员工培训管理平台，支持 Web 端和移动端。  
> 面向管理员、导师、学员三种角色，提供课程管理、培训计划、任务下发、在线测试等完整功能。

---

## 🖼️ 系统截图

> *（待补充：登录页、首页 Dashboard、课程管理、培训计划下发、在线答题等核心页面截图）*

---

## ⚙️ 技术栈

### 后端
- **Java 21** + **Spring Boot 3.4**
- **MyBatis** — 数据持久层（XML Mapper）
- **MySQL 8.0** — 关系型数据库
- **JWT (auth0)** — 用户认证
- **PageHelper** — 分页插件
- **Hutool** — Java 工具类库
- **Apache POI** — Excel 导入/导出

### 前端（Web）
- **Vue 3** (Composition API + `<script setup>`)
- **Element Plus** — UI 组件库
- **Vite** — 构建工具
- **Vue Router 4** — 路由管理
- **Axios** — HTTP 请求封装

### 移动端
- **uni-app x** (Vue 3 + UTS)
- **uView UI** — 移动端组件库
- 支持编译为微信小程序 / Android APP

---

## 📋 功能模块

### 👤 三种角色

| 角色 | 权限 |
|---|---|
| **管理员** | 全局管理：用户、课程、培训计划、测试题库、系统公告 |
| **企业导师** | 管理学员、制定培训计划、下发任务、审核完成情况 |
| **企业员工** | 学习课程、完成培训任务、参加在线测试、查看成绩 |

### 🔧 核心功能

- **课程管理** — 视频课程上传、封面管理、课程分类
- **培训计划** — 创建培训计划并关联课程，支持时间范围设定
- **任务下发** — 导师选择学员下发培训任务，支持搜索筛选
- **任务审核** — 学员上传图片凭证，导师审核通过/驳回
- **在线测试** — 支持单选/多选/判断题，题库管理，自动评分
- **系统公告** — 管理员发布系统公告，所有用户可见
- **用户管理** — 管理员/导师/学员信息管理，Excel 批量导入导出
- **JWT 认证** — 基于 Token 的用户登录与权限验证

---

## 🗄️ 数据库设计

共 8 张核心表：

| 表名 | 说明 |
|---|---|
| `admin` | 管理员信息 |
| `staff_user` | 导师/学员信息（role 字段区分） |
| `course` | 培训课程 |
| `training_plan` | 培训计划 |
| `training_task` | 培训任务（关联计划、学员、导师） |
| `question` | 测试题目 |
| `question_bank` / `bank_question` | 题库及题目关联 |
| `user_answer` / `user_score` | 答题记录与成绩 |
| `notice` | 系统公告 |

---

## 🚀 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+

### 后端启动

```bash
# 1. 创建数据库
mysql -u root -p -e "CREATE DATABASE companies_training DEFAULT CHARACTER SET utf8mb4;"

# 2. 导入数据
mysql -u root -p companies_training < companies_training.sql

# 3. 修改数据库配置
# 编辑 springboot/src/main/resources/application.yml，修改数据库连接信息

# 4. 启动后端
cd springboot
mvn spring-boot:run
# 后端运行在 http://localhost:9999
```

### 前端启动

```bash
cd vue
npm install
npm run dev
# 前端运行在 http://localhost:5173
```

### 移动端（可选）

使用 HBuilderX 打开 `CompaniesTrainingMobile` 目录，运行到微信小程序或 Android 模拟器。

---

## 📂 项目结构

```
Corp-Train-Stem/
├── CompaniesTraining/          # 主项目
│   ├── springboot/             # 后端 (Spring Boot)
│   │   └── src/main/java/com/hanz/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 业务逻辑层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       └── common/         # 公共模块（JWT、CORS、统一响应）
│   └── vue/                    # Web 前端 (Vue 3)
│       └── src/
│           ├── views/          # 页面组件
│           ├── components/     # 公共组件
│           ├── router/         # 路由配置
│           └── utils/          # 工具函数（Axios 封装）
└── CompaniesTrainingMobile/    # 移动端 (uni-app x)
    ├── pages/                  # 页面
    ├── api/                    # 接口
    └── utils/                  # 工具函数
```

---

## 📝 默认账号

| 角色 | 账号 | 密码 |
|---|---|---|
| 管理员 | admin | 123456 |
| 导师 | 0001 | 123456 |
| 学员 | xhr | 123456 |

---

## 📄 License

MIT License
