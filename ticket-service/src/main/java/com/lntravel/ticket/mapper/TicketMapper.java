package com.lntravel.ticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lntravel.ticket.entity.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门票Mapper
 */
@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {
    
    /**
     * 根据景区ID查询门票
     */
    List<Ticket> selectByScenicId(@Param("scenicId") Long scenicId);
    
    /**
     * 根据门票类型查询门票
     */
    List<Ticket> selectByType(@Param("type") String type);
}
