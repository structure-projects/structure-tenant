package cn.structured.tenant.service.impl;

import cn.structure.common.vo.OptionVO;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.mybatis.plus.starter.convert.ResPageConvert;
import cn.structured.tenant.assembler.TenantTemplateAssembler;
import cn.structured.tenant.dto.TenantTemplateDTO;
import cn.structured.tenant.entity.TenantTemplate;
import cn.structured.tenant.enums.TenantExceptionEnum;
import cn.structured.tenant.enums.TenantPackageStateEnum;
import cn.structured.tenant.exception.TenantException;
import cn.structured.tenant.manager.ITenantTemplateManager;
import cn.structured.tenant.query.TenantTemplateQuery;
import cn.structured.tenant.service.ITenantTemplateService;
import cn.structured.tenant.vo.TenantTemplateVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TenantTemplateServiceImpl implements ITenantTemplateService {

    private final ITenantTemplateManager tenantTemplateManager;

    @Override
    @Transactional
    public Long create(TenantTemplateDTO dto) {
        if (dto.getCode() != null && tenantTemplateManager.existsByCode(dto.getCode())) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_CODE_DUPLICATE);
        }
        if (tenantTemplateManager.existsByName(dto.getName())) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_ALREADY_EXISTS);
        }
        TenantTemplate tenantTemplate = TenantTemplateAssembler.assembler(dto);
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            TenantTemplate defaultTemplate = tenantTemplateManager.findDefaultTemplate();
            if (defaultTemplate != null) {
                defaultTemplate.setIsDefault(false);
                tenantTemplateManager.updateById(defaultTemplate);
            }
        }
        tenantTemplateManager.save(tenantTemplate);
        return tenantTemplate.getId();
    }

    @Override
    @Transactional
    public void update(Long id, TenantTemplateDTO dto) {
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(id);
        if (tenantTemplate == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        if (dto.getCode() != null && !dto.getCode().equals(tenantTemplate.getCode()) && tenantTemplateManager.existsByCode(dto.getCode())) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_CODE_DUPLICATE);
        }
        if (!dto.getName().equals(tenantTemplate.getName()) && tenantTemplateManager.existsByName(dto.getName())) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_ALREADY_EXISTS);
        }
        TenantTemplate updateTemplate = TenantTemplateAssembler.assembler(dto);
        updateTemplate.setId(id);
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            TenantTemplate defaultTemplate = tenantTemplateManager.findDefaultTemplate();
            if (defaultTemplate != null && !defaultTemplate.getId().equals(id)) {
                defaultTemplate.setIsDefault(false);
                tenantTemplateManager.updateById(defaultTemplate);
            }
        }
        tenantTemplateManager.updateById(updateTemplate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(id);
        if (tenantTemplate == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        tenantTemplateManager.removeById(id);
    }

    @Override
    public TenantTemplateVO findById(Long id) {
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(id);
        if (tenantTemplate == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        return TenantTemplateAssembler.assembler(tenantTemplate);
    }

    @Override
    public ResPage<TenantTemplateVO> page(TenantTemplateQuery query, ReqPage reqPage) {
        // 构建查询条件
        LambdaQueryWrapper<TenantTemplate> queryWrapper = Wrappers.<TenantTemplate>lambdaQuery()
                .like(null != query.getName(), TenantTemplate::getName, query.getName())
                .eq(null != query.getCode(), TenantTemplate::getCode, query.getCode())
                .eq(null != query.getState(), TenantTemplate::getState, query.getState())
                .eq(null != query.getIsDefault(), TenantTemplate::getIsDefault, query.getIsDefault())
                .ge(null != query.getCreateTimeStart(), TenantTemplate::getCreateTime, query.getCreateTimeStart())
                .le(null != query.getCreateTimeEnd(), TenantTemplate::getCreateTime, query.getCreateTimeEnd())
                .orderByAsc(TenantTemplate::getSort);
        IPage<TenantTemplate> page = tenantTemplateManager.page(new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize()), queryWrapper);
        return ResPageConvert.convert(page, TenantTemplateAssembler::assembler);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        TenantTemplate tenantTemplate = new TenantTemplate();
        tenantTemplate.setId(id);
        tenantTemplate.setState(TenantPackageStateEnum.ACTIVE.getCode());
        tenantTemplateManager.updateById(tenantTemplate);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        TenantTemplate tenantTemplate = new TenantTemplate();
        tenantTemplate.setState(TenantPackageStateEnum.DISABLED.getCode());
        tenantTemplateManager.updateById(tenantTemplate);
    }

    @Override
    @Transactional
    public void setDefault(Long id) {
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(id);
        if (tenantTemplate == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        TenantTemplate defaultTemplate = tenantTemplateManager.findDefaultTemplate();
        if (defaultTemplate != null && !defaultTemplate.getId().equals(id)) {
            defaultTemplate.setIsDefault(false);
            tenantTemplateManager.updateById(defaultTemplate);
        }
        tenantTemplate.setIsDefault(true);
        tenantTemplateManager.updateById(tenantTemplate);
    }

    @Override
    public TenantTemplateVO getDefault() {
        TenantTemplate tenantTemplate = tenantTemplateManager.findDefaultTemplate();
        if (tenantTemplate == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        return TenantTemplateAssembler.assembler(tenantTemplate);
    }

    @Override
    public List<OptionVO> listSelect() {
        return List.of();
    }


}