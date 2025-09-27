package com.lntravel.comment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lntravel.comment.dto.CommentDTO;
import com.lntravel.comment.dto.CommentReplyDTO;
import com.lntravel.comment.entity.Comment;
import com.lntravel.comment.entity.CommentReply;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService {
    
    /**
     * 发布评论
     */
    Comment publishComment(Long userId, CommentDTO commentDTO);
    
    /**
     * 回复评论
     */
    CommentReply replyComment(Long userId, CommentReplyDTO replyDTO);
    
    /**
     * 根据景区ID分页查询评论
     */
    IPage<Comment> getCommentsByScenicId(Long scenicId, Integer page, Integer size, Integer rating, String keyword);
    
    /**
     * 根据评论ID查询回复
     */
    List<CommentReply> getRepliesByCommentId(Long commentId);
    
    /**
     * 点赞评论
     */
    Boolean likeComment(Long commentId, Long userId);
    
    /**
     * 点赞回复
     */
    Boolean likeReply(Long replyId, Long userId);
    
    /**
     * 删除评论
     */
    Boolean deleteComment(Long commentId, Long userId);
    
    /**
     * 删除回复
     */
    Boolean deleteReply(Long replyId, Long userId);
    
    /**
     * 获取景区评分统计
     */
    List<Object> getRatingStatistics(Long scenicId);
    
    /**
     * 获取热门评论
     */
    List<Comment> getHotComments(Long scenicId, Integer limit);
    
    /**
     * 根据用户ID查询评论历史
     */
    List<Comment> getCommentsByUserId(Long userId);
}
