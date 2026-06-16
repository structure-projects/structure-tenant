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
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TenantPackageServiceImpl implements ITenantPackageService {

    private final ITenantPackageManager tenantPackageManager;

    @Override
    @Transactional
    public Long create(TenantPackageDTO dto) {
        log.info("开始创建租户套餐，套餐名称: {}, 套餐编码: {}", dto.getName(), dto.getCode());
        
        if (dto.getCode() != null && tenantPackageManager.existsByCode(dto.getCode())) {
            log.warn("租户套餐编码重复，编码: {}", dto.getCode());
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_CODE_DUPLICATE);
        }
        if (tenantPackageManager.existsByName(dto.getName())) {
            log.warn("租户套餐名称重复，名称: {}", dto.getName());
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_ALREADY_EXISTS);
        }
        
        TenantPackage tenantPackage = TenantPackageAssembler.assembler(dto);
        tenantPackageManager.save(tenantPackage);
        
        log.info("租户套餐创建成功，套餐ID: {}, 套餐名称: {}", tenantPackage.getId(), tenantPackage.getName());
        return tenantPackage.getId();
    }

    @Override
    @Transactional
    public void update(Long id, TenantPackageDTO dto) {
        log.info("开始更新租户套餐，套餐ID: {}, 新套餐名称: {}", id, dto.getName());
        
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            log.warn("租户套餐不存在，套餐ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        
        if (dto.getCode() != null && !dto.getCode().equals(tenantPackage.getCode()) && tenantPackageManager.existsByCode(dto.getCode())) {
            log.warn("租户套餐编码重复，编码: {}", dto.getCode());
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_CODE_DUPLICATE);
        }
        if (!dto.getName().equals(tenantPackage.getName()) && tenantPackageManager.existsByName(dto.getName())) {
            log.warn("租户套餐名称重复，名称: {}", dto.getName());
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_ALREADY_EXISTS);
        }
        
        TenantPackage updatePackage = TenantPackageAssembler.assembler(dto);
        updatePackage.setId(id);
        tenantPackageManager.updateById(updatePackage);
        
        log.info("租户套餐更新成功，套餐ID: {}, 套餐名称: {}", id, dto.getName());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("开始删除租户套餐，套餐ID: {}", id);
        
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            log.warn("租户套餐不存在，套餐ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        
        tenantPackageManager.removeById(id);
        
        log.info("租户套餐删除成功，套餐ID: {}, 套餐名称: {}", id, tenantPackage.getName());
    }

    @Override
    public TenantPackageVO findById(Long id) {
        log.info("查询租户套餐详情，套餐ID: {}", id);
        
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            log.warn("租户套餐不存在，套餐ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        
        TenantPackageVO vo = TenantPackageAssembler.assembler(tenantPackage);
        log.debug("查询租户套餐详情成功，套餐ID: {}, 套餐名称: {}", id, vo.getName());
        return vo;
    }

    @Override
    public ResPage<TenantPackageVO> page(TenantPackageQuery query, ReqPage reqPage) {
        log.info("分页查询租户套餐列表，页码: {}, 每页大小: {}, 查询条件: {}", 
                reqPage.getCurrentPage(), reqPage.getPageSize(), query);
        
        LambdaQueryWrapper<TenantPackage> queryWrapper = Wrappers.<TenantPackage>lambdaQuery()
                .like(null != query.getName(), TenantPackage::getName, query.getName())
                .eq(null != query.getCode(), TenantPackage::getCode, query.getCode())
                .eq(null != query.getState(), TenantPackage::getState, query.getState())
                .ge(null != query.getCreateTimeStart(), TenantPackage::getCreateTime, query.getCreateTimeStart())
                .le(null != query.getCreateTimeEnd(), TenantPackage::getCreateTime, query.getCreateTimeEnd())
                .orderByAsc(TenantPackage::getSort);
        
        IPage<TenantPackage> page = tenantPackageManager.page(new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize()), queryWrapper);
        ResPage<TenantPackageVO> result = ResPageConvert.convert(page, TenantPackageAssembler::assembler);
        
        log.debug("分页查询租户套餐列表成功，总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getSize());
        return result;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        log.info("开始启用租户套餐，套餐ID: {}", id);
        
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            log.warn("租户套餐不存在，套餐ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        
        tenantPackage.setState(TenantPackageStateEnum.ACTIVE.getCode());
        tenantPackageManager.updateById(tenantPackage);
        
        log.info("租户套餐启用成功，套餐ID: {}, 套餐名称: {}", id, tenantPackage.getName());
    }

    @Override
    @Transactional
    public void disable(Long id) {
        log.info("开始禁用租户套餐，套餐ID: {}", id);
        
        TenantPackage tenantPackage = tenantPackageManager.getById(id);
        if (tenantPackage == null) {
            log.warn("租户套餐不存在，套餐ID: {}", id);
            throw new TenantException(TenantExceptionEnum.TENANT_PACKAGE_NOT_FOUND);
        }
        
        tenantPackage.setState(TenantPackageStateEnum.DISABLED.getCode());
        tenantPackageManager.updateById(tenantPackage);
        
        log.info("租户套餐禁用成功，套餐ID: {}, 套餐名称: {}", id, tenantPackage.getName());
    }

    @Override
    public List<OptionVO> listSelect() {
        log.info("查询租户套餐下拉选项列表");
        
        List<TenantPackage> packages = tenantPackageManager.list(Wrappers.<TenantPackage>lambdaQuery()
                .eq(TenantPackage::getState, TenantPackageStateEnum.ACTIVE.getCode())
                .orderByAsc(TenantPackage::getSort));
        
        List<OptionVO> options = packages.stream()
                .map(pkg -> {
                    OptionVO option = new OptionVO();
                    option.setValue(pkg.getId().toString());
                    option.setLabel(pkg.getName());
                    return option;
                })
                .collect(Collectors.toList());
        
        log.debug("查询租户套餐下拉选项列表成功，选项数量: {}", options.size());
        return options;
    }


}