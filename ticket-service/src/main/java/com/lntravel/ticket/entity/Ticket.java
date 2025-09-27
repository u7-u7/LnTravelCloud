package com.lntravel.ticket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门票实体类
 */
@Data
@TableName("ticket")
public class Ticket {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long scenicId;
    
    private String name;
    
    private String type;
    
    private BigDecimal price;
    
    private BigDecimal originalPrice;
    
    private String description;
    
    private Integer validDays;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
