package com.lntravel.data.controller;

import com.lntravel.common.result.Result;
import com.lntravel.data.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据统计控制器
 */
@Slf4j
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {
    
    private final DataService dataService;
    
    /**
     * 获取首页统计数据
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverviewData() {
        log.info("获取首页统计数据");
        Map<String, Object> data = dataService.getOverviewData();
        return Result.success(data);
    }
    
    /**
     * 获取全国热力图数据
     */
    @GetMapping("/heatmap/national")
    public Result<List<Object>> getNationalHeatmapData() {
        log.info("获取全国热力图数据");
        List<Object> data = dataService.getNationalHeatmapData();
        return Result.success(data);
    }
    
    /**
     * 获取省份热力图数据
     */
    @GetMapping("/heatmap/province/{province}")
    public Result<List<Object>> getProvinceHeatmapData(@PathVariable String province) {
        log.info("获取省份热力图数据: province={}", province);
        List<Object> data = dataService.getProvinceHeatmapData(province);
        return Result.success(data);
    }
    
    /**
     * 获取景区热度排行榜
     */
    @GetMapping("/ranking/hot")
    public Result<List<Object>> getHotRanking(@RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取景区热度排行榜: limit={}", limit);
        List<Object> ranking = dataService.getHotRanking(limit);
        return Result.success(ranking);
    }
    
    /**
     * 获取景区好评排行榜
     */
    @GetMapping("/ranking/rating")
    public Result<List<Object>> getRatingRanking(@RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取景区好评排行榜: limit={}", limit);
        List<Object> ranking = dataService.getRatingRanking(limit);
        return Result.success(ranking);
    }
    
    /**
     * 获取省份统计数据
     */
    @GetMapping("/province/{province}/statistics")
    public Result<Map<String, Object>> getProvinceStatistics(@PathVariable String province) {
        log.info("获取省份统计数据: province={}", province);
        Map<String, Object> statistics = dataService.getProvinceStatistics(province);
        return Result.success(statistics);
    }
    
    /**
     * 获取景区访问趋势
     */
    @GetMapping("/scenic/{scenicId}/trend")
    public Result<List<Object>> getScenicTrend(
            @PathVariable Long scenicId,
            @RequestParam(defaultValue = "7") Integer days) {
        
        log.info("获取景区访问趋势: scenicId={}, days={}", scenicId, days);
        List<Object> trend = dataService.getScenicTrend(scenicId, days);
        return Result.success(trend);
    }
    
    /**
     * 获取客流量分析
     */
    @GetMapping("/visitor-flow")
    public Result<Map<String, Object>> getVisitorFlowAnalysis(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        log.info("获取客流量分析: province={}, startDate={}, endDate={}", province, startDate, endDate);
        Map<String, Object> analysis = dataService.getVisitorFlowAnalysis(province, startDate, endDate);
        return Result.success(analysis);
    }
    
    /**
     * 获取实时数据
     */
    @GetMapping("/realtime")
    public Result<Map<String, Object>> getRealtimeData() {
        log.info("获取实时数据");
        Map<String, Object> data = dataService.getRealtimeData();
        return Result.success(data);
    }
}
