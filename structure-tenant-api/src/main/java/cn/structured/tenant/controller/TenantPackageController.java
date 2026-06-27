package cn.structured.tenant.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.common.vo.OptionVO;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.tenant.dto.TenantPackageDTO;
import cn.structured.tenant.query.TenantPackageQuery;
import cn.structured.tenant.service.ITenantPackageService;
import cn.structured.tenant.vo.TenantPackageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "租户套餐管理", description = "租户套餐管理接口")
@RestController
@RequestMapping("/api/tenant-package")
@AllArgsConstructor
public class TenantPackageController {

    private final ITenantPackageService tenantPackageService;

    @Operation(summary = "创建套餐")
    @PostMapping
    public ResResultVO<Long> create(@Valid @RequestBody TenantPackageDTO dto) {
        Long id = tenantPackageService.create(dto);
        return ResultUtilSimpleImpl.success(id);
    }

    @Operation(summary = "更新套餐")
    @PutMapping("/{id}")
    public ResResultVO<Void> update(
            @Parameter(description = "套餐ID") @PathVariable Long id,
            @Valid @RequestBody TenantPackageDTO dto) {
        tenantPackageService.update(id, dto);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "删除套餐")
    @DeleteMapping("/{id}")
    public ResResultVO<Void> delete(@Parameter(description = "套餐ID") @PathVariable Long id) {
        tenantPackageService.delete(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "查询套餐详情")
    @GetMapping("/{id}")
    public ResResultVO<TenantPackageVO> findById(@Parameter(description = "套餐ID") @PathVariable Long id) {
        return ResultUtilSimpleImpl.success(tenantPackageService.findById(id));
    }

    @Operation(summary = "分页查询套餐列表")
    @GetMapping("/page")
    public ResResultVO<ResPage<TenantPackageVO>> page(TenantPackageQuery query, ReqPage reqPage) {
        return ResultUtilSimpleImpl.success(tenantPackageService.page(query, reqPage));
    }

    @Operation(summary = "启用套餐")
    @PutMapping("/{id}/enable")
    public ResResultVO<Void> enable(@Parameter(description = "套餐ID") @PathVariable Long id) {
        tenantPackageService.enable(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "禁用套餐")
    @PutMapping("/{id}/disable")
    public ResResultVO<Void> disable(@Parameter(description = "套餐ID") @PathVariable Long id) {
        tenantPackageService.disable(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "套餐下拉选择")
    @GetMapping("/options")
    public ResResultVO<List<OptionVO>> listSelect() {
        return ResultUtilSimpleImpl.success(tenantPackageService.listSelect());
    }
}