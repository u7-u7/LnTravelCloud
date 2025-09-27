package com.lntravel.ticket.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lntravel.ticket.entity.SeckillActivity;
import com.lntravel.ticket.entity.Ticket;

import java.util.List;

/**
 * 门票服务接口
 */
public interface TicketService {
    
    /**
     * 根据景区ID获取门票列表
     */
    List<Ticket> getTicketsByScenicId(Long scenicId);
    
    /**
     * 获取门票详情
     */
    Ticket getTicketDetail(Long ticketId);
    
    /**
     * 获取秒杀活动列表
     */
    IPage<SeckillActivity> getSeckillActivities(Integer page, Integer size);
    
    /**
     * 获取正在进行的秒杀活动
     */
    List<SeckillActivity> getActiveActivities();
    
    /**
     * 根据门票ID获取秒杀活动
     */
    List<SeckillActivity> getSeckillByTicketId(Long ticketId);
    
    /**
     * 参与秒杀
     */
    Boolean participateSeckill(Long activityId, Long userId, Integer quantity);
    
    /**
     * 检查秒杀库存
     */
    Boolean checkSeckillStock(Long activityId, Integer quantity);
}
