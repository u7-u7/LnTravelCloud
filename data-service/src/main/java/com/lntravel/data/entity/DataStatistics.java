package com.lntravel.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据统计实体类
 */
@Data
@TableName("data_statistics")
public class DataStatistics {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private LocalDate statDate;
    
    private String statType;
    
    private Long scenicId;
    
    private String province;
    
    private String city;
    
    private Integer visitCount;
    
    private Integer userCount;
    
    private Integer orderCount;
    
    private BigDecimal orderAmount;
    
    private Integer commentCount;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
