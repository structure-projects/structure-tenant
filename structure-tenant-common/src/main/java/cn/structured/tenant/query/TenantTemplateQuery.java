package cn.structured.tenant.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "租户模板查询条件")
public class TenantTemplateQuery {

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "模板编码")
    private String code;

    @Schema(description = "模板状态")
    private Integer state;

    @Schema(description = "是否默认模板")
    private Boolean isDefault;

    @Schema(description = "创建时间开始")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束")
    private LocalDateTime createTimeEnd;
}