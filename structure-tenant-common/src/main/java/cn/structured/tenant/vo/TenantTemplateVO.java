package cn.structured.tenant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "租户模板VO")
public class TenantTemplateVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "模板编码")
    private String code;

    @Schema(description = "模板描述")
    private String description;

    @Schema(description = "模板状态 0:草稿 1:启用 2:禁用")
    private Integer state;

    @Schema(description = "模板状态描述")
    private String stateDescription;

    @Schema(description = "模板配置(JSON格式)")
    private String config;

    @Schema(description = "是否默认模板")
    private Boolean isDefault;

    @Schema(description = "排序号")
    private Integer sort;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private Long updateBy;
}