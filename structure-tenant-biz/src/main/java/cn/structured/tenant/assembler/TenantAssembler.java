package cn.structured.tenant.assembler;

import cn.structured.tenant.dto.TenantDTO;
import cn.structured.tenant.entity.Tenant;
import cn.structured.tenant.enums.TenantStateEnum;
import cn.structured.tenant.vo.TenantVO;

public class TenantAssembler {

    private TenantAssembler() {
    }

    public static TenantVO assembler(Tenant tenant) {
        if (tenant == null) {
            return null;
        }
        TenantVO vo = new TenantVO();
        vo.setId(tenant.getId());
        vo.setName(tenant.getName());
        vo.setCode(tenant.getCode());
        vo.setLogo(tenant.getLogo());
        vo.setDescription(tenant.getDescription());
        vo.setIndustry(tenant.getIndustry());
        vo.setAddress(tenant.getAddress());
        vo.setContactPhone(tenant.getContactPhone());
        vo.setPackageId(tenant.getPackageId());
        vo.setTemplateId(tenant.getTemplateId());
        vo.setState(tenant.getState());
        if (tenant.getState() != null) {
            TenantStateEnum stateEnum = TenantStateEnum.fromCode(tenant.getState());
            vo.setStateDescription(stateEnum != null ? stateEnum.getDescription() : null);
        }
        vo.setValidStartTime(tenant.getValidStartTime());
        vo.setValidEndTime(tenant.getValidEndTime());
        vo.setCreateTime(tenant.getCreateTime());
        vo.setCreateBy(tenant.getCreateBy());
        vo.setUpdateTime(tenant.getUpdateTime());
        vo.setUpdateBy(tenant.getUpdateBy());
        return vo;
    }

    public static Tenant assembler(TenantDTO dto) {
        if (dto == null) {
            return null;
        }
        Tenant tenant = new Tenant();
        tenant.setName(dto.getName());
        tenant.setCode(dto.getCode());
        tenant.setLogo(dto.getLogo());
        tenant.setDescription(dto.getDescription());
        tenant.setIndustry(dto.getIndustry());
        tenant.setAddress(dto.getAddress());
        tenant.setContactPhone(dto.getContactPhone());
        tenant.setPackageId(dto.getPackageId());
        tenant.setTemplateId(dto.getTemplateId());
        tenant.setState(dto.getState());
        tenant.setValidStartTime(dto.getValidStartTime());
        tenant.setValidEndTime(dto.getValidEndTime());
        return tenant;
    }
}