package com.lntravel.recommend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lntravel.recommend.config.AiConfig;
import com.lntravel.recommend.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {
    
    private final AiConfig aiConfig;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    
    @Override
    public String generateReply(String userMessage, String sessionId) {
        log.info("生成AI回复: userMessage={}, sessionId={}", userMessage, sessionId);
        
        if (!aiConfig.getEnabled()) {
            return generateFallbackReply(userMessage);
        }
        
        try {
            // 构建请求参数
            Map<String, Object> requestBody = buildRequestBody(userMessage);
            
            // 调用AI API
            String response = callAiApi(requestBody);
            
            // 解析响应
            return parseAiResponse(response);
            
        } catch (Exception e) {
            log.error("生成AI回复失败: userMessage={}", userMessage, e);
            return generateFallbackReply(userMessage);
        }
    }
    
    @Override
    public String generateRecommendation(String userMessage, String context) {
        log.info("生成智能推荐: userMessage={}, context={}", userMessage, context);
        
        if (!aiConfig.getEnabled()) {
            return generateFallbackRecommendation(userMessage);
        }
        
        try {
            // 构建推荐请求
            String prompt = buildRecommendationPrompt(userMessage, context);
            Map<String, Object> requestBody = buildRequestBody(prompt);
            
            // 调用AI API
            String response = callAiApi(requestBody);
            
            // 解析推荐结果
            return parseRecommendationResponse(response);
            
        } catch (Exception e) {
            log.error("生成智能推荐失败: userMessage={}", userMessage, e);
            return generateFallbackRecommendation(userMessage);
        }
    }
    
    @Override
    public String analyzeUserIntent(String userMessage) {
        log.info("分析用户意图: userMessage={}", userMessage);
        
        if (!aiConfig.getEnabled()) {
            return "旅游推荐";
        }
        
        try {
            String prompt = "请分析以下用户消息的意图，返回关键词：\n" + userMessage;
            Map<String, Object> requestBody = buildRequestBody(prompt);
            
            String response = callAiApi(requestBody);
            return parseIntentResponse(response);
            
        } catch (Exception e) {
            log.error("分析用户意图失败: userMessage={}", userMessage, e);
            return "旅游推荐";
        }
    }
    
    @Override
    public String extractKeywords(String userMessage) {
        log.info("提取关键词: userMessage={}", userMessage);
        
        if (!aiConfig.getEnabled()) {
            return extractKeywordsFallback(userMessage);
        }
        
        try {
            String prompt = "请从以下文本中提取旅游相关的关键词，用逗号分隔：\n" + userMessage;
            Map<String, Object> requestBody = buildRequestBody(prompt);
            
            String response = callAiApi(requestBody);
            return parseKeywordsResponse(response);
            
        } catch (Exception e) {
            log.error("提取关键词失败: userMessage={}", userMessage, e);
            return extractKeywordsFallback(userMessage);
        }
    }
    
    @Override
    public String generateRecommendReason(String userMessage, String scenicInfo) {
        log.info("生成推荐理由: userMessage={}, scenicInfo={}", userMessage, scenicInfo);
        
        if (!aiConfig.getEnabled()) {
            return "基于您的偏好推荐";
        }
        
        try {
            String prompt = String.format("用户需求：%s\n景区信息：%s\n请生成个性化的推荐理由：", userMessage, scenicInfo);
            Map<String, Object> requestBody = buildRequestBody(prompt);
            
            String response = callAiApi(requestBody);
            return parseReasonResponse(response);
            
        } catch (Exception e) {
            log.error("生成推荐理由失败: userMessage={}", userMessage, e);
            return "基于您的偏好推荐";
        }
    }
    
    // 私有方法
    
    private Map<String, Object> buildRequestBody(String userMessage) {
        Map<String, Object> requestBody = new HashMap<>();
        
            if ("openai".equals(aiConfig.getProvider())) {
                // OpenAI格式
                requestBody.put("model", aiConfig.getModel());
                requestBody.put("max_tokens", aiConfig.getMaxTokens());
                requestBody.put("temperature", aiConfig.getTemperature());
                
                Map<String, Object> systemMessage = new HashMap<>();
                systemMessage.put("role", "system");
                systemMessage.put("content", aiConfig.getSystemPrompt());
                
                Map<String, Object> userMessageMap = new HashMap<>();
                userMessageMap.put("role", "user");
                userMessageMap.put("content", userMessage);
                
                requestBody.put("messages", List.of(systemMessage, userMessageMap));
        } else {
            // 其他AI服务格式
            requestBody.put("prompt", userMessage);
            requestBody.put("max_tokens", aiConfig.getMaxTokens());
            requestBody.put("temperature", aiConfig.getTemperature());
        }
        
        return requestBody;
    }
    
    private String callAiApi(Map<String, Object> requestBody) {
        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            
            return webClient.post()
                    .uri(aiConfig.getBaseUrl())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + aiConfig.getApiKey())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(aiConfig.getTimeout()))
                    .retry(aiConfig.getRetryCount())
                    .block();
                    
        } catch (JsonProcessingException e) {
            log.error("序列化请求体失败", e);
            throw new RuntimeException("AI API调用失败", e);
        }
    }
    
    private String parseAiResponse(String response) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            
            if ("openai".equals(aiConfig.getProvider())) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    return (String) message.get("content");
                }
            } else {
                // 其他AI服务的响应解析
                return (String) responseMap.get("text");
            }
            
        } catch (JsonProcessingException e) {
            log.error("解析AI响应失败", e);
        }
        
        return "抱歉，我暂时无法理解您的需求，请尝试更具体的描述。";
    }
    
    private String parseRecommendationResponse(String response) {
        // 解析推荐结果
        return parseAiResponse(response);
    }
    
    private String parseIntentResponse(String response) {
        // 解析意图分析结果
        return parseAiResponse(response);
    }
    
    private String parseKeywordsResponse(String response) {
        // 解析关键词提取结果
        return parseAiResponse(response);
    }
    
    private String parseReasonResponse(String response) {
        // 解析推荐理由结果
        return parseAiResponse(response);
    }
    
    private String buildRecommendationPrompt(String userMessage, String context) {
        return String.format(aiConfig.getUserPromptTemplate(), userMessage) + 
               (context != null ? "\n\n上下文信息：" + context : "");
    }
    
    // 降级处理方法
    
    private String generateFallbackReply(String userMessage) {
        if (userMessage.contains("海") || userMessage.contains("海滨")) {
            return "根据您想看海的愿望，我为您推荐三亚、青岛、厦门等海滨城市，这些地方都有美丽的海景和丰富的海滨活动。";
        } else if (userMessage.contains("山") || userMessage.contains("登山")) {
            return "为您推荐黄山、泰山、华山等名山，这些地方都有壮丽的山景和丰富的登山体验。";
        } else if (userMessage.contains("历史") || userMessage.contains("文化")) {
            return "推荐您参观故宫、兵马俑、长城等历史文化景点，这些地方承载着深厚的历史文化底蕴。";
        } else {
            return "根据您的需求，我为您推荐一些热门景区：故宫博物院、天安门广场、颐和园等，这些地方都很有特色。";
        }
    }
    
    private String generateFallbackRecommendation(String userMessage) {
        return "基于您的需求，我为您推荐以下景区：\n\n1. 故宫博物院 - 历史文化景区，评分4.8\n2. 天安门广场 - 历史文化景区，评分4.7\n3. 颐和园 - 皇家园林，评分4.6\n\n这些景区都很有特色，您可以根据自己的兴趣选择。";
    }
    
    private String extractKeywordsFallback(String userMessage) {
        // 简单的关键词提取
        if (userMessage.contains("海")) return "海滨,海洋";
        if (userMessage.contains("山")) return "山区,登山";
        if (userMessage.contains("历史")) return "历史,文化";
        if (userMessage.contains("自然")) return "自然,风景";
        return "旅游,景区";
    }
}
