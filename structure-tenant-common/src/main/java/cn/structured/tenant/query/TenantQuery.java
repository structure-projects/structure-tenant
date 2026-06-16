package cn.structured.tenant.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "租户查询条件")
public class TenantQuery {

    @Schema(description = "租户名称")
    private String name;

    @Schema(description = "租户编码")
    private String code;

    @Schema(description = "租户状态")
    private Integer state;

    @Schema(description = "套餐ID")
    private Long packageId;

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "创建时间开始")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束")
    private LocalDateTime createTimeEnd;
}