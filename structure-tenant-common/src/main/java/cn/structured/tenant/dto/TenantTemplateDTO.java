package cn.structured.tenant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "租户模板DTO")
public class TenantTemplateDTO {

    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "模板名称不能为空")
    private String name;

    @Schema(description = "模板编码")
    private String code;

    @Schema(description = "模板描述")
    private String description;

    @Schema(description = "模板状态 0:草稿 1:启用 2:禁用", defaultValue = "0")
    private Integer state;

    @Schema(description = "模板配置(JSON格式)")
    private String config;

    @Schema(description = "是否默认模板")
    private Boolean isDefault;

    @Schema(description = "排序号")
    private Integer sort;
}