package com.lntravel.recommend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AI服务配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiConfig {
    
    /**
     * AI服务提供商
     */
    private String provider = "openai";
    
    /**
     * API密钥
     */
    private String apiKey;
    
    /**
     * API基础URL
     */
    private String baseUrl;
    
    /**
     * 模型名称
     */
    private String model = "gpt-3.5-turbo";
    
    /**
     * 最大token数
     */
    private Integer maxTokens = 1000;
    
    /**
     * 温度参数（0-1，控制随机性）
     */
    private Double temperature = 0.7;
    
    /**
     * 请求超时时间（毫秒）
     */
    private Integer timeout = 30000;
    
    /**
     * 重试次数
     */
    private Integer retryCount = 3;
    
    /**
     * 是否启用AI功能
     */
    private Boolean enabled = true;
    
    /**
     * 推荐系统提示词模板
     */
    private String systemPrompt = "你是一个专业的旅游推荐助手，擅长根据用户需求推荐合适的景区。请基于用户的需求和偏好，提供个性化的旅游推荐。";
    
    /**
     * 用户消息模板
     */
    private String userPromptTemplate = "用户需求：{userMessage}\n\n请根据以上需求，推荐3-5个合适的景区，并说明推荐理由。";
}
