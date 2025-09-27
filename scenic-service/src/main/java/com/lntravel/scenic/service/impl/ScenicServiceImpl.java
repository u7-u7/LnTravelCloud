package com.lntravel.scenic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lntravel.common.exception.BusinessException;
import com.lntravel.scenic.entity.Scenic;
import com.lntravel.scenic.entity.ScenicCategory;
import com.lntravel.scenic.mapper.ScenicMapper;
import com.lntravel.scenic.mapper.ScenicCategoryMapper;
import com.lntravel.scenic.service.ScenicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 景区服务实现类
 */
@Service
@RequiredArgsConstructor
public class ScenicServiceImpl extends ServiceImpl<ScenicMapper, Scenic> implements ScenicService {
    
    private final ScenicCategoryMapper scenicCategoryMapper;
    
    @Override
    public IPage<Scenic> getScenicList(Integer page, Integer size, String category, String province, 
                                       String city, String keyword, String level, String sortBy, String sortOrder) {
        Page<Scenic> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Scenic> wrapper = new LambdaQueryWrapper<>();
        
        // 构建查询条件
        if (StringUtils.hasText(category)) {
            wrapper.eq(Scenic::getCategoryId, category);
        }
        if (StringUtils.hasText(province)) {
            wrapper.eq(Scenic::getProvince, province);
        }
        if (StringUtils.hasText(city)) {
            wrapper.eq(Scenic::getCity, city);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Scenic::getName, keyword);
        }
        if (StringUtils.hasText(level)) {
            // 这里需要关联查询分类表，暂时简化处理
            wrapper.like(Scenic::getName, level);
        }
        
        // 排序
        if (StringUtils.hasText(sortBy)) {
            if ("rating".equals(sortBy)) {
                wrapper.orderByDesc(Scenic::getRating);
            } else if ("visitCount".equals(sortBy)) {
                wrapper.orderByDesc(Scenic::getVisitCount);
            } else if ("createTime".equals(sortBy)) {
                wrapper.orderByDesc(Scenic::getCreateTime);
            }
        } else {
            wrapper.orderByDesc(Scenic::getCreateTime);
        }
        
        return page(pageParam, wrapper);
    }
    
    @Override
    public Scenic getScenicDetail(Long id) {
        Scenic scenic = getById(id);
        if (scenic == null) {
            throw new BusinessException("景区不存在");
        }
        return scenic;
    }
    
    @Override
    public IPage<Scenic> searchScenic(String keyword, Integer page, Integer size) {
        Page<Scenic> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Scenic> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Scenic::getName, keyword)
               .or()
               .like(Scenic::getDescription, keyword)
               .orderByDesc(Scenic::getRating);
        
        return page(pageParam, wrapper);
    }
    
    @Override
    public List<ScenicCategory> getScenicCategories() {
        return scenicCategoryMapper.selectList(null);
    }
    
    @Override
    public List<Object> getScenicHeatmap() {
        // 模拟热力图数据
        List<Object> heatmapData = new ArrayList<>();
        // 这里应该从数据库查询真实数据，暂时返回模拟数据
        return heatmapData;
    }
    
    @Override
    public List<Scenic> getScenicRanking(String type) {
        LambdaQueryWrapper<Scenic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Scenic::getStatus, 1);
        
        if ("hot".equals(type)) {
            wrapper.orderByDesc(Scenic::getVisitCount);
        } else if ("rating".equals(type)) {
            wrapper.orderByDesc(Scenic::getRating);
        } else {
            wrapper.orderByDesc(Scenic::getCreateTime);
        }
        
        wrapper.last("LIMIT 10");
        return list(wrapper);
    }
    
    @Override
    public Object getProvinceScenic(String provinceId) {
        // 模拟省份数据
        // 这里应该从数据库查询真实数据，暂时返回模拟数据
        return new Object();
    }
}
