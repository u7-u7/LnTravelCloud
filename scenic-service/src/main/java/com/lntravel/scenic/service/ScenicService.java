package com.lntravel.scenic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lntravel.scenic.entity.Scenic;
import com.lntravel.scenic.entity.ScenicCategory;

import java.util.List;

/**
 * 景区服务接口
 */
public interface ScenicService extends IService<Scenic> {
    
    /**
     * 获取景区列表
     */
    IPage<Scenic> getScenicList(Integer page, Integer size, String category, String province, 
                                String city, String keyword, String level, String sortBy, String sortOrder);
    
    /**
     * 获取景区详情
     */
    Scenic getScenicDetail(Long id);
    
    /**
     * 搜索景区
     */
    IPage<Scenic> searchScenic(String keyword, Integer page, Integer size);
    
    /**
     * 获取景区分类
     */
    List<ScenicCategory> getScenicCategories();
    
    /**
     * 获取景区热力图数据
     */
    List<Object> getScenicHeatmap();
    
    /**
     * 获取景区排行榜
     */
    List<Scenic> getScenicRanking(String type);
    
    /**
     * 获取省份景区数据
     */
    Object getProvinceScenic(String provinceId);
}
