# 租户管理项目规范

## 📅 文档信息

- **版本**: 1.0.0
- **创建日期**: 2026-06-16
- **最后更新**: 2026-06-16

## 📁 项目结构规范

### 整体架构

项目采用标准的微服务分层架构，分为四个模块：

```
structure-tenant/
├── structure-tenant-api/        # 控制层（对外暴露REST API）
├── structure-tenant-biz/        # 业务层（核心业务逻辑）
├── structure-tenant-common/     # 公共层（DTO、VO、枚举、异常等）
└── structure-tenant-dependencies/ # 依赖管理（统一版本控制）
```

### 模块职责说明

| 模块 | 职责 | 包含包 |
|-----|------|--------|
| **structure-tenant-api** | 控制层，处理HTTP请求 | `controller/`, `TenantApplication.java` |
| **structure-tenant-biz** | 业务逻辑层 | `service/`, `manager/`, `mapper/`, `entity/`, `assembler/`, `config/` |
| **structure-tenant-common** | 公共组件 | `dto/`, `vo/`, `query/`, `enums/`, `exception/`, `constant/` |
| **structure-tenant-dependencies** | Maven依赖管理 | `pom.xml` |

### 包结构详解

```
cn.structured.tenant/
├── controller/     # REST API控制层，处理请求和响应
├── service/        # 业务服务层，定义业务接口和实现
├── manager/        # 数据管理层，封装数据访问逻辑
├── mapper/         # MyBatis数据访问层
├── entity/         # 数据库实体类
├── assembler/      # 对象转换器（Entity ↔ DTO/VO）
├── dto/            # 数据传输对象（请求参数）
├── vo/             # 视图对象（响应数据）
├── query/          # 查询条件对象
├── enums/          # 枚举定义（状态码、错误码等）
├── exception/      # 自定义业务异常
├── constant/       # 常量定义
└── config/         # 配置类
```

---

## 业务功能说明

### 1. 租户生命周期管理

租户状态流转：
- **初始化(0)** → **正常(1)**：启用租户
- **正常(1)** → **停用(2)**：停用租户
- **正常(1)** → **冻结(3)**：冻结租户
- **正常(1)** → **已过期(4)**：过期租户

### 2. 租户套餐管理

套餐包含以下配置：
- 最大用户数
- 最大存储空间(GB)
- 最大部门数
- 是否支持自定义字段
- 是否支持API接口

### 3. 租户模板管理

模板用于租户初始化配置，包含：
- 模板配置(JSON格式)
- 默认模板标记

---

## 枚举规范 (enums/)

### 枚举命名规范

| 类型 | 命名模式 | 示例 |
|-----|---------|------|
| 状态枚举 | `{业务}StateEnum` | `TenantStateEnum` |
| 类型枚举 | `{业务}TypeEnum` | `TenantPackageStateEnum` |
| 错误码枚举 | `{业务}ExceptionEnum` | `TenantExceptionEnum` |

---

## 异常规范 (exception/)

自定义业务异常类：`TenantException`，继承 `cn.structure.common.exception.CommonException`

---

## 响应规范

使用 `cn.structure.common.utils.ResultUtilSimpleImpl` 封装响应：

```java
ResultUtilSimpleImpl.success(data);
ResultUtilSimpleImpl.fail(code, message);
```

---

## 命名规范汇总

| 类型 | 命名模式 | 示例 |
|-----|---------|------|
| 枚举类 | `{业务}Enum` | `TenantExceptionEnum` |
| 异常类 | `{业务}Exception` | `TenantException` |
| 转换器 | `{业务}Assembler` | `TenantAssembler` |
| 常量类 | `{业务}Constant` | `TenantConstant` |
| 实体类 | `{业务}` | `Tenant` |
| DTO类 | `{业务}DTO` | `TenantDTO` |
| VO类 | `{业务}VO` | `TenantVO` |
| Query类 | `{业务}Query` | `TenantQuery` |
| Service接口 | `I{业务}Service` | `ITenantService` |
| Service实现 | `{业务}ServiceImpl` | `TenantServiceImpl` |
| Manager接口 | `I{业务}Manager` | `ITenantManager` |
| Manager实现 | `{业务}ManagerImpl` | `TenantManagerImpl` |
| Controller | `{业务}Controller` | `TenantController` |
| Mapper | `{业务}Mapper` | `TenantMapper` |

---

## 📚 相关文档

- [README](README.md)

## 🔄 文档更新日志

### v1.0.0 (2026-06-16)

- 初始版本
- 定义租户生命周期管理、套餐管理、模板管理
- 定义项目结构和代码规范