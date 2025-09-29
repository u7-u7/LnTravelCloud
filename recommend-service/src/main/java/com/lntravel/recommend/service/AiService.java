package com.lntravel.recommend.service;

/**
 * AI服务接口
 */
public interface AiService {
    
    /**
     * 生成AI回复
     */
    String generateReply(String userMessage, String sessionId);
    
    /**
     * 生成智能推荐
     */
    String generateRecommendation(String userMessage, String context);
    
    /**
     * 分析用户意图
     */
    String analyzeUserIntent(String userMessage);
    
    /**
     * 提取关键词
     */
    String extractKeywords(String userMessage);
    
    /**
     * 生成推荐理由
     */
    String generateRecommendReason(String userMessage, String scenicInfo);
}
