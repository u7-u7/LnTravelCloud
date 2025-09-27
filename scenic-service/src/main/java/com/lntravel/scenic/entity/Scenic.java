package com.lntravel.scenic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 景区实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("scenic")
public class Scenic {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("name")
    private String name;
    
    @TableField("category_id")
    private Long categoryId;
    
    @TableField("province")
    private String province;
    
    @TableField("city")
    private String city;
    
    @TableField("district")
    private String district;
    
    @TableField("address")
    private String address;
    
    @TableField("longitude")
    private BigDecimal longitude;
    
    @TableField("latitude")
    private BigDecimal latitude;
    
    @TableField("phone")
    private String phone;
    
    @TableField("open_time")
    private String openTime;
    
    @TableField("description")
    private String description;
    
    @TableField("facilities")
    private String facilities;
    
    @TableField("images")
    private String images;
    
    @TableField("rating")
    private BigDecimal rating;
    
    @TableField("visit_count")
    private Integer visitCount;
    
    @TableField("status")
    private Integer status;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
