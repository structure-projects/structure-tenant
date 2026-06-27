package cn.structured.tenant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tenant")
public class Tenant {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("logo")
    private String logo;

    @TableField("description")
    private String description;

    @TableField("industry")
    private String industry;

    @TableField("address")
    private String address;

    @TableField("contact_phone")
    private String contactPhone;

    @TableField("package_id")
    private Long packageId;

    @TableField("template_id")
    private Long templateId;

    @TableField("state")
    private Integer state;

    @TableField("valid_start_time")
    private LocalDateTime validStartTime;

    @TableField("valid_end_time")
    private LocalDateTime validEndTime;

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