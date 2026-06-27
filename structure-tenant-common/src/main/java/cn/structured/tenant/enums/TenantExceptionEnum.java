package cn.structured.tenant.enums;

public enum TenantExceptionEnum {

    TENANT_NOT_FOUND("110701", "租户不存在"),
    TENANT_ALREADY_EXISTS("110702", "租户已存在"),
    TENANT_CODE_DUPLICATE("110703", "租户编码重复"),
    TENANT_NAME_DUPLICATE("110704", "租户名称重复"),
    TENANT_STATE_ERROR("110705", "租户状态不允许此操作"),

    TENANT_PACKAGE_NOT_FOUND("110711", "租户套餐不存在"),
    TENANT_PACKAGE_ALREADY_EXISTS("110712", "租户套餐已存在"),
    TENANT_PACKAGE_CODE_DUPLICATE("110713", "租户套餐编码重复"),
    TENANT_PACKAGE_STATE_ERROR("110714", "租户套餐状态不允许此操作"),

    TENANT_TEMPLATE_NOT_FOUND("110721", "租户模板不存在"),
    TENANT_TEMPLATE_ALREADY_EXISTS("110722", "租户模板已存在"),
    TENANT_TEMPLATE_CODE_DUPLICATE("110723", "租户模板编码重复"),
    TENANT_TEMPLATE_STATE_ERROR("110724", "租户模板状态不允许此操作");

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