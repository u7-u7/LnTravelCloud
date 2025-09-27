package com.lntravel.scenic.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lntravel.common.result.Result;
import com.lntravel.scenic.entity.Scenic;
import com.lntravel.scenic.entity.ScenicCategory;
import com.lntravel.scenic.service.ScenicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 景区控制器
 */
@Slf4j
@RestController
@RequestMapping("/scenic")
@RequiredArgsConstructor
public class ScenicController {
    
    private final ScenicService scenicService;
    
    /**
     * 获取景区列表
     */
    @GetMapping("/list")
    public Result<IPage<Scenic>> getScenicList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        
        log.info("获取景区列表: page={}, size={}, category={}, province={}, city={}, keyword={}, level={}, sortBy={}, sortOrder={}", 
                page, size, category, province, city, keyword, level, sortBy, sortOrder);
        
        IPage<Scenic> result = scenicService.getScenicList(page, size, category, province, city, keyword, level, sortBy, sortOrder);
        return Result.success(result);
    }
    
    /**
     * 获取景区详情
     */
    @GetMapping("/{id}")
    public Result<Scenic> getScenicDetail(@PathVariable Long id) {
        log.info("获取景区详情: id={}", id);
        Scenic scenic = scenicService.getScenicDetail(id);
        return Result.success(scenic);
    }
    
    /**
     * 搜索景区
     */
    @GetMapping("/search")
    public Result<IPage<Scenic>> searchScenic(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        log.info("搜索景区: keyword={}, page={}, size={}", keyword, page, size);
        IPage<Scenic> result = scenicService.searchScenic(keyword, page, size);
        return Result.success(result);
    }
    
    /**
     * 获取景区分类
     */
    @GetMapping("/categories")
    public Result<List<ScenicCategory>> getScenicCategories() {
        log.info("获取景区分类");
        List<ScenicCategory> categories = scenicService.getScenicCategories();
        return Result.success(categories);
    }
    
    /**
     * 获取景区热力图数据
     */
    @GetMapping("/heatmap")
    public Result<List<Object>> getScenicHeatmap() {
        log.info("获取景区热力图数据");
        List<Object> heatmapData = scenicService.getScenicHeatmap();
        return Result.success(heatmapData);
    }
    
    /**
     * 获取景区排行榜
     */
    @GetMapping("/ranking/{type}")
    public Result<List<Scenic>> getScenicRanking(@PathVariable String type) {
        log.info("获取景区排行榜: type={}", type);
        List<Scenic> ranking = scenicService.getScenicRanking(type);
        return Result.success(ranking);
    }
    
    /**
     * 获取省份景区数据
     */
    @GetMapping("/province/{provinceId}")
    public Result<Object> getProvinceScenic(@PathVariable String provinceId) {
        log.info("获取省份景区数据: provinceId={}", provinceId);
        Object provinceData = scenicService.getProvinceScenic(provinceId);
        return Result.success(provinceData);
    }
}
