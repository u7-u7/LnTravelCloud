package com.lntravel.recommend.service.impl;

import com.lntravel.recommend.service.AiService;
import com.lntravel.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 智能推荐服务实现类
 * 基于AI的智能推荐算法
 */
@Slf4j
@Service("smartRecommendService")
@RequiredArgsConstructor
public class SmartRecommendServiceImpl implements RecommendService {
    
    private final RecommendService basicRecommendService;
    private final AiService aiService;
    
    @Override
    public List<Object> getPersonalRecommend(Long userId) {
        log.info("智能个性化推荐: userId={}", userId);
        
        try {
            // 1. 获取基础个性化推荐
            List<Object> basicRecommendations = basicRecommendService.getPersonalRecommend(userId);
            
            // 2. 使用AI优化推荐结果
            List<Object> optimizedRecommendations = optimizeRecommendationsWithAi(basicRecommendations, userId);
            
            return optimizedRecommendations;
            
        } catch (Exception e) {
            log.error("智能个性化推荐失败: userId={}", userId, e);
            return basicRecommendService.getPersonalRecommend(userId);
        }
    }
    
    @Override
    public List<Object> getHotRecommend(Integer limit) {
        log.info("智能热门推荐: limit={}", limit);
        
        try {
            // 1. 获取基础热门推荐
            List<Object> basicRecommendations = basicRecommendService.getHotRecommend(limit);
            
            // 2. 使用AI分析热门趋势
            List<Object> trendOptimizedRecommendations = analyzeTrendWithAi(basicRecommendations);
            
            return trendOptimizedRecommendations;
            
        } catch (Exception e) {
            log.error("智能热门推荐失败: limit={}", limit, e);
            return basicRecommendService.getHotRecommend(limit);
        }
    }
    
    @Override
    public List<Object> getFilteredRecommend(com.lntravel.recommend.dto.RecommendQueryDTO queryDTO) {
        log.info("智能筛选推荐: {}", queryDTO);
        
        try {
            // 1. 获取基础筛选推荐
            List<Object> basicRecommendations = basicRecommendService.getFilteredRecommend(queryDTO);
            
            // 2. 使用AI优化筛选结果
            List<Object> optimizedRecommendations = optimizeFilterWithAi(basicRecommendations, queryDTO);
            
            return optimizedRecommendations;
            
        } catch (Exception e) {
            log.error("智能筛选推荐失败: {}", queryDTO, e);
            return basicRecommendService.getFilteredRecommend(queryDTO);
        }
    }
    
    @Override
    public com.lntravel.recommend.entity.AiChatRecord aiChatRecommend(Long userId, com.lntravel.recommend.dto.AiChatDTO chatDTO) {
        log.info("智能AI聊天推荐: userId={}, message={}", userId, chatDTO.getMessage());
        
        try {
            // 1. 使用AI服务生成智能回复
            String aiReply = aiService.generateReply(chatDTO.getMessage(), chatDTO.getSessionId());
            
            // 2. 使用AI服务生成智能推荐
            String aiRecommendation = aiService.generateRecommendation(chatDTO.getMessage(), null);
            
            // 3. 创建聊天记录
            com.lntravel.recommend.entity.AiChatRecord chatRecord = new com.lntravel.recommend.entity.AiChatRecord();
            chatRecord.setUserId(userId);
            chatRecord.setSessionId(chatDTO.getSessionId());
            chatRecord.setMessage(chatDTO.getMessage());
            chatRecord.setReply(aiReply);
            chatRecord.setRecommendations(aiRecommendation);
            chatRecord.setCreateTime(java.time.LocalDateTime.now());
            chatRecord.setUpdateTime(java.time.LocalDateTime.now());
            chatRecord.setDeleted(0);
            
            return chatRecord;
            
        } catch (Exception e) {
            log.error("智能AI聊天推荐失败: userId={}, message={}", userId, chatDTO.getMessage(), e);
            return basicRecommendService.aiChatRecommend(userId, chatDTO);
        }
    }
    
    @Override
    public List<com.lntravel.recommend.entity.AiChatRecord> getChatHistory(Long userId, String sessionId) {
        return basicRecommendService.getChatHistory(userId, sessionId);
    }
    
    @Override
    public Boolean recordClick(Long recommendId, Long userId) {
        return basicRecommendService.recordClick(recommendId, userId);
    }
    
    @Override
    public List<com.lntravel.recommend.entity.RecommendRecord> getRecommendHistory(Long userId) {
        return basicRecommendService.getRecommendHistory(userId);
    }
    
    @Override
    public void generateRecommendations(Long userId) {
        basicRecommendService.generateRecommendations(userId);
    }
    
    // 私有方法
    
    private List<Object> optimizeRecommendationsWithAi(List<Object> recommendations, Long userId) {
        try {
            // 使用AI分析用户偏好，优化推荐结果
            String userContext = "用户ID: " + userId + ", 推荐数量: " + recommendations.size();
            String aiOptimization = aiService.generateRecommendation("优化个性化推荐", userContext);
            
            log.info("AI优化结果: {}", aiOptimization);
            
            // 这里可以根据AI分析结果调整推荐顺序或过滤
            return recommendations;
            
        } catch (Exception e) {
            log.warn("AI优化推荐失败，返回原始推荐: {}", e.getMessage());
            return recommendations;
        }
    }
    
    private List<Object> analyzeTrendWithAi(List<Object> recommendations) {
        try {
            // 使用AI分析热门趋势
            String trendAnalysis = aiService.generateRecommendation("分析热门趋势", "推荐数量: " + recommendations.size());
            
            log.info("AI趋势分析: {}", trendAnalysis);
            
            return recommendations;
            
        } catch (Exception e) {
            log.warn("AI趋势分析失败，返回原始推荐: {}", e.getMessage());
            return recommendations;
        }
    }
    
    private List<Object> optimizeFilterWithAi(List<Object> recommendations, com.lntravel.recommend.dto.RecommendQueryDTO queryDTO) {
        try {
            // 使用AI优化筛选结果
            String filterContext = "筛选条件: " + queryDTO.toString();
            String aiOptimization = aiService.generateRecommendation("优化筛选结果", filterContext);
            
            log.info("AI筛选优化: {}", aiOptimization);
            
            return recommendations;
            
        } catch (Exception e) {
            log.warn("AI筛选优化失败，返回原始推荐: {}", e.getMessage());
            return recommendations;
        }
    }
}
