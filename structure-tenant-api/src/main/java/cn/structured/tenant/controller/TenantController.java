package cn.structured.tenant.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.tenant.dto.TenantDTO;
import cn.structured.tenant.query.TenantQuery;
import cn.structured.tenant.service.ITenantService;
import cn.structured.tenant.vo.TenantVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户管理", description = "租户生命周期管理接口")
@RestController
@RequestMapping("/api/tenant")
@AllArgsConstructor
public class TenantController {

    private final ITenantService tenantService;

    @Operation(summary = "创建租户")
    @PostMapping
    public ResResultVO<Long> create(@Valid @RequestBody TenantDTO dto) {
        Long id = tenantService.create(dto);
        return ResultUtilSimpleImpl.success(id);
    }

    @Operation(summary = "更新租户")
    @PutMapping("/{id}")
    public ResResultVO<Void> update(
            @Parameter(description = "租户ID") @PathVariable Long id,
            @Valid @RequestBody TenantDTO dto) {
        tenantService.update(id, dto);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "删除租户")
    @DeleteMapping("/{id}")
    public ResResultVO<Void> delete(@Parameter(description = "租户ID") @PathVariable Long id) {
        tenantService.delete(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "查询租户详情")
    @GetMapping("/{id}")
    public ResResultVO<TenantVO> findById(@Parameter(description = "租户ID") @PathVariable Long id) {
        return ResultUtilSimpleImpl.success(tenantService.findById(id));
    }

    @Operation(summary = "分页查询租户列表")
    @GetMapping("/page")
    public ResResultVO<ResPage<TenantVO>> page(TenantQuery query, ReqPage reqPage) {
        return ResultUtilSimpleImpl.success(tenantService.page(query, reqPage));
    }

    @Operation(summary = "启用租户")
    @PutMapping("/{id}/activate")
    public ResResultVO<Void> activate(@Parameter(description = "租户ID") @PathVariable Long id) {
        tenantService.activate(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "停用租户")
    @PutMapping("/{id}/suspend")
    public ResResultVO<Void> suspend(@Parameter(description = "租户ID") @PathVariable Long id) {
        tenantService.suspend(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "冻结租户")
    @PutMapping("/{id}/freeze")
    public ResResultVO<Void> freeze(@Parameter(description = "租户ID") @PathVariable Long id) {
        tenantService.freeze(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "过期租户")
    @PutMapping("/{id}/expire")
    public ResResultVO<Void> expire(@Parameter(description = "租户ID") @PathVariable Long id) {
        tenantService.expire(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @Operation(summary = "应用模板")
    @PutMapping("/{tenantId}/template/{templateId}")
    public ResResultVO<Void> applyTemplate(
            @Parameter(description = "租户ID") @PathVariable Long tenantId,
            @Parameter(description = "模板ID") @PathVariable Long templateId) {
        tenantService.applyTemplate(tenantId, templateId);
        return ResultUtilSimpleImpl.success(null);
    }
}