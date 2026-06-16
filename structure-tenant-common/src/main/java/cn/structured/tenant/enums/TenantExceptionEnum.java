package cn.structured.tenant.enums;

public enum TenantExceptionEnum {

    TENANT_NOT_FOUND("TENANT_001", "租户不存在"),
    TENANT_ALREADY_EXISTS("TENANT_002", "租户已存在"),
    TENANT_CODE_DUPLICATE("TENANT_003", "租户编码重复"),
    TENANT_NAME_DUPLICATE("TENANT_004", "租户名称重复"),
    TENANT_STATE_ERROR("TENANT_005", "租户状态不允许此操作"),

    TENANT_PACKAGE_NOT_FOUND("TENANT_101", "租户套餐不存在"),
    TENANT_PACKAGE_ALREADY_EXISTS("TENANT_102", "租户套餐已存在"),
    TENANT_PACKAGE_CODE_DUPLICATE("TENANT_103", "租户套餐编码重复"),
    TENANT_PACKAGE_STATE_ERROR("TENANT_104", "租户套餐状态不允许此操作"),

    TENANT_TEMPLATE_NOT_FOUND("TENANT_201", "租户模板不存在"),
    TENANT_TEMPLATE_ALREADY_EXISTS("TENANT_202", "租户模板已存在"),
    TENANT_TEMPLATE_CODE_DUPLICATE("TENANT_203", "租户模板编码重复"),
    TENANT_TEMPLATE_STATE_ERROR("TENANT_204", "租户模板状态不允许此操作");

    private final String code;
    private final String message;

    TenantExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}