package com.lntravel.recommend.controller;

import com.lntravel.common.result.Result;
import com.lntravel.recommend.dto.AiChatDTO;
import com.lntravel.recommend.dto.RecommendQueryDTO;
import com.lntravel.recommend.entity.AiChatRecord;
import com.lntravel.recommend.entity.RecommendRecord;
import com.lntravel.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 推荐控制器
 */
@Slf4j
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {
    
    private final RecommendService recommendService;
    
    /**
     * 获取个性化推荐
     */
    @GetMapping("/personal")
    public Result<List<Object>> getPersonalRecommend(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("获取个性化推荐: userId={}", userId);
        List<Object> recommendations = recommendService.getPersonalRecommend(userId);
        return Result.success(recommendations);
    }
    
    /**
     * 获取热门推荐
     */
    @GetMapping("/hot")
    public Result<List<Object>> getHotRecommend(@RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取热门推荐: limit={}", limit);
        List<Object> recommendations = recommendService.getHotRecommend(limit);
        return Result.success(recommendations);
    }
    
    /**
     * 根据条件筛选推荐
     */
    @PostMapping("/filter")
    public Result<List<Object>> getFilteredRecommend(@RequestBody RecommendQueryDTO queryDTO) {
        log.info("根据条件筛选推荐: {}", queryDTO);
        List<Object> recommendations = recommendService.getFilteredRecommend(queryDTO);
        return Result.success(recommendations);
    }
    
    /**
     * AI聊天推荐
     */
    @PostMapping("/ai-chat")
    public Result<AiChatRecord> aiChatRecommend(@Valid @RequestBody AiChatDTO chatDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("AI聊天推荐: userId={}, message={}", userId, chatDTO.getMessage());
        AiChatRecord chatRecord = recommendService.aiChatRecommend(userId, chatDTO);
        return Result.success(chatRecord);
    }
    
    /**
     * 获取AI对话历史
     */
    @GetMapping("/ai-chat/history")
    public Result<List<AiChatRecord>> getChatHistory(
            @RequestParam String sessionId,
            HttpServletRequest request) {
        
        Long userId = getCurrentUserId(request);
        log.info("获取AI对话历史: userId={}, sessionId={}", userId, sessionId);
        List<AiChatRecord> history = recommendService.getChatHistory(userId, sessionId);
        return Result.success(history);
    }
    
    /**
     * 记录推荐点击
     */
    @PostMapping("/click/{recommendId}")
    public Result<Boolean> recordClick(@PathVariable Long recommendId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("记录推荐点击: recommendId={}, userId={}", recommendId, userId);
        Boolean success = recommendService.recordClick(recommendId, userId);
        return Result.success(success);
    }
    
    /**
     * 获取用户推荐历史
     */
    @GetMapping("/history")
    public Result<List<RecommendRecord>> getRecommendHistory(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("获取用户推荐历史: userId={}", userId);
        List<RecommendRecord> history = recommendService.getRecommendHistory(userId);
        return Result.success(history);
    }
    
    /**
     * 从请求中获取当前用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return com.lntravel.common.utils.JwtUtils.getUserIdFromToken(token);
        }
        throw new com.lntravel.common.exception.BusinessException("未登录");
    }
}
