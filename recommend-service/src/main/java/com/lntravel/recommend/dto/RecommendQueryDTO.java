package com.lntravel.recommend.dto;

import lombok.Data;

/**
 * 推荐查询DTO
 */
@Data
public class RecommendQueryDTO {
    
    private String province;
    
    private String city;
    
    private String category;
    
    private String level;
    
    private String type;
    
    private Integer limit;
}
