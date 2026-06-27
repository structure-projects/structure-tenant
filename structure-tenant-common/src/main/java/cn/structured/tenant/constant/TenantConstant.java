package cn.structured.tenant.constant;

public class TenantConstant {

    private TenantConstant() {
    }

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final String CACHE_KEY_PREFIX = "tenant:";
    public static final String CACHE_KEY_TENANT = CACHE_KEY_PREFIX + "tenant:";
    public static final String CACHE_KEY_PACKAGE = CACHE_KEY_PREFIX + "package:";
    public static final String CACHE_KEY_TEMPLATE = CACHE_KEY_PREFIX + "template:";

    public static final long CACHE_EXPIRE_SECONDS = 3600;

    public static final String DEFAULT_TEMPLATE_CODE = "DEFAULT";
}