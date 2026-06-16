package cn.structured.tenant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "租户DTO")
public class TenantDTO {

    @Schema(description = "租户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "租户名称不能为空")
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

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "租户状态 0:初始化 1:正常 2:停用 3:冻结 4:已过期", defaultValue = "0")
    private Integer state;

    @Schema(description = "有效期开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "有效期结束时间")
    private LocalDateTime validEndTime;
}