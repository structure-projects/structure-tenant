-- H2 compatible schema for testing

-- Tenant table
CREATE TABLE IF NOT EXISTS tenant (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(100) NOT NULL COMMENT '租户名称',
    code VARCHAR(50) COMMENT '租户编码',
    logo VARCHAR(255) COMMENT '租户logo',
    description VARCHAR(500) COMMENT '租户描述',
    industry VARCHAR(100) COMMENT '所属行业',
    address VARCHAR(255) COMMENT '租户地址',
    contact_phone VARCHAR(50) COMMENT '联系电话',
    package_id BIGINT COMMENT '套餐ID',
    template_id BIGINT COMMENT '模板ID',
    state INT DEFAULT 0 COMMENT '租户状态 0:初始化 1:正常 2:停用 3:冻结 4:已过期',
    valid_start_time TIMESTAMP NULL COMMENT '有效期开始时间',
    valid_end_time TIMESTAMP NULL COMMENT '有效期结束时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    create_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by BIGINT NULL COMMENT '创建人',
    update_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by BIGINT NULL COMMENT '更新人',
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_code ON tenant(code);
CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_name ON tenant(name);

-- Tenant package table
CREATE TABLE IF NOT EXISTS tenant_package (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(100) NOT NULL COMMENT '套餐名称',
    code VARCHAR(50) COMMENT '套餐编码',
    description VARCHAR(500) COMMENT '套餐描述',
    state INT DEFAULT 0 COMMENT '套餐状态 0:草稿 1:启用 2:禁用',
    max_user_count INT DEFAULT 0 COMMENT '最大用户数',
    max_storage_gb INT DEFAULT 0 COMMENT '最大存储空间(GB)',
    max_dept_count INT DEFAULT 0 COMMENT '最大部门数',
    custom_field_enabled TINYINT(1) DEFAULT 0 COMMENT '是否支持自定义字段',
    api_enabled TINYINT(1) DEFAULT 0 COMMENT '是否支持API接口',
    sort INT DEFAULT 0 COMMENT '排序号',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    create_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by BIGINT NULL COMMENT '创建人',
    update_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by BIGINT NULL COMMENT '更新人',
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_package_code ON tenant_package(code);
CREATE UNIQUE INDEX IF NOT EXISTS uk_package_name ON tenant_package(name);

-- Tenant template table
CREATE TABLE IF NOT EXISTS tenant_template (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(100) NOT NULL COMMENT '模板名称',
    code VARCHAR(50) COMMENT '模板编码',
    description VARCHAR(500) COMMENT '模板描述',
    state INT DEFAULT 0 COMMENT '模板状态 0:草稿 1:启用 2:禁用',
    config TEXT COMMENT '模板配置(JSON格式)',
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认模板',
    sort INT DEFAULT 0 COMMENT '排序号',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    create_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by BIGINT NULL COMMENT '创建人',
    update_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by BIGINT NULL COMMENT '更新人',
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_template_code ON tenant_template(code);
CREATE UNIQUE INDEX IF NOT EXISTS uk_template_name ON tenant_template(name);
