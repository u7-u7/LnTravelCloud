package com.lntravel.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lntravel.comment.entity.CommentReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论回复Mapper
 */
@Mapper
public interface CommentReplyMapper extends BaseMapper<CommentReply> {
    
    /**
     * 根据评论ID查询回复
     */
    List<CommentReply> selectByCommentId(@Param("commentId") Long commentId);
    
    /**
     * 根据评论ID删除回复
     */
    int deleteByCommentId(@Param("commentId") Long commentId);
}
