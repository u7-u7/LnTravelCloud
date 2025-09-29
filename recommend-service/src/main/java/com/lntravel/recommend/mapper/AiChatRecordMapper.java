package com.lntravel.recommend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lntravel.recommend.entity.AiChatRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI聊天记录Mapper接口
 */
@Mapper
public interface AiChatRecordMapper extends BaseMapper<AiChatRecord> {
    
    /**
     * 根据用户ID和会话ID获取聊天记录
     */
    @Select("SELECT * FROM ai_chat_record WHERE user_id = #{userId} AND session_id = #{sessionId} AND deleted = 0 ORDER BY create_time ASC")
    List<AiChatRecord> getByUserIdAndSessionId(@Param("userId") Long userId, @Param("sessionId") String sessionId);
    
    /**
     * 根据用户ID获取所有聊天记录
     */
    @Select("SELECT * FROM ai_chat_record WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC")
    List<AiChatRecord> getByUserId(@Param("userId") Long userId);
    
    /**
     * 获取用户最近的聊天记录
     */
    @Select("SELECT * FROM ai_chat_record WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC LIMIT #{limit}")
    List<AiChatRecord> getRecentByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);
}
