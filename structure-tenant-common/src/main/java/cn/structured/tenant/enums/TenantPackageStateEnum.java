package cn.structured.tenant.enums;

public enum TenantPackageStateEnum {

    DRAFT(0, "草稿"),
    ACTIVE(1, "启用"),
    DISABLED(2, "禁用");

    private final Integer code;
    private final String description;

    TenantPackageStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TenantPackageStateEnum fromCode(Integer code) {
        for (TenantPackageStateEnum state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }
}