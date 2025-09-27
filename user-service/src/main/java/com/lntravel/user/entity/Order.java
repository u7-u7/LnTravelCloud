package com.lntravel.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("`order`")
public class Order {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("order_no")
    private String orderNo;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("scenic_id")
    private Long scenicId;
    
    @TableField("ticket_id")
    private Long ticketId;
    
    @TableField("quantity")
    private Integer quantity;
    
    @TableField("unit_price")
    private BigDecimal unitPrice;
    
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    @TableField("visit_date")
    private LocalDate visitDate;
    
    @TableField("visitor_info")
    private String visitorInfo;
    
    @TableField("status")
    private String status;
    
    @TableField("pay_time")
    private LocalDateTime payTime;
    
    @TableField("use_time")
    private LocalDateTime useTime;
    
    @TableField("cancel_time")
    private LocalDateTime cancelTime;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
