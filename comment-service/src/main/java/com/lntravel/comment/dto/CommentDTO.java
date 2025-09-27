package com.lntravel.comment.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评论DTO
 */
@Data
public class CommentDTO {
    
    @NotNull(message = "景区ID不能为空")
    private Long scenicId;
    
    private Long orderId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低1分")
    @Max(value = 5, message = "评分最高5分")
    private Integer rating;
    
    private String images;
}
