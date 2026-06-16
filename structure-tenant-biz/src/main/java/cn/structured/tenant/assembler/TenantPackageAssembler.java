package cn.structured.tenant.assembler;

import cn.structured.tenant.dto.TenantPackageDTO;
import cn.structured.tenant.entity.TenantPackage;
import cn.structured.tenant.enums.TenantPackageStateEnum;
import cn.structured.tenant.vo.TenantPackageVO;

public class TenantPackageAssembler {

    private TenantPackageAssembler() {
    }

    public static TenantPackageVO assembler(TenantPackage tenantPackage) {
        if (tenantPackage == null) {
            return null;
        }
        TenantPackageVO vo = new TenantPackageVO();
        vo.setId(tenantPackage.getId());
        vo.setName(tenantPackage.getName());
        vo.setCode(tenantPackage.getCode());
        vo.setDescription(tenantPackage.getDescription());
        vo.setState(tenantPackage.getState());
        if (tenantPackage.getState() != null) {
            TenantPackageStateEnum stateEnum = TenantPackageStateEnum.fromCode(tenantPackage.getState());
            vo.setStateDescription(stateEnum != null ? stateEnum.getDescription() : null);
        }
        vo.setMaxUserCount(tenantPackage.getMaxUserCount());
        vo.setMaxStorageGB(tenantPackage.getMaxStorageGB());
        vo.setMaxDeptCount(tenantPackage.getMaxDeptCount());
        vo.setCustomFieldEnabled(tenantPackage.getCustomFieldEnabled());
        vo.setApiEnabled(tenantPackage.getApiEnabled());
        vo.setSort(tenantPackage.getSort());
        vo.setCreateTime(tenantPackage.getCreateTime());
        vo.setCreateBy(tenantPackage.getCreateBy());
        vo.setUpdateTime(tenantPackage.getUpdateTime());
        vo.setUpdateBy(tenantPackage.getUpdateBy());
        return vo;
    }

    public static TenantPackage assembler(TenantPackageDTO dto) {
        if (dto == null) {
            return null;
        }
        TenantPackage tenantPackage = new TenantPackage();
        tenantPackage.setName(dto.getName());
        tenantPackage.setCode(dto.getCode());
        tenantPackage.setDescription(dto.getDescription());
        tenantPackage.setState(dto.getState());
        tenantPackage.setMaxUserCount(dto.getMaxUserCount());
        tenantPackage.setMaxStorageGB(dto.getMaxStorageGB());
        tenantPackage.setMaxDeptCount(dto.getMaxDeptCount());
        tenantPackage.setCustomFieldEnabled(dto.getCustomFieldEnabled());
        tenantPackage.setApiEnabled(dto.getApiEnabled());
        tenantPackage.setSort(dto.getSort());
        return tenantPackage;
    }
}