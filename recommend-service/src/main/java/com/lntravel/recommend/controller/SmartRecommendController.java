package com.lntravel.recommend.controller;

import com.lntravel.common.result.Result;
import com.lntravel.recommend.dto.AiChatDTO;
import com.lntravel.recommend.dto.RecommendQueryDTO;
import com.lntravel.recommend.entity.AiChatRecord;
import com.lntravel.recommend.entity.RecommendRecord;
import com.lntravel.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 智能推荐控制器
 * 基于AI的智能推荐API
 */
@Slf4j
@RestController
@RequestMapping("/smart-recommend")
@RequiredArgsConstructor
public class SmartRecommendController {
    
    @Qualifier("smartRecommendService")
    private final RecommendService smartRecommendService;
    
    /**
     * 智能个性化推荐
     */
    @GetMapping("/personal")
    public Result<List<Object>> getSmartPersonalRecommend(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("智能个性化推荐: userId={}", userId);
        List<Object> recommendations = smartRecommendService.getPersonalRecommend(userId);
        return Result.success(recommendations);
    }
    
    /**
     * 智能热门推荐
     */
    @GetMapping("/hot")
    public Result<List<Object>> getSmartHotRecommend(@RequestParam(defaultValue = "10") Integer limit) {
        log.info("智能热门推荐: limit={}", limit);
        List<Object> recommendations = smartRecommendService.getHotRecommend(limit);
        return Result.success(recommendations);
    }
    
    /**
     * 智能条件筛选推荐
     */
    @PostMapping("/filter")
    public Result<List<Object>> getSmartFilteredRecommend(@RequestBody RecommendQueryDTO queryDTO) {
        log.info("智能条件筛选推荐: {}", queryDTO);
        List<Object> recommendations = smartRecommendService.getFilteredRecommend(queryDTO);
        return Result.success(recommendations);
    }
    
    /**
     * 智能AI聊天推荐
     */
    @PostMapping("/ai-chat")
    public Result<AiChatRecord> smartAiChatRecommend(@Valid @RequestBody AiChatDTO chatDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("智能AI聊天推荐: userId={}, message={}", userId, chatDTO.getMessage());
        AiChatRecord chatRecord = smartRecommendService.aiChatRecommend(userId, chatDTO);
        return Result.success(chatRecord);
    }
    
    /**
     * 获取智能推荐历史
     */
    @GetMapping("/history")
    public Result<List<RecommendRecord>> getSmartRecommendHistory(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("获取智能推荐历史: userId={}", userId);
        List<RecommendRecord> history = smartRecommendService.getRecommendHistory(userId);
        return Result.success(history);
    }
    
    /**
     * 从请求中获取当前用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        // 从请求头中获取用户ID
        String userIdStr = request.getHeader("userId");
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                return Long.valueOf(userIdStr);
            } catch (NumberFormatException e) {
                log.warn("无效的用户ID: {}", userIdStr);
            }
        }
        
        // 从JWT Token中解析用户ID（这里简化处理）
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            // 实际项目中应该解析JWT Token获取用户ID
            // 这里返回默认用户ID
            return 1L;
        }
        
        // 返回默认用户ID
        return 1L;
    }
}
