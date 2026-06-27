package cn.structured.tenant.manager.impl;

import cn.structured.tenant.entity.TenantPackage;
import cn.structured.tenant.manager.ITenantPackageManager;
import cn.structured.tenant.mapper.TenantPackageMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TenantPackageManagerImpl extends ServiceImpl<TenantPackageMapper, TenantPackage> implements ITenantPackageManager {


    @Override
    public boolean existsByCode(String code) {
        LambdaQueryWrapper<TenantPackage> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TenantPackage::getCode, code);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean existsByName(String name) {
        LambdaQueryWrapper<TenantPackage> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TenantPackage::getName, name);
        return count(queryWrapper) > 0;
    }
}