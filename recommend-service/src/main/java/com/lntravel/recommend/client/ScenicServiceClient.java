package com.lntravel.recommend.client;

import com.lntravel.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 景区服务Feign客户端
 */
@FeignClient(name = "scenic-service", path = "/scenic")
public interface ScenicServiceClient {
    
    /**
     * 获取景区详情
     */
    @GetMapping("/{id}")
    Result<Object> getScenicDetail(@PathVariable("id") Long id);
    
    /**
     * 获取景区列表
     */
    @GetMapping("/list")
    Result<Object> getScenicList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder
    );
    
    /**
     * 获取景区排行榜
     */
    @GetMapping("/ranking/{type}")
    Result<List<Object>> getScenicRanking(@PathVariable("type") String type);
    
    /**
     * 获取景区热力图数据
     */
    @GetMapping("/heatmap")
    Result<List<Object>> getScenicHeatmap();
}
