package cn.structured.tenant.service.impl;

import cn.structure.common.vo.OptionVO;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.mybatis.plus.starter.convert.ResPageConvert;
import cn.structured.tenant.assembler.TenantPackageAssembler;
import cn.structured.tenant.dto.TenantPackageDTO;
import cn.structured.tenant.entity.TenantPackage;
import cn.structured.tenant.enums.TenantExceptionEnum;
import cn.structured.tenant.enums.TenantPackageStateEnum;
import cn.structured.tenant.exception.TenantException;
import cn.structured.tenant.manager.ITenantPackageManager;
import cn.structured.tenant.query.TenantPackageQuery;
import cn.structured.tenant.service.ITenantPackageService;
import cn.structured.tenant.vo.TenantPackageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TenantPackageServiceImpl implements ITenantPackageService {

    private final ITenantPackageManager tenantPackageManager;

    @Override
    @Transactional
    public Long create(TenantPackageDTO dto) {
        if (dto.getCode() != null && tenantPackageManager.existsByCode(dto.getCode())) {
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_CODE_DUPLICATE);
        }
        if (tenantPackageManager.existsByName(dto.getName())) {
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_ALREADY_EXISTS);
        }
        TenantPackage tenantPackage = TenantPackageAssembler.assembler(dto);
        tenantPackageManager.save(tenantPackage);
        return tenantPackage.getId();
    }

    @Override
    @Transactional
    public void update(Long id, TenantPackageDTO dto) {
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        if (dto.getCode() != null && !dto.getCode().equals(tenantPackage.getCode()) && tenantPackageManager.existsByCode(dto.getCode())) {
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_CODE_DUPLICATE);
        }
        if (!dto.getName().equals(tenantPackage.getName()) && tenantPackageManager.existsByName(dto.getName())) {
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_ALREADY_EXISTS);
        }
        TenantPackage updatePackage = TenantPackageAssembler.assembler(dto);
        updatePackage.setId(id);
        tenantPackageManager.updateById(updatePackage);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        tenantPackageManager.removeById(id);
    }

    @Override
    public TenantPackageVO findById(Long id) {
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        return TenantPackageAssembler.assembler(tenantPackage);
    }

    @Override
    public ResPage<TenantPackageVO> page(TenantPackageQuery query, ReqPage reqPage) {
        // 构建查询条件
        LambdaQueryWrapper<TenantPackage> queryWrapper = Wrappers.<TenantPackage>lambdaQuery()
                .like(null != query.getName(), TenantPackage::getName, query.getName())
                .eq(null != query.getCode(), TenantPackage::getCode, query.getCode())
                .eq(null != query.getState(), TenantPackage::getState, query.getState())
                .ge(null != query.getCreateTimeStart(), TenantPackage::getCreateTime, query.getCreateTimeStart())
                .le(null != query.getCreateTimeEnd(), TenantPackage::getCreateTime, query.getCreateTimeEnd())
                .orderByAsc(TenantPackage::getSort);
        IPage<TenantPackage> page = tenantPackageManager.page(new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize()), queryWrapper);
        return ResPageConvert.convert(page, TenantPackageAssembler::assembler);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        tenantPackage.setState(TenantPackageStateEnum.ACTIVE.getCode());
        tenantPackageManager.updateById(tenantPackage);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        tenantPackage.setState(TenantPackageStateEnum.DISABLED.getCode());
        tenantPackageManager.updateById(tenantPackage);
    }

    @Override
    public List<OptionVO> listSelect() {
        return List.of();
    }


}