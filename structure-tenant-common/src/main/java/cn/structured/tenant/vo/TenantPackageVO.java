package cn.structured.tenant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "租户套餐VO")
public class TenantPackageVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "套餐编码")
    private String code;

    @Schema(description = "套餐描述")
    private String description;

    @Schema(description = "套餐状态 0:草稿 1:启用 2:禁用")
    private Integer state;

    @Schema(description = "套餐状态描述")
    private String stateDescription;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private Long updateBy;
}