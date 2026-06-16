package cn.structured.tenant.assembler;

import cn.structured.tenant.dto.TenantTemplateDTO;
import cn.structured.tenant.entity.TenantTemplate;
import cn.structured.tenant.enums.TenantPackageStateEnum;
import cn.structured.tenant.vo.TenantTemplateVO;

public class TenantTemplateAssembler {

    private TenantTemplateAssembler() {
    }

    public static TenantTemplateVO assembler(TenantTemplate tenantTemplate) {
        if (tenantTemplate == null) {
            return null;
        }
        TenantTemplateVO vo = new TenantTemplateVO();
        vo.setId(tenantTemplate.getId());
        vo.setName(tenantTemplate.getName());
        vo.setCode(tenantTemplate.getCode());
        vo.setDescription(tenantTemplate.getDescription());
        vo.setState(tenantTemplate.getState());
        if (tenantTemplate.getState() != null) {
            TenantPackageStateEnum stateEnum = TenantPackageStateEnum.fromCode(tenantTemplate.getState());
            vo.setStateDescription(stateEnum != null ? stateEnum.getDescription() : null);
        }
        vo.setConfig(tenantTemplate.getConfig());
        vo.setIsDefault(tenantTemplate.getIsDefault());
        vo.setSort(tenantTemplate.getSort());
        vo.setCreateTime(tenantTemplate.getCreateTime());
        vo.setCreateBy(tenantTemplate.getCreateBy());
        vo.setUpdateTime(tenantTemplate.getUpdateTime());
        vo.setUpdateBy(tenantTemplate.getUpdateBy());
        return vo;
    }

    public static TenantTemplate assembler(TenantTemplateDTO dto) {
        if (dto == null) {
            return null;
        }
        TenantTemplate tenantTemplate = new TenantTemplate();
        tenantTemplate.setName(dto.getName());
        tenantTemplate.setCode(dto.getCode());
        tenantTemplate.setDescription(dto.getDescription());
        tenantTemplate.setState(dto.getState());
        tenantTemplate.setConfig(dto.getConfig());
        tenantTemplate.setIsDefault(dto.getIsDefault());
        tenantTemplate.setSort(dto.getSort());
        return tenantTemplate;
    }
}