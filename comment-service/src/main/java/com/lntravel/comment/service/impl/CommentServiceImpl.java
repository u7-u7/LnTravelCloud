package com.lntravel.comment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lntravel.comment.dto.CommentDTO;
import com.lntravel.comment.dto.CommentReplyDTO;
import com.lntravel.comment.entity.Comment;
import com.lntravel.comment.entity.CommentReply;
import com.lntravel.comment.mapper.CommentMapper;
import com.lntravel.comment.mapper.CommentReplyMapper;
import com.lntravel.comment.service.CommentService;
import com.lntravel.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    
    private final CommentMapper commentMapper;
    private final CommentReplyMapper commentReplyMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    @Transactional
    public Comment publishComment(Long userId, CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setUserId(userId);
        comment.setLikeCount(0);
        comment.setReplyCount(0);
        comment.setStatus(1);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        
        int result = commentMapper.insert(comment);
        if (result <= 0) {
            throw new BusinessException("发布评论失败");
        }
        
        return comment;
    }
    
    @Override
    @Transactional
    public CommentReply replyComment(Long userId, CommentReplyDTO replyDTO) {
        // 检查评论是否存在
        Comment comment = commentMapper.selectById(replyDTO.getCommentId());
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        CommentReply reply = new CommentReply();
        BeanUtils.copyProperties(replyDTO, reply);
        reply.setUserId(userId);
        reply.setLikeCount(0);
        reply.setCreateTime(LocalDateTime.now());
        reply.setUpdateTime(LocalDateTime.now());
        
        int result = commentReplyMapper.insert(reply);
        if (result <= 0) {
            throw new BusinessException("回复评论失败");
        }
        
        // 更新评论回复数
        comment.setReplyCount(comment.getReplyCount() + 1);
        commentMapper.updateById(comment);
        
        return reply;
    }
    
    @Override
    public IPage<Comment> getCommentsByScenicId(Long scenicId, Integer page, Integer size, Integer rating, String keyword) {
        Page<Comment> pageParam = new Page<>(page, size);
        return commentMapper.selectByScenicId(pageParam, scenicId, rating, keyword);
    }
    
    @Override
    public List<CommentReply> getRepliesByCommentId(Long commentId) {
        return commentReplyMapper.selectByCommentId(commentId);
    }
    
    @Override
    @Transactional
    public Boolean likeComment(Long commentId, Long userId) {
        // 检查是否已点赞
        String likeKey = "comment:like:" + commentId + ":" + userId;
        if (redisTemplate.hasKey(likeKey)) {
            throw new BusinessException("您已点赞过该评论");
        }
        
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        // 增加点赞数
        comment.setLikeCount(comment.getLikeCount() + 1);
        int result = commentMapper.updateById(comment);
        
        if (result > 0) {
            // 记录点赞状态
            redisTemplate.opsForValue().set(likeKey, "1");
            return true;
        }
        
        return false;
    }
    
    @Override
    @Transactional
    public Boolean likeReply(Long replyId, Long userId) {
        // 检查是否已点赞
        String likeKey = "reply:like:" + replyId + ":" + userId;
        if (redisTemplate.hasKey(likeKey)) {
            throw new BusinessException("您已点赞过该回复");
        }
        
        CommentReply reply = commentReplyMapper.selectById(replyId);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }
        
        // 增加点赞数
        reply.setLikeCount(reply.getLikeCount() + 1);
        int result = commentReplyMapper.updateById(reply);
        
        if (result > 0) {
            // 记录点赞状态
            redisTemplate.opsForValue().set(likeKey, "1");
            return true;
        }
        
        return false;
    }
    
    @Override
    @Transactional
    public Boolean deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("只能删除自己的评论");
        }
        
        // 删除评论
        int result = commentMapper.deleteById(commentId);
        if (result > 0) {
            // 删除相关回复
            commentReplyMapper.deleteByCommentId(commentId);
            return true;
        }
        
        return false;
    }
    
    @Override
    @Transactional
    public Boolean deleteReply(Long replyId, Long userId) {
        CommentReply reply = commentReplyMapper.selectById(replyId);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }
        
        if (!reply.getUserId().equals(userId)) {
            throw new BusinessException("只能删除自己的回复");
        }
        
        // 删除回复
        int result = commentReplyMapper.deleteById(replyId);
        if (result > 0) {
            // 更新评论回复数
            Comment comment = commentMapper.selectById(reply.getCommentId());
            if (comment != null) {
                comment.setReplyCount(Math.max(0, comment.getReplyCount() - 1));
                commentMapper.updateById(comment);
            }
            return true;
        }
        
        return false;
    }
    
    @Override
    public List<Object> getRatingStatistics(Long scenicId) {
        return commentMapper.selectRatingStatistics(scenicId);
    }
    
    @Override
    public List<Comment> getHotComments(Long scenicId, Integer limit) {
        return commentMapper.selectHotComments(scenicId, limit);
    }
    
    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentMapper.selectByUserId(userId);
    }
}
