package com.lntravel.ticket.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lntravel.common.exception.BusinessException;
import com.lntravel.ticket.entity.SeckillActivity;
import com.lntravel.ticket.entity.Ticket;
import com.lntravel.ticket.mapper.SeckillActivityMapper;
import com.lntravel.ticket.mapper.TicketMapper;
import com.lntravel.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 门票服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    
    private final TicketMapper ticketMapper;
    private final SeckillActivityMapper seckillActivityMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public List<Ticket> getTicketsByScenicId(Long scenicId) {
        return ticketMapper.selectByScenicId(scenicId);
    }
    
    @Override
    public Ticket getTicketDetail(Long ticketId) {
        Ticket ticket = ticketMapper.selectById(ticketId);
        if (ticket == null) {
            throw new BusinessException("门票不存在");
        }
        return ticket;
    }
    
    @Override
    public IPage<SeckillActivity> getSeckillActivities(Integer page, Integer size) {
        Page<SeckillActivity> pageParam = new Page<>(page, size);
        return seckillActivityMapper.selectPage(pageParam, null);
    }
    
    @Override
    public List<SeckillActivity> getActiveActivities() {
        return seckillActivityMapper.selectActiveActivities(LocalDateTime.now());
    }
    
    @Override
    public List<SeckillActivity> getSeckillByTicketId(Long ticketId) {
        return seckillActivityMapper.selectByTicketId(ticketId);
    }
    
    @Override
    @Transactional
    public Boolean participateSeckill(Long activityId, Long userId, Integer quantity) {
        // 检查库存
        if (!checkSeckillStock(activityId, quantity)) {
            throw new BusinessException("库存不足");
        }
        
        // 使用Redis实现分布式锁防止超卖
        String lockKey = "seckill:lock:" + activityId;
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        
        if (!locked) {
            throw new BusinessException("系统繁忙，请稍后再试");
        }
        
        try {
            // 再次检查库存
            SeckillActivity activity = seckillActivityMapper.selectById(activityId);
            if (activity == null || activity.getStock() - activity.getSold() < quantity) {
                throw new BusinessException("库存不足");
            }
            
            // 更新销量
            int result = seckillActivityMapper.updateSold(activityId, quantity);
            if (result <= 0) {
                throw new BusinessException("秒杀失败");
            }
            
            // TODO: 创建订单逻辑
            
            return true;
        } finally {
            // 释放锁
            redisTemplate.delete(lockKey);
        }
    }
    
    @Override
    public Boolean checkSeckillStock(Long activityId, Integer quantity) {
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        if (activity == null) {
            return false;
        }
        
        // 检查活动是否在进行中
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getStartTime()) || now.isAfter(activity.getEndTime())) {
            return false;
        }
        
        // 检查库存
        return activity.getStock() - activity.getSold() >= quantity;
    }
}
