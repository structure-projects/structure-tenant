package cn.structured.tenant.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.common.vo.OptionVO;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.tenant.dto.TenantTemplateDTO;
import cn.structured.tenant.query.TenantTemplateQuery;
import cn.structured.tenant.service.ITenantTemplateService;
import cn.structured.tenant.vo.TenantTemplateVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "租户模板管理", description = "租户模板管理接口")
@RestController
@RequestMapping("/api/tenant-template")
@AllArgsConstructor
public class TenantTemplateController {

    private final ITenantTemplateService tenantTemplateService;

    @Operation(summary = "创建模板")
    @PostMapping
    public ResResultVO<Long> create(@Valid @RequestBody TenantTemplateDTO dto) {
        Long id = tenantTemplateService.create(dto);
        return ResultUtilSimpleImpl.success(id);
    }

    @Operation(summary = "更新模板")
    @PutMapping("/{id}")
    public ResResultVO<Void> update(
            @Parameter(description = "模板ID") @PathVariable Long id,
            @Valid @RequestBody TenantTemplateDTO dto) {
        tenantTemplateService.update(id, dto);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "删除模板")
    @DeleteMapping("/{id}")
    public ResResultVO<Void> delete(@Parameter(description = "模板ID") @PathVariable Long id) {
        tenantTemplateService.delete(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "查询模板详情")
    @GetMapping("/{id}")
    public ResResultVO<TenantTemplateVO> findById(@Parameter(description = "模板ID") @PathVariable Long id) {
        return ResultUtilSimpleImpl.success(tenantTemplateService.findById(id));
    }

    @Operation(summary = "分页查询模板列表")
    @GetMapping("/page")
    public ResResultVO<ResPage<TenantTemplateVO>> page(TenantTemplateQuery query, ReqPage reqPage) {
        return ResultUtilSimpleImpl.success(tenantTemplateService.page(query, reqPage));
    }

    @Operation(summary = "启用模板")
    @PutMapping("/{id}/enable")
    public ResResultVO<Void> enable(@Parameter(description = "模板ID") @PathVariable Long id) {
        tenantTemplateService.enable(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "禁用模板")
    @PutMapping("/{id}/disable")
    public ResResultVO<Void> disable(@Parameter(description = "模板ID") @PathVariable Long id) {
        tenantTemplateService.disable(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "设为默认模板")
    @PutMapping("/{id}/default")
    public ResResultVO<Void> setDefault(@Parameter(description = "模板ID") @PathVariable Long id) {
        tenantTemplateService.setDefault(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "获取默认模板")
    @GetMapping("/default")
    public ResResultVO<TenantTemplateVO> getDefault() {
        return ResultUtilSimpleImpl.success(tenantTemplateService.getDefault());
    }

    @Operation(summary = "模板下拉选择")
    @GetMapping("/options")
    public ResResultVO<List<OptionVO>> listSelect() {
        return ResultUtilSimpleImpl.success(tenantTemplateService.listSelect());
    }
}