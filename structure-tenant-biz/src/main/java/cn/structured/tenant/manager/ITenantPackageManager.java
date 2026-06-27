package cn.structured.tenant.manager;

import cn.structured.tenant.entity.TenantPackage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ITenantPackageManager extends IService<TenantPackage> {

    boolean existsByCode(String code);

    boolean existsByName(String name);
}