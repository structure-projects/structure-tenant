package cn.structured.tenant.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "租户套餐查询条件")
public class TenantPackageQuery {

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "套餐编码")
    private String code;

    @Schema(description = "套餐状态")
    private Integer state;

    @Schema(description = "创建时间开始")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束")
    private LocalDateTime createTimeEnd;
}