package com.lntravel.recommend.service;

import com.lntravel.recommend.dto.AiChatDTO;
import com.lntravel.recommend.dto.RecommendQueryDTO;
import com.lntravel.recommend.entity.AiChatRecord;
import com.lntravel.recommend.entity.RecommendRecord;

import java.util.List;

/**
 * 推荐服务接口
 */
public interface RecommendService {
    
    /**
     * 获取个性化推荐
     */
    List<Object> getPersonalRecommend(Long userId);
    
    /**
     * 获取热门推荐
     */
    List<Object> getHotRecommend(Integer limit);
    
    /**
     * 根据条件筛选推荐
     */
    List<Object> getFilteredRecommend(RecommendQueryDTO queryDTO);
    
    /**
     * AI聊天推荐
     */
    AiChatRecord aiChatRecommend(Long userId, AiChatDTO chatDTO);
    
    /**
     * 获取AI对话历史
     */
    List<AiChatRecord> getChatHistory(Long userId, String sessionId);
    
    /**
     * 记录推荐点击
     */
    Boolean recordClick(Long recommendId, Long userId);
    
    /**
     * 获取用户推荐历史
     */
    List<RecommendRecord> getRecommendHistory(Long userId);
    
    /**
     * 生成推荐数据
     */
    void generateRecommendations(Long userId);
}
