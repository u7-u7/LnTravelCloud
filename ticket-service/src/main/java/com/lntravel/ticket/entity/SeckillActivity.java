package com.lntravel.ticket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀活动实体类
 */
@Data
@TableName("seckill_activity")
public class SeckillActivity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long ticketId;
    
    private String title;
    
    private BigDecimal seckillPrice;
    
    private Integer stock;
    
    private Integer sold;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
