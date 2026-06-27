package cn.structured.tenant.enums;

public enum TenantStateEnum {

    INIT(0, "初始化"),
    ACTIVE(1, "正常"),
    SUSPENDED(2, "停用"),
    FROZEN(3, "冻结"),
    EXPIRED(4, "已过期");

    private final Integer code;
    private final String description;

    TenantStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TenantStateEnum fromCode(Integer code) {
        for (TenantStateEnum state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }
}