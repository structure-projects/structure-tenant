package cn.structured.tenant.exception;

import cn.structure.common.exception.CommonException;
import cn.structured.tenant.enums.TenantExceptionEnum;

public class TenantException extends CommonException {

    public TenantException(TenantExceptionEnum exceptionEnum) {
        super(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }

    public TenantException(String code, String message) {
        super(code, message);
    }
}