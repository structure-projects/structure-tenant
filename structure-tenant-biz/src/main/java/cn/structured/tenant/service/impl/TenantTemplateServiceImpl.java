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
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TenantTemplateServiceImpl implements ITenantTemplateService {

    private final ITenantTemplateManager tenantTemplateManager;

    @Override
    @Transactional
    public Long create(TenantTemplateDTO dto) {
        log.info("开始创建租户模板，模板名称: {}, 模板编码: {}", dto.getName(), dto.getCode());
        
        if (dto.getCode() != null && tenantTemplateManager.existsByCode(dto.getCode())) {
            log.warn("租户模板编码重复，编码: {}", dto.getCode());
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_CODE_DUPLICATE);
        }
        if (tenantTemplateManager.existsByName(dto.getName())) {
            log.warn("租户模板名称重复，名称: {}", dto.getName());
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_ALREADY_EXISTS);
        }
        
        TenantTemplate tenantTemplate = TenantTemplateAssembler.assembler(dto);
        
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            TenantTemplate defaultTemplate = tenantTemplateManager.findDefaultTemplate();
            if (defaultTemplate != null) {
                log.debug("将原默认模板设置为非默认，模板ID: {}", defaultTemplate.getId());
                defaultTemplate.setIsDefault(false);
                tenantTemplateManager.updateById(defaultTemplate);
            }
        }
        
        tenantTemplateManager.save(tenantTemplate);
        
        log.info("租户模板创建成功，模板ID: {}, 模板名称: {}", tenantTemplate.getId(), tenantTemplate.getName());
        return tenantTemplate.getId();
    }

    @Override
    @Transactional
    public void update(Long id, TenantTemplateDTO dto) {
        log.info("开始更新租户模板，模板ID: {}, 新模板名称: {}", id, dto.getName());
        
        if (dto.getCode() != null && tenantTemplateManager.existsByCode(dto.getCode())) {
            log.warn("租户模板编码重复，编码: {}", dto.getCode());
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_CODE_DUPLICATE);
        }
        if (tenantTemplateManager.existsByName(dto.getName())) {
            log.warn("租户模板名称重复，名称: {}", dto.getName());
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_ALREADY_EXISTS);
        }
        
        TenantTemplate updateTemplate = TenantTemplateAssembler.assembler(dto);
        updateTemplate.setId(id);
        
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            TenantTemplate defaultTemplate = tenantTemplateManager.findDefaultTemplate();
            if (defaultTemplate != null && !defaultTemplate.getId().equals(id)) {
                log.debug("将原默认模板设置为非默认，模板ID: {}", defaultTemplate.getId());
                defaultTemplate.setIsDefault(false);
                tenantTemplateManager.updateById(defaultTemplate);
            }
        }
        
        tenantTemplateManager.updateById(updateTemplate);
        
        log.info("租户模板更新成功，模板ID: {}, 模板名称: {}", id, dto.getName());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("开始删除租户模板，模板ID: {}", id);
        
        tenantTemplateManager.removeById(id);
        
        log.info("租户模板删除成功，模板ID: {}", id);
    }

    @Override
    public TenantTemplateVO findById(Long id) {
        log.info("查询租户模板详情，模板ID: {}", id);
        
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(id);
        if (tenantTemplate == null) {
            log.warn("租户模板不存在，模板ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        
        TenantTemplateVO vo = TenantTemplateAssembler.assembler(tenantTemplate);
        log.debug("查询租户模板详情成功，模板ID: {}, 模板名称: {}", id, vo.getName());
        return vo;
    }

    @Override
    public ResPage<TenantTemplateVO> page(TenantTemplateQuery query, ReqPage reqPage) {
        log.info("分页查询租户模板列表，页码: {}, 每页大小: {}, 查询条件: {}", 
                reqPage.getPage(), reqPage.getSize(), query);
        
        LambdaQueryWrapper<TenantTemplate> queryWrapper = Wrappers.<TenantTemplate>lambdaQuery()
                .like(null != query.getName(), TenantTemplate::getName, query.getName())
                .eq(null != query.getCode(), TenantTemplate::getCode, query.getCode())
                .eq(null != query.getState(), TenantTemplate::getState, query.getState())
                .eq(null != query.getIsDefault(), TenantTemplate::getIsDefault, query.getIsDefault())
                .ge(null != query.getCreateTimeStart(), TenantTemplate::getCreateTime, query.getCreateTimeStart())
                .le(null != query.getCreateTimeEnd(), TenantTemplate::getCreateTime, query.getCreateTimeEnd())
                .orderByAsc(TenantTemplate::getSort);
        
        IPage<TenantTemplate> page = tenantTemplateManager.page(new Page<>(reqPage.getPage(), reqPage.getSize()), queryWrapper);
        ResPage<TenantTemplateVO> result = ResPageConvert.convert(page, TenantTemplateAssembler::assembler);
        
        log.debug("分页查询租户模板列表成功，总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getSize());
        return result;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        log.info("开始启用租户模板，模板ID: {}", id);
        
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(id);
        if (tenantTemplate == null) {
            log.warn("租户模板不存在，模板ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        
        tenantTemplate.setState(TenantPackageStateEnum.ACTIVE.getCode());
        tenantTemplateManager.updateById(tenantTemplate);
        
        log.info("租户模板启用成功，模板ID: {}, 模板名称: {}", id, tenantTemplate.getName());
    }

    @Override
    @Transactional
    public void disable(Long id) {
        log.info("开始禁用租户模板，模板ID: {}", id);
        
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(id);
        if (tenantTemplate == null) {
            log.warn("租户模板不存在，模板ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        
        tenantTemplate.setState(TenantPackageStateEnum.DISABLED.getCode());
        tenantTemplateManager.updateById(tenantTemplate);
        
        log.info("租户模板禁用成功，模板ID: {}, 模板名称: {}", id, tenantTemplate.getName());
    }

    @Override
    @Transactional
    public void setDefault(Long id) {
        log.info("开始设置租户模板为默认，模板ID: {}", id);
        
        TenantTemplate tenantTemplate = tenantTemplateManager.getById(id);
        if (tenantTemplate == null) {
            log.warn("租户模板不存在，模板ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        
        TenantTemplate defaultTemplate = tenantTemplateManager.findDefaultTemplate();
        if (defaultTemplate != null && !defaultTemplate.getId().equals(id)) {
            log.debug("将原默认模板设置为非默认，模板ID: {}", defaultTemplate.getId());
            defaultTemplate.setIsDefault(false);
            tenantTemplateManager.updateById(defaultTemplate);
        }
        
        tenantTemplate.setIsDefault(true);
        tenantTemplateManager.updateById(tenantTemplate);
        
        log.info("租户模板设置为默认成功，模板ID: {}, 模板名称: {}", id, tenantTemplate.getName());
    }

    @Override
    public TenantTemplateVO getDefault() {
        log.info("查询默认租户模板");
        
        TenantTemplate tenantTemplate = tenantTemplateManager.findDefaultTemplate();
        if (tenantTemplate == null) {
            log.warn("默认租户模板不存在");
            throw new TenantException(TenantExceptionEnum.TENANT_TEMPLATE_NOT_FOUND);
        }
        
        TenantTemplateVO vo = TenantTemplateAssembler.assembler(tenantTemplate);
        log.debug("查询默认租户模板成功，模板ID: {}, 模板名称: {}", vo.getId(), vo.getName());
        return vo;
    }

    @Override
    public List<OptionVO> listSelect() {
        log.info("查询租户模板下拉选项列表");
        
        List<TenantTemplate> templates = tenantTemplateManager.list(Wrappers.<TenantTemplate>lambdaQuery()
                .eq(TenantTemplate::getState, TenantPackageStateEnum.ACTIVE.getCode())
                .orderByAsc(TenantTemplate::getSort));
        
        List<OptionVO> options = templates.stream()
                .map(template -> {
                    OptionVO option = new OptionVO();
                    option.setValue(template.getId().toString());
                    option.setLabel(template.getName());
                    return option;
                })
                .collect(Collectors.toList());
        
        log.debug("查询租户模板下拉选项列表成功，选项数量: {}", options.size());
        return options;
    }


}