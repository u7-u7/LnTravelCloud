package com.lntravel.ticket.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lntravel.common.result.Result;
import com.lntravel.ticket.entity.SeckillActivity;
import com.lntravel.ticket.entity.Ticket;
import com.lntravel.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 门票控制器
 */
@Slf4j
@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {
    
    private final TicketService ticketService;
    
    /**
     * 根据景区ID获取门票列表
     */
    @GetMapping("/scenic/{scenicId}")
    public Result<List<Ticket>> getTicketsByScenicId(@PathVariable Long scenicId) {
        log.info("获取景区门票列表: scenicId={}", scenicId);
        List<Ticket> tickets = ticketService.getTicketsByScenicId(scenicId);
        return Result.success(tickets);
    }
    
    /**
     * 获取门票详情
     */
    @GetMapping("/{ticketId}")
    public Result<Ticket> getTicketDetail(@PathVariable Long ticketId) {
        log.info("获取门票详情: ticketId={}", ticketId);
        Ticket ticket = ticketService.getTicketDetail(ticketId);
        return Result.success(ticket);
    }
    
    /**
     * 获取秒杀活动列表
     */
    @GetMapping("/seckill/list")
    public Result<IPage<SeckillActivity>> getSeckillActivities(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        log.info("获取秒杀活动列表: page={}, size={}", page, size);
        IPage<SeckillActivity> activities = ticketService.getSeckillActivities(page, size);
        return Result.success(activities);
    }
    
    /**
     * 获取正在进行的秒杀活动
     */
    @GetMapping("/seckill/active")
    public Result<List<SeckillActivity>> getActiveActivities() {
        log.info("获取正在进行的秒杀活动");
        List<SeckillActivity> activities = ticketService.getActiveActivities();
        return Result.success(activities);
    }
    
    /**
     * 根据门票ID获取秒杀活动
     */
    @GetMapping("/{ticketId}/seckill")
    public Result<List<SeckillActivity>> getSeckillByTicketId(@PathVariable Long ticketId) {
        log.info("根据门票ID获取秒杀活动: ticketId={}", ticketId);
        List<SeckillActivity> activities = ticketService.getSeckillByTicketId(ticketId);
        return Result.success(activities);
    }
    
    /**
     * 参与秒杀
     */
    @PostMapping("/seckill/{activityId}/participate")
    public Result<Boolean> participateSeckill(
            @PathVariable Long activityId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        
        log.info("参与秒杀: activityId={}, userId={}, quantity={}", activityId, userId, quantity);
        Boolean success = ticketService.participateSeckill(activityId, userId, quantity);
        return Result.success("秒杀成功", success);
    }
    
    /**
     * 检查秒杀库存
     */
    @GetMapping("/seckill/{activityId}/stock")
    public Result<Boolean> checkSeckillStock(
            @PathVariable Long activityId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        
        log.info("检查秒杀库存: activityId={}, quantity={}", activityId, quantity);
        Boolean available = ticketService.checkSeckillStock(activityId, quantity);
        return Result.success(available);
    }
}
