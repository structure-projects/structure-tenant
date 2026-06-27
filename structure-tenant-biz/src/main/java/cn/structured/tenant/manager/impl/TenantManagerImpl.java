package cn.structured.tenant.manager.impl;

import cn.structured.tenant.entity.Tenant;
import cn.structured.tenant.manager.ITenantManager;
import cn.structured.tenant.mapper.TenantMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TenantManagerImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantManager {

    @Override
    public boolean existsByCode(String code) {
        LambdaQueryWrapper<Tenant> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Tenant::getCode, code);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean existsByName(String name) {
        LambdaQueryWrapper<Tenant> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Tenant::getName, name);
        return count(queryWrapper) > 0;
    }
}