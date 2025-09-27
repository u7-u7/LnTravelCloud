package com.lntravel.ticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lntravel.ticket.entity.SeckillActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 秒杀活动Mapper
 */
@Mapper
public interface SeckillActivityMapper extends BaseMapper<SeckillActivity> {
    
    /**
     * 查询正在进行的秒杀活动
     */
    List<SeckillActivity> selectActiveActivities(@Param("now") LocalDateTime now);
    
    /**
     * 根据门票ID查询秒杀活动
     */
    List<SeckillActivity> selectByTicketId(@Param("ticketId") Long ticketId);
    
    /**
     * 更新秒杀活动销量
     */
    int updateSold(@Param("id") Long id, @Param("quantity") Integer quantity);
}
