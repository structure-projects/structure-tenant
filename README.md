# structure-tenant

租户中心服务，提供租户管理、套餐管理和模板管理的核心功能。

## 技术栈

- **语言**: Java 21
- **框架**: Spring Boot 3.2.x
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis-Plus 3.5.x
- **API文档**: SpringDoc OpenAPI 2.3.x
- **工具**: Lombok

## 项目结构

```
structure-tenant/
├── structure-tenant-api/          # API层（对外REST接口）
│   ├── src/main/java/cn/structured/tenant/
│   │   ├── controller/            # REST控制器
│   │   │   ├── TenantController.java
│   │   │   ├── TenantPackageController.java
│   │   │   └── TenantTemplateController.java
│   │   └── TenantApplication.java # Spring Boot启动类
│   └── src/main/resources/
│       └── application.yaml       # 应用配置
├── structure-tenant-biz/          # 业务层（核心业务逻辑）
│   └── src/main/java/cn/structured/tenant/
│       ├── assembler/             # DTO/VO转换
│       ├── config/                # 配置类
│       ├── entity/                # 数据库实体
│       ├── manager/               # 数据访问层
│       ├── mapper/                # MyBatis Mapper
│       └── service/               # 业务服务
├── structure-tenant-common/       # 公共模块（DTO、VO、枚举等）
│   └── src/main/java/cn/structured/tenant/
│       ├── constant/              # 常量定义
│       ├── dto/                   # 数据传输对象
│       ├── enums/                 # 枚举类
│       ├── exception/             # 自定义异常
│       ├── query/                 # 查询条件对象
│       └── vo/                    # 视图对象
└── structure-tenant-dependencies/ # 依赖管理（BOM）
```

## 核心功能

### 租户管理 (Tenant)
- 创建租户
- 更新租户信息
- 删除租户
- 查询租户详情
- 分页查询租户列表
- 租户状态管理（激活、暂停、冻结、过期）
- 应用模板

### 套餐管理 (TenantPackage)
- 创建套餐
- 更新套餐信息
- 删除套餐
- 查询套餐详情
- 分页查询套餐列表
- 启用/禁用套餐
- 套餐下拉选择

### 模板管理 (TenantTemplate)
- 创建模板
- 更新模板信息
- 删除模板
- 查询模板详情
- 分页查询模板列表
- 启用/禁用模板
- 设置默认模板
- 获取默认模板
- 模板下拉选择

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+

### 数据库配置

创建数据库并修改 `structure-tenant-api/src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/example_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: admin
    password: password
```

### 启动服务

```bash
cd structure-tenant
mvn clean package -DskipTests
java -jar structure-tenant-api/target/structure-tenant-api-1.0.0.jar
```

### 访问API文档

启动后访问: http://localhost:8080/swagger-ui.html

## API接口

| 模块 | 基础路径 | 功能 |
|:---|:---|:---|
| 租户 | `/api/tenant` | 租户CRUD及状态管理 |
| 套餐 | `/api/tenant-package` | 套餐CRUD及状态管理 |
| 模板 | `/api/tenant-template` | 模板CRUD及默认设置 |

## 代码规范

- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 方法参数使用`@Parameter`注解说明
- 异常处理使用统一异常处理器
- 日志使用SLF4J，按级别记录（info/warn/debug）

## License

MIT License
