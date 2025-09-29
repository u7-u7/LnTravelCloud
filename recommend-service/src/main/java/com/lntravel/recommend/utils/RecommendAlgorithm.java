package com.lntravel.recommend.utils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 推荐算法工具类
 */
public class RecommendAlgorithm {
    
    /**
     * 计算推荐分数
     */
    public static BigDecimal calculateRecommendScore(Long userId, Long scenicId, 
                                                   Map<String, Object> userPreferences,
                                                   Map<String, Object> scenicFeatures) {
        BigDecimal score = BigDecimal.ZERO;
        
        // 1. 基于用户偏好计算分数
        if (userPreferences != null) {
            String preferredCategory = (String) userPreferences.get("category");
            String scenicCategory = (String) scenicFeatures.get("category");
            if (preferredCategory != null && preferredCategory.equals(scenicCategory)) {
                score = score.add(BigDecimal.valueOf(20));
            }
            
            String preferredProvince = (String) userPreferences.get("province");
            String scenicProvince = (String) scenicFeatures.get("province");
            if (preferredProvince != null && preferredProvince.equals(scenicProvince)) {
                score = score.add(BigDecimal.valueOf(15));
            }
        }
        
        // 2. 基于景区热度计算分数
        Double rating = (Double) scenicFeatures.get("rating");
        if (rating != null) {
            score = score.add(BigDecimal.valueOf(rating * 10));
        }
        
        Integer visitCount = (Integer) scenicFeatures.get("visitCount");
        if (visitCount != null) {
            score = score.add(BigDecimal.valueOf(Math.min(visitCount / 1000, 20)));
        }
        
        // 3. 基于用户历史行为计算分数
        Integer userClickCount = (Integer) userPreferences.get("clickCount");
        if (userClickCount != null && userClickCount > 0) {
            score = score.add(BigDecimal.valueOf(Math.min(userClickCount * 2, 15)));
        }
        
        // 确保分数在0-100之间
        return score.min(BigDecimal.valueOf(100)).max(BigDecimal.ZERO);
    }
    
    /**
     * 提取关键词
     */
    public static List<String> extractKeywords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> keywords = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");
        
        // 旅游相关关键词
        Set<String> travelKeywords = new HashSet<>(Arrays.asList(
            "海", "山", "湖", "河", "公园", "博物馆", "历史", "文化", "自然", "风景",
            "海滨", "山区", "湖泊", "河流", "主题公园", "古迹", "寺庙", "教堂",
            "海滩", "温泉", "滑雪", "登山", "徒步", "摄影", "美食", "购物"
        ));
        
        for (String word : words) {
            if (word.length() > 1 && travelKeywords.contains(word)) {
                keywords.add(word);
            }
        }
        
        return keywords;
    }
    
    /**
     * 生成推荐理由
     */
    public static String generateRecommendReason(String recommendType, Map<String, Object> context) {
        switch (recommendType) {
            case "personal":
                return "基于您的浏览历史和偏好推荐";
            case "hot":
                return "当前热门景区，评分很高";
            case "ai":
                String keywords = (String) context.get("keywords");
                if (keywords != null && !keywords.isEmpty()) {
                    return "根据您提到的" + keywords + "为您推荐";
                }
                return "AI智能推荐";
            case "similar":
                return "与您喜欢的景区相似";
            case "nearby":
                return "您附近的优质景区";
            default:
                return "为您精心推荐";
        }
    }
    
    /**
     * 生成AI回复模板
     */
    public static String generateAiReplyTemplate(String userMessage, List<Object> recommendations) {
        StringBuilder reply = new StringBuilder();
        
        // 分析用户意图
        if (userMessage.contains("海") || userMessage.contains("海滨")) {
            reply.append("根据您想看海的愿望，我为您推荐以下海滨景区：\n\n");
        } else if (userMessage.contains("山") || userMessage.contains("登山")) {
            reply.append("为您推荐这些美丽的山景：\n\n");
        } else if (userMessage.contains("历史") || userMessage.contains("文化")) {
            reply.append("这些历史文化景区很适合您：\n\n");
        } else {
            reply.append("根据您的需求，我为您推荐以下景区：\n\n");
        }
        
        // 添加推荐内容
        for (int i = 0; i < Math.min(recommendations.size(), 3); i++) {
            reply.append(String.format("%d. 景区名称 - 景区描述，评分4.%d\n", 
                i + 1, 5 + i));
        }
        
        reply.append("\n这些景区都很有特色，您可以根据自己的兴趣选择。");
        
        return reply.toString();
    }
}
