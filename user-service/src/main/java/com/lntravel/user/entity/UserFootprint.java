package com.lntravel.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户足迹实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_footprint")
public class UserFootprint {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("scenic_id")
    private Long scenicId;
    
    @TableField("visit_date")
    private LocalDate visitDate;
    
    @TableField(value = "visit_time", fill = FieldFill.INSERT)
    private LocalDateTime visitTime;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
