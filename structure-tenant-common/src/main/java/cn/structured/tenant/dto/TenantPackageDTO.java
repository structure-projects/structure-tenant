package cn.structured.tenant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "租户套餐DTO")
public class TenantPackageDTO {

    @Schema(description = "套餐名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "套餐名称不能为空")
    private String name;

    @Schema(description = "套餐编码")
    private String code;

    @Schema(description = "套餐描述")
    private String description;

    @Schema(description = "套餐状态 0:草稿 1:启用 2:禁用", defaultValue = "0")
    private Integer state;

    @Schema(description = "最大用户数")
    private Integer maxUserCount;

    @Schema(description = "最大存储空间(GB)")
    private Integer maxStorageGB;

    @Schema(description = "最大部门数")
    private Integer maxDeptCount;

    @Schema(description = "是否支持自定义字段")
    private Boolean customFieldEnabled;

    @Schema(description = "是否支持API接口")
    private Boolean apiEnabled;

    @Schema(description = "排序号")
    private Integer sort;
}