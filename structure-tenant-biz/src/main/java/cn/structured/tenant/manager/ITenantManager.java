package cn.structured.tenant.manager;

import cn.structured.tenant.entity.Tenant;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ITenantManager extends IService<Tenant> {

    boolean existsByCode(String code);

    boolean existsByName(String name);
}