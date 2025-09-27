package com.lntravel.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lntravel.comment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论Mapper
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    /**
     * 根据景区ID分页查询评论
     */
    IPage<Comment> selectByScenicId(Page<Comment> page, @Param("scenicId") Long scenicId, @Param("rating") Integer rating, @Param("keyword") String keyword);
    
    /**
     * 根据用户ID查询评论
     */
    List<Comment> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 查询景区评分统计
     */
    List<Object> selectRatingStatistics(@Param("scenicId") Long scenicId);
    
    /**
     * 查询热门评论
     */
    List<Comment> selectHotComments(@Param("scenicId") Long scenicId, @Param("limit") Integer limit);
}
