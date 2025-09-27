package com.lntravel.data.service;

import java.util.List;
import java.util.Map;

/**
 * 数据统计服务接口
 */
public interface DataService {
    
    /**
     * 获取首页统计数据
     */
    Map<String, Object> getOverviewData();
    
    /**
     * 获取全国热力图数据
     */
    List<Object> getNationalHeatmapData();
    
    /**
     * 获取省份热力图数据
     */
    List<Object> getProvinceHeatmapData(String province);
    
    /**
     * 获取景区热度排行榜
     */
    List<Object> getHotRanking(Integer limit);
    
    /**
     * 获取景区好评排行榜
     */
    List<Object> getRatingRanking(Integer limit);
    
    /**
     * 获取省份统计数据
     */
    Map<String, Object> getProvinceStatistics(String province);
    
    /**
     * 获取景区访问趋势
     */
    List<Object> getScenicTrend(Long scenicId, Integer days);
    
    /**
     * 获取客流量分析
     */
    Map<String, Object> getVisitorFlowAnalysis(String province, String startDate, String endDate);
    
    /**
     * 获取实时数据
     */
    Map<String, Object> getRealtimeData();
    
    /**
     * 生成统计数据
     */
    void generateStatistics();
}
