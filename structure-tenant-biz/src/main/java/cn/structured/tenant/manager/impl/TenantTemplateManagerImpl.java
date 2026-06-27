package cn.structured.tenant.manager.impl;

import cn.structured.tenant.entity.TenantTemplate;
import cn.structured.tenant.manager.ITenantTemplateManager;
import cn.structured.tenant.mapper.TenantTemplateMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TenantTemplateManagerImpl extends ServiceImpl<TenantTemplateMapper, TenantTemplate> implements ITenantTemplateManager {

    @Override
    public boolean existsByCode(String code) {
        LambdaQueryWrapper<TenantTemplate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TenantTemplate::getCode, code);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean existsByName(String name) {
        LambdaQueryWrapper<TenantTemplate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TenantTemplate::getName, name);
        return count(queryWrapper) > 0;
    }

    @Override
    public TenantTemplate findDefaultTemplate() {
        LambdaQueryWrapper<TenantTemplate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TenantTemplate::getIsDefault, true);
        return getOne(queryWrapper);
    }
}