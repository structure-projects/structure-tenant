package cn.structured.tenant.manager;

import cn.structured.tenant.entity.TenantTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ITenantTemplateManager extends IService<TenantTemplate> {


    boolean existsByCode(String code);

    boolean existsByName(String name);

    TenantTemplate findDefaultTemplate();
}