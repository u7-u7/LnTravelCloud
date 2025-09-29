package com.lntravel.recommend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lntravel.recommend.entity.RecommendRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 推荐记录Mapper接口
 */
@Mapper
public interface RecommendRecordMapper extends BaseMapper<RecommendRecord> {
    
    /**
     * 根据用户ID获取推荐记录
     */
    @Select("SELECT * FROM recommend_record WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC")
    List<RecommendRecord> getByUserId(@Param("userId") Long userId);
    
    /**
     * 根据推荐类型获取推荐记录
     */
    @Select("SELECT * FROM recommend_record WHERE recommend_type = #{recommendType} AND deleted = 0 ORDER BY score DESC LIMIT #{limit}")
    List<RecommendRecord> getByRecommendType(@Param("recommendType") String recommendType, @Param("limit") Integer limit);
    
    /**
     * 获取用户点击的推荐记录
     */
    @Select("SELECT * FROM recommend_record WHERE user_id = #{userId} AND is_clicked = 1 AND deleted = 0 ORDER BY click_time DESC")
    List<RecommendRecord> getClickedByUserId(@Param("userId") Long userId);
    
    /**
     * 统计用户推荐点击率
     */
    @Select("SELECT COUNT(*) FROM recommend_record WHERE user_id = #{userId} AND is_clicked = 1 AND deleted = 0")
    Integer getClickCountByUserId(@Param("userId") Long userId);
    
    /**
     * 获取热门推荐（基于点击量）
     */
    @Select("SELECT scenic_id, COUNT(*) as click_count FROM recommend_record WHERE is_clicked = 1 AND deleted = 0 GROUP BY scenic_id ORDER BY click_count DESC LIMIT #{limit}")
    List<Object> getHotRecommendations(@Param("limit") Integer limit);
}
