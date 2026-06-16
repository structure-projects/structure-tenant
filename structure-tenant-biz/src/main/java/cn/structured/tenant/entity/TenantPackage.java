package cn.structured.tenant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tenant_package")
public class TenantPackage {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("description")
    private String description;

    @TableField("state")
    private Integer state;

    @TableField("max_user_count")
    private Integer maxUserCount;

    @TableField("max_storage_gb")
    private Integer maxStorageGB;

    @TableField("max_dept_count")
    private Integer maxDeptCount;

    @TableField("custom_field_enabled")
    private Boolean customFieldEnabled;

    @TableField("api_enabled")
    private Boolean apiEnabled;

    @TableField("sort")
    private Integer sort;

    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}