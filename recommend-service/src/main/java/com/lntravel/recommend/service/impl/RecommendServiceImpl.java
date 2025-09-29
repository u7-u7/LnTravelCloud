package com.lntravel.recommend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lntravel.common.result.Result;
import com.lntravel.recommend.client.ScenicServiceClient;
import com.lntravel.recommend.client.UserServiceClient;
import com.lntravel.recommend.dto.AiChatDTO;
import com.lntravel.recommend.dto.RecommendQueryDTO;
import com.lntravel.recommend.entity.AiChatRecord;
import com.lntravel.recommend.entity.RecommendRecord;
import com.lntravel.recommend.mapper.AiChatRecordMapper;
import com.lntravel.recommend.mapper.RecommendRecordMapper;
import com.lntravel.recommend.service.RecommendService;
import com.lntravel.recommend.utils.RecommendAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 推荐服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    
    private final RecommendRecordMapper recommendRecordMapper;
    private final AiChatRecordMapper aiChatRecordMapper;
    private final ScenicServiceClient scenicServiceClient;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;
    
    @Override
    public List<Object> getPersonalRecommend(Long userId) {
        log.info("获取个性化推荐: userId={}", userId);
        
        try {
            // 1. 获取用户历史行为数据
            List<RecommendRecord> userHistory = recommendRecordMapper.getByUserId(userId);
            List<Object> userFootprints = getUserFootprints(userId);
            List<Object> userFavorites = getUserFavorites(userId);
            
            // 2. 基于用户行为生成个性化推荐
            List<Object> recommendations = generatePersonalRecommendations(userId, userHistory, userFootprints, userFavorites);
            
            // 3. 保存推荐记录
            saveRecommendRecords(userId, recommendations, "personal");
            
            return recommendations;
        } catch (Exception e) {
            log.error("获取个性化推荐失败: userId={}", userId, e);
            return getHotRecommend(10); // 降级到热门推荐
        }
    }
    
    @Override
    public List<Object> getHotRecommend(Integer limit) {
        log.info("获取热门推荐: limit={}", limit);
        
        try {
            // 1. 获取热门景区（基于点击量）
            List<Object> hotScenics = recommendRecordMapper.getHotRecommendations(limit);
            
            // 2. 获取景区详细信息
            List<Object> recommendations = new ArrayList<>();
            for (Object scenicId : hotScenics) {
                try {
                    Result<Object> scenicResult = scenicServiceClient.getScenicDetail(Long.valueOf(scenicId.toString()));
                    if (scenicResult.getCode() == 200 && scenicResult.getData() != null) {
                        recommendations.add(scenicResult.getData());
                    }
                } catch (Exception e) {
                    log.warn("获取景区详情失败: scenicId={}", scenicId, e);
                }
            }
            
            // 3. 如果热门推荐不足，补充其他景区
            if (recommendations.size() < limit) {
                Result<Object> scenicListResult = scenicServiceClient.getScenicList(1, limit - recommendations.size(), null, null, null, null, null, "visitCount", "desc");
                if (scenicListResult.getCode() == 200 && scenicListResult.getData() != null) {
                    // 这里需要根据实际返回的数据结构进行解析
                    recommendations.addAll(Collections.singletonList(scenicListResult.getData()));
                }
            }
            
            return recommendations;
        } catch (Exception e) {
            log.error("获取热门推荐失败: limit={}", limit, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Object> getFilteredRecommend(RecommendQueryDTO queryDTO) {
        log.info("根据条件筛选推荐: {}", queryDTO);
        
        try {
            // 调用景区服务获取筛选结果
            Result<Object> result = scenicServiceClient.getScenicList(
                    1, 
                    queryDTO.getLimit() != null ? queryDTO.getLimit() : 10,
                    queryDTO.getCategory(),
                    queryDTO.getProvince(),
                    queryDTO.getCity(),
                    null,
                    queryDTO.getLevel(),
                    "rating",
                    "desc"
            );
            
            if (result.getCode() == 200 && result.getData() != null) {
                return Collections.singletonList(result.getData());
            }
            
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("根据条件筛选推荐失败: {}", queryDTO, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public AiChatRecord aiChatRecommend(Long userId, AiChatDTO chatDTO) {
        log.info("AI聊天推荐: userId={}, message={}", userId, chatDTO.getMessage());
        
        try {
            // 1. 解析用户消息，提取关键词
            List<String> keywords = extractKeywords(chatDTO.getMessage());
            
            // 2. 基于关键词生成推荐
            List<Object> recommendations = generateAiRecommendations(keywords);
            
            // 3. 生成AI回复
            String aiReply = generateAiReply(chatDTO.getMessage(), recommendations);
            
            // 4. 保存聊天记录
            AiChatRecord chatRecord = new AiChatRecord();
            chatRecord.setUserId(userId);
            chatRecord.setSessionId(chatDTO.getSessionId());
            chatRecord.setMessage(chatDTO.getMessage());
            chatRecord.setReply(aiReply);
            chatRecord.setRecommendations(convertRecommendationsToJson(recommendations));
            chatRecord.setCreateTime(LocalDateTime.now());
            chatRecord.setUpdateTime(LocalDateTime.now());
            chatRecord.setDeleted(0);
            
            aiChatRecordMapper.insert(chatRecord);
            
            return chatRecord;
        } catch (Exception e) {
            log.error("AI聊天推荐失败: userId={}, message={}", userId, chatDTO.getMessage(), e);
            
            // 返回默认回复
            AiChatRecord chatRecord = new AiChatRecord();
            chatRecord.setUserId(userId);
            chatRecord.setSessionId(chatDTO.getSessionId());
            chatRecord.setMessage(chatDTO.getMessage());
            chatRecord.setReply("抱歉，我暂时无法为您提供推荐，请稍后再试。");
            chatRecord.setRecommendations("[]");
            chatRecord.setCreateTime(LocalDateTime.now());
            chatRecord.setUpdateTime(LocalDateTime.now());
            chatRecord.setDeleted(0);
            
            return chatRecord;
        }
    }
    
    @Override
    public List<AiChatRecord> getChatHistory(Long userId, String sessionId) {
        log.info("获取AI对话历史: userId={}, sessionId={}", userId, sessionId);
        
        try {
            if (StringUtils.hasText(sessionId)) {
                return aiChatRecordMapper.getByUserIdAndSessionId(userId, sessionId);
            } else {
                return aiChatRecordMapper.getRecentByUserId(userId, 20);
            }
        } catch (Exception e) {
            log.error("获取AI对话历史失败: userId={}, sessionId={}", userId, sessionId, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public Boolean recordClick(Long recommendId, Long userId) {
        log.info("记录推荐点击: recommendId={}, userId={}", recommendId, userId);
        
        try {
            RecommendRecord record = recommendRecordMapper.selectById(recommendId);
            if (record != null && record.getUserId().equals(userId)) {
                record.setIsClicked(1);
                record.setClickTime(LocalDateTime.now());
                record.setUpdateTime(LocalDateTime.now());
                recommendRecordMapper.updateById(record);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("记录推荐点击失败: recommendId={}, userId={}", recommendId, userId, e);
            return false;
        }
    }
    
    @Override
    public List<RecommendRecord> getRecommendHistory(Long userId) {
        log.info("获取用户推荐历史: userId={}", userId);
        
        try {
            return recommendRecordMapper.getByUserId(userId);
        } catch (Exception e) {
            log.error("获取用户推荐历史失败: userId={}", userId, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public void generateRecommendations(Long userId) {
        log.info("生成推荐数据: userId={}", userId);
        
        try {
            // 1. 获取个性化推荐
            List<Object> personalRecommendations = getPersonalRecommend(userId);
            
            // 2. 获取热门推荐
            List<Object> hotRecommendations = getHotRecommend(5);
            
            // 3. 合并推荐结果
            List<Object> allRecommendations = new ArrayList<>();
            allRecommendations.addAll(personalRecommendations);
            allRecommendations.addAll(hotRecommendations);
            
            log.info("为用户 {} 生成了 {} 条推荐", userId, allRecommendations.size());
        } catch (Exception e) {
            log.error("生成推荐数据失败: userId={}", userId, e);
        }
    }
    
    // 私有方法
    
    private List<Object> getUserFootprints(Long userId) {
        try {
            Result<List<Object>> result = userServiceClient.getUserFootprints(userId);
            if (result.getCode() == 200 && result.getData() != null) {
                return result.getData();
            }
        } catch (Exception e) {
            log.warn("获取用户足迹失败: userId={}", userId, e);
        }
        return new ArrayList<>();
    }
    
    private List<Object> getUserFavorites(Long userId) {
        try {
            Result<List<Object>> result = userServiceClient.getUserFavorites(userId);
            if (result.getCode() == 200 && result.getData() != null) {
                return result.getData();
            }
        } catch (Exception e) {
            log.warn("获取用户收藏失败: userId={}", userId, e);
        }
        return new ArrayList<>();
    }
    
    private List<Object> generatePersonalRecommendations(Long userId, List<RecommendRecord> userHistory, 
                                                       List<Object> userFootprints, List<Object> userFavorites) {
        List<Object> recommendations = new ArrayList<>();
        
        try {
            // 基于用户收藏推荐相似景区
            if (!userFavorites.isEmpty()) {
                // 这里应该调用景区服务获取相似景区
                // 暂时返回空列表
                log.debug("用户收藏数量: {}", userFavorites.size());
            }
            
            // 基于用户足迹推荐周边景区
            if (!userFootprints.isEmpty()) {
                // 这里应该调用景区服务获取周边景区
                // 暂时返回空列表
                log.debug("用户足迹数量: {}", userFootprints.size());
            }
            
            // 如果个性化推荐不足，补充热门推荐
            if (recommendations.size() < 5) {
                List<Object> hotRecommendations = getHotRecommend(5 - recommendations.size());
                recommendations.addAll(hotRecommendations);
            }
            
        } catch (Exception e) {
            log.error("生成个性化推荐失败: userId={}", userId, e);
        }
        
        return recommendations;
    }
    
    private void saveRecommendRecords(Long userId, List<Object> recommendations, String recommendType) {
        try {
            for (int i = 0; i < recommendations.size(); i++) {
                RecommendRecord record = new RecommendRecord();
                record.setUserId(userId);
                // 这里需要根据实际数据结构设置scenicId
                // record.setScenicId(scenicId);
                record.setRecommendType(recommendType);
                record.setRecommendReason(RecommendAlgorithm.generateRecommendReason("personal", new HashMap<>()));
                record.setScore(BigDecimal.valueOf(90 - i * 5)); // 模拟推荐分数
                record.setIsClicked(0);
                record.setCreateTime(LocalDateTime.now());
                record.setUpdateTime(LocalDateTime.now());
                record.setDeleted(0);
                
                recommendRecordMapper.insert(record);
            }
        } catch (Exception e) {
            log.error("保存推荐记录失败: userId={}", userId, e);
        }
    }
    
    private List<String> extractKeywords(String message) {
        return RecommendAlgorithm.extractKeywords(message);
    }
    
    private List<Object> generateAiRecommendations(List<String> keywords) {
        // 基于关键词生成推荐
        List<Object> recommendations = new ArrayList<>();
        
        try {
            // 构建搜索关键词
            String keyword = String.join(" ", keywords);
            
            // 调用景区服务搜索
            Result<Object> result = scenicServiceClient.getScenicList(1, 5, null, null, null, keyword, null, "rating", "desc");
            if (result.getCode() == 200 && result.getData() != null) {
                recommendations.add(result.getData());
            }
        } catch (Exception e) {
            log.error("生成AI推荐失败: keywords={}", keywords, e);
        }
        
        return recommendations;
    }
    
    private String generateAiReply(String userMessage, List<Object> recommendations) {
        return RecommendAlgorithm.generateAiReplyTemplate(userMessage, recommendations);
    }
    
    private String convertRecommendationsToJson(List<Object> recommendations) {
        try {
            if (recommendations == null || recommendations.isEmpty()) {
                return "[]";
            }
            
            // 使用Jackson ObjectMapper将推荐结果转换为JSON字符串
            return objectMapper.writeValueAsString(recommendations);
        } catch (JsonProcessingException e) {
            log.error("转换推荐结果为JSON失败", e);
            // 如果转换失败，返回一个包含错误信息的JSON
            return "[{\"error\":\"推荐结果转换失败\"}]";
        }
    }
}
