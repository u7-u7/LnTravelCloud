package com.lntravel.comment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lntravel.comment.dto.CommentDTO;
import com.lntravel.comment.dto.CommentReplyDTO;
import com.lntravel.comment.entity.Comment;
import com.lntravel.comment.entity.CommentReply;
import com.lntravel.comment.service.CommentService;
import com.lntravel.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 评论控制器
 */
@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    /**
     * 发布评论
     */
    @PostMapping("/publish")
    public Result<Comment> publishComment(@Valid @RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("发布评论: userId={}, scenicId={}", userId, commentDTO.getScenicId());
        Comment comment = commentService.publishComment(userId, commentDTO);
        return Result.success("发布成功", comment);
    }
    
    /**
     * 回复评论
     */
    @PostMapping("/reply")
    public Result<CommentReply> replyComment(@Valid @RequestBody CommentReplyDTO replyDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("回复评论: userId={}, commentId={}", userId, replyDTO.getCommentId());
        CommentReply reply = commentService.replyComment(userId, replyDTO);
        return Result.success("回复成功", reply);
    }
    
    /**
     * 根据景区ID分页查询评论
     */
    @GetMapping("/scenic/{scenicId}")
    public Result<IPage<Comment>> getCommentsByScenicId(
            @PathVariable Long scenicId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String keyword) {
        
        log.info("查询景区评论: scenicId={}, page={}, size={}, rating={}, keyword={}", 
                scenicId, page, size, rating, keyword);
        IPage<Comment> comments = commentService.getCommentsByScenicId(scenicId, page, size, rating, keyword);
        return Result.success(comments);
    }
    
    /**
     * 根据评论ID查询回复
     */
    @GetMapping("/{commentId}/replies")
    public Result<List<CommentReply>> getRepliesByCommentId(@PathVariable Long commentId) {
        log.info("查询评论回复: commentId={}", commentId);
        List<CommentReply> replies = commentService.getRepliesByCommentId(commentId);
        return Result.success(replies);
    }
    
    /**
     * 点赞评论
     */
    @PostMapping("/{commentId}/like")
    public Result<Boolean> likeComment(@PathVariable Long commentId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("点赞评论: commentId={}, userId={}", commentId, userId);
        Boolean success = commentService.likeComment(commentId, userId);
        return Result.success("点赞成功", success);
    }
    
    /**
     * 点赞回复
     */
    @PostMapping("/reply/{replyId}/like")
    public Result<Boolean> likeReply(@PathVariable Long replyId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("点赞回复: replyId={}, userId={}", replyId, userId);
        Boolean success = commentService.likeReply(replyId, userId);
        return Result.success("点赞成功", success);
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    public Result<Boolean> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("删除评论: commentId={}, userId={}", commentId, userId);
        Boolean success = commentService.deleteComment(commentId, userId);
        return Result.success("删除成功", success);
    }
    
    /**
     * 删除回复
     */
    @DeleteMapping("/reply/{replyId}")
    public Result<Boolean> deleteReply(@PathVariable Long replyId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("删除回复: replyId={}, userId={}", replyId, userId);
        Boolean success = commentService.deleteReply(replyId, userId);
        return Result.success("删除成功", success);
    }
    
    /**
     * 获取景区评分统计
     */
    @GetMapping("/scenic/{scenicId}/rating-statistics")
    public Result<List<Object>> getRatingStatistics(@PathVariable Long scenicId) {
        log.info("获取景区评分统计: scenicId={}", scenicId);
        List<Object> statistics = commentService.getRatingStatistics(scenicId);
        return Result.success(statistics);
    }
    
    /**
     * 获取热门评论
     */
    @GetMapping("/scenic/{scenicId}/hot")
    public Result<List<Comment>> getHotComments(
            @PathVariable Long scenicId,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        log.info("获取热门评论: scenicId={}, limit={}", scenicId, limit);
        List<Comment> comments = commentService.getHotComments(scenicId, limit);
        return Result.success(comments);
    }
    
    /**
     * 根据用户ID查询评论历史
     */
    @GetMapping("/user/history")
    public Result<List<Comment>> getCommentsByUserId(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("查询用户评论历史: userId={}", userId);
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return Result.success(comments);
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
