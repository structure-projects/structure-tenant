package cn.structured.tenant.service.impl;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.mybatis.plus.starter.convert.ResPageConvert;
import cn.structured.tenant.assembler.TenantAssembler;
import cn.structured.tenant.dto.TenantDTO;
import cn.structured.tenant.entity.Tenant;
import cn.structured.tenant.entity.TenantPackage;
import cn.structured.tenant.entity.TenantTemplate;
import cn.structured.tenant.enums.TenantExceptionEnum;
import cn.structured.tenant.enums.TenantStateEnum;
import cn.structured.tenant.exception.TenantException;
import cn.structured.tenant.manager.ITenantManager;
import cn.structured.tenant.manager.ITenantPackageManager;
import cn.structured.tenant.manager.ITenantTemplateManager;
import cn.structured.tenant.query.TenantQuery;
import cn.structured.tenant.service.ITenantService;
import cn.structured.tenant.vo.TenantVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class TenantServiceImpl implements ITenantService {

    private final ITenantManager tenantManager;
    private final ITenantPackageManager tenantPackageManager;
    private final ITenantTemplateManager tenantTemplateManager;

    @Override
    @Transactional
    public Long create(TenantDTO dto) {
        if (dto.getCode() != null && tenantManager.existsByCode(dto.getCode())) {
            throw new TenantException(TenantExceptionEnum.TENANT_CODE_DUPLICATE);
        }
        if (tenantManager.existsByName(dto.getName())) {
            throw new TenantException(TenantExceptionEnum.TENANT_NAME_DUPLICATE);
        }
        if (dto.getPackageId() != null) {
            TenantPackage tenantPackage = tenantPackageManager.getById(dto.getPackageId());
            if (tenantPackage == null) {
                throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
            }
        }
        if (dto.getTemplateId() != null) {
            TenantTemplate tenantTemplate = tenantTemplateManager.getById(dto.getTemplateId());
            if (tenantTemplate == null) {
                throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
            }
        }
        Tenant tenant = TenantAssembler.assembler(dto);
        tenantManager.save(tenant);
        return tenant.getId();
    }

    @Override
    @Transactional
    public void update(Long id, TenantDTO dto) {
        Tenant tenant = tenantManager.getById(id);
        if (tenant == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        if (dto.getCode() != null && !dto.getCode().equals(tenant.getCode()) && tenantManager.existsByCode(dto.getCode())) {
            throw new TenantException(TenantExceptionEnum.TENANT_CODE_DUPLICATE);
        }
        if (!dto.getName().equals(tenant.getName()) && tenantManager.existsByName(dto.getName())) {
            throw new TenantException(TenantExceptionEnum.TENANT_NAME_DUPLICATE);
        }
        Tenant updateTenant = TenantAssembler.assembler(dto);
        updateTenant.setId(id);
        tenantManager.updateById(updateTenant);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        tenantManager.removeById(id);
    }

    @Override
    public TenantVO findById(Long id) {
        Tenant tenant = tenantManager.getById(id);
        TenantVO vo = TenantAssembler.assembler(tenant);
        if (tenant.getPackageId() != null) {
            TenantPackage tenantPackage = tenantPackageManager.getById(tenant.getPackageId());
            if (tenantPackage != null) {
                vo.setPackageName(tenantPackage.getName());
            }
        }
        if (tenant.getTemplateId() != null) {
            TenantTemplate tenantTemplate = tenantTemplateManager.getById(tenant.getTemplateId());
            if (tenantTemplate != null) {
                vo.setTemplateName(tenantTemplate.getName());
            }
        }
        return vo;
    }

    @Override
    public ResPage<TenantVO> page(TenantQuery query, ReqPage reqPage) {
        // 构建查询条件
        LambdaQueryWrapper<Tenant> queryWrapper = Wrappers.<Tenant>lambdaQuery()
                .like(null != query.getName(), Tenant::getName, query.getName())
                .eq(null != query.getCode(), Tenant::getCode, query.getCode())
                .eq(null != query.getState(), Tenant::getState, query.getState())
                .eq(null != query.getPackageId(), Tenant::getPackageId, query.getPackageId())
                .eq(null != query.getTemplateId(), Tenant::getTemplateId, query.getTemplateId())
                .eq(null != query.getIndustry(), Tenant::getIndustry, query.getIndustry())
                .ge(null != query.getCreateTimeStart(), Tenant::getCreateTime, query.getCreateTimeStart())
                .le(null != query.getCreateTimeEnd(), Tenant::getCreateTime, query.getCreateTimeEnd())
                .orderByDesc(Tenant::getCreateTime);
        IPage<Tenant> page = tenantManager.page(new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize()), queryWrapper);
        return ResPageConvert.convert(page, TenantAssembler::assembler);
    }

    @Override
    @Transactional
    public void activate(Long id) {
        Tenant tenant = tenantManager.getById(id);
        if (tenant == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        tenant.setState(TenantStateEnum.ACTIVE.getCode());
        tenantManager.updateById(tenant);
    }

    @Override
    @Transactional
    public void suspend(Long id) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setState(TenantStateEnum.SUSPENDED.getCode());
        tenantManager.updateById(tenant);
    }

    @Override
    @Transactional
    public void freeze(Long id) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setState(TenantStateEnum.FROZEN.getCode());
        tenantManager.updateById(tenant);
    }

    @Override
    @Transactional
    public void expire(Long id) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setState(TenantStateEnum.EXPIRED.getCode());
        tenantManager.updateById(tenant);
    }

    @Override
    @Transactional
    public void applyTemplate(Long tenantId, Long templateId) {
        Tenant tenant = tenantManager.getById(tenantId);
        if (tenant == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(templateId);
        if (tenantTemplate == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        tenant.setTemplateId(templateId);
        tenantManager.updateById(tenant);
    }
}