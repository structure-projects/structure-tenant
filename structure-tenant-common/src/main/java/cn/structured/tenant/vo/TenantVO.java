package cn.structured.tenant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "租户VO")
public class TenantVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "租户名称")
    private String name;

    @Schema(description = "租户编码")
    private String code;

    @Schema(description = "租户logo")
    private String logo;

    @Schema(description = "租户描述")
    private String description;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "租户地址")
    private String address;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "套餐ID")
    private Long packageId;

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "租户状态 0:初始化 1:正常 2:停用 3:冻结 4:已过期")
    private Integer state;

    @Schema(description = "租户状态描述")
    private String stateDescription;

    @Schema(description = "有效期开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "有效期结束时间")
    private LocalDateTime validEndTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private Long updateBy;
}