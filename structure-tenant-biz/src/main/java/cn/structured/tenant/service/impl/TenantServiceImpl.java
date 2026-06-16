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
        log.info("开始创建租户，租户名称: {}, 租户编码: {}", dto.getName(), dto.getCode());
        
        if (dto.getCode() != null && tenantManager.existsByCode(dto.getCode())) {
            log.warn("租户编码重复，编码: {}", dto.getCode());
            throw new TenantException(TenantExceptionEnum.TENANT_CODE_DUPLICATE);
        }
        if (tenantManager.existsByName(dto.getName())) {
            log.warn("租户名称重复，名称: {}", dto.getName());
            throw new TenantException(TenantExceptionEnum.TENANT_NAME_DUPLICATE);
        }
        if (dto.getPackageId() != null) {
            TenantPackage tenantPackage = tenantPackageManager.getById(dto.getPackageId());
            if (tenantPackage == null) {
                log.warn("租户套餐不存在，套餐ID: {}", dto.getPackageId());
                throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
            }
        }
        if (dto.getTemplateId() != null) {
            TenantTemplate tenantTemplate = tenantTemplateManager.getById(dto.getTemplateId());
            if (tenantTemplate == null) {
                log.warn("租户模板不存在，模板ID: {}", dto.getTemplateId());
                throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
            }
        }
        
        Tenant tenant = TenantAssembler.assembler(dto);
        tenantManager.save(tenant);
        
        log.info("租户创建成功，租户ID: {}, 租户名称: {}", tenant.getId(), tenant.getName());
        return tenant.getId();
    }

    @Override
    @Transactional
    public void update(Long id, TenantDTO dto) {
        log.info("开始更新租户，租户ID: {}, 新租户名称: {}", id, dto.getName());
        
        Tenant tenant = tenantManager.getById(id);
        if (tenant == null) {
            log.warn("租户不存在，租户ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        if (dto.getCode() != null && !dto.getCode().equals(tenant.getCode()) && tenantManager.existsByCode(dto.getCode())) {
            log.warn("租户编码重复，编码: {}", dto.getCode());
            throw new TenantException(TenantExceptionEnum.TENANT_CODE_DUPLICATE);
        }
        if (!dto.getName().equals(tenant.getName()) && tenantManager.existsByName(dto.getName())) {
            log.warn("租户名称重复，名称: {}", dto.getName());
            throw new TenantException(TenantExceptionEnum.TENANT_NAME_DUPLICATE);
        }
        
        Tenant updateTenant = TenantAssembler.assembler(dto);
        updateTenant.setId(id);
        tenantManager.updateById(updateTenant);
        
        log.info("租户更新成功，租户ID: {}, 租户名称: {}", id, dto.getName());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("开始删除租户，租户ID: {}", id);
        
        Tenant tenant = tenantManager.getById(id);
        if (tenant == null) {
            log.warn("租户不存在，租户ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        
        tenantManager.removeById(id);
        
        log.info("租户删除成功，租户ID: {}, 租户名称: {}", id, tenant.getName());
    }

    @Override
    public TenantVO findById(Long id) {
        log.info("查询租户详情，租户ID: {}", id);
        
        Tenant tenant = tenantManager.getById(id);
        if (tenant == null) {
            log.warn("租户不存在，租户ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        
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
        
        log.debug("查询租户详情成功，租户ID: {}, 租户名称: {}", id, vo.getName());
        return vo;
    }

    @Override
    public ResPage<TenantVO> page(TenantQuery query, ReqPage reqPage) {
        log.info("分页查询租户列表，页码: {}, 每页大小: {}, 查询条件: {}", 
                reqPage.getCurrentPage(), reqPage.getPageSize(), query);
        
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
        ResPage<TenantVO> result = ResPageConvert.convert(page, TenantAssembler::assembler);
        
        log.debug("分页查询租户列表成功，总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getSize());
        return result;
    }

    @Override
    @Transactional
    public void activate(Long id) {
        log.info("开始激活租户，租户ID: {}", id);
        
        Tenant tenant = tenantManager.getById(id);
        if (tenant == null) {
            log.warn("租户不存在，租户ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        
        tenant.setState(TenantStateEnum.ACTIVE.getCode());
        tenantManager.updateById(tenant);
        
        log.info("租户激活成功，租户ID: {}, 租户名称: {}", id, tenant.getName());
    }

    @Override
    @Transactional
    public void suspend(Long id) {
        log.info("开始暂停租户，租户ID: {}", id);
        
        Tenant tenant = tenantManager.getById(id);
        if (tenant == null) {
            log.warn("租户不存在，租户ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        
        tenant.setState(TenantStateEnum.SUSPENDED.getCode());
        tenantManager.updateById(tenant);
        
        log.info("租户暂停成功，租户ID: {}, 租户名称: {}", id, tenant.getName());
    }

    @Override
    @Transactional
    public void freeze(Long id) {
        log.info("开始冻结租户，租户ID: {}", id);
        
        Tenant tenant = tenantManager.getById(id);
        if (tenant == null) {
            log.warn("租户不存在，租户ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        
        tenant.setState(TenantStateEnum.FROZEN.getCode());
        tenantManager.updateById(tenant);
        
        log.info("租户冻结成功，租户ID: {}, 租户名称: {}", id, tenant.getName());
    }

    @Override
    @Transactional
    public void expire(Long id) {
        log.info("开始过期租户，租户ID: {}", id);
        
        Tenant tenant = tenantManager.getById(id);
        if (tenant == null) {
            log.warn("租户不存在，租户ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        
        tenant.setState(TenantStateEnum.EXPIRED.getCode());
        tenantManager.updateById(tenant);
        
        log.info("租户过期成功，租户ID: {}, 租户名称: {}", id, tenant.getName());
    }

    @Override
    @Transactional
    public void applyTemplate(Long tenantId, Long templateId) {
        log.info("开始为租户应用模板，租户ID: {}, 模板ID: {}", tenantId, templateId);
        
        Tenant tenant = tenantManager.getById(tenantId);
        if (tenant == null) {
            log.warn("租户不存在，租户ID: {}", tenantId);
            throw new TenantException(TenantExceptionEnum.TENANT_NOT_FOUND);
        }
        
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(templateId);
        if (tenantTemplate == null) {
            log.warn("租户模板不存在，模板ID: {}", templateId);
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        
        tenant.setTemplateId(templateId);
        tenantManager.updateById(tenant);
        
        log.info("租户模板应用成功，租户ID: {}, 租户名称: {}, 模板ID: {}, 模板名称: {}", 
                tenantId, tenant.getName(), templateId, tenantTemplate.getName());
    }
}