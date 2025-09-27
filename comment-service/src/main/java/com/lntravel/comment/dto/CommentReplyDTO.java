package com.lntravel.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评论回复DTO
 */
@Data
public class CommentReplyDTO {
    
    @NotNull(message = "评论ID不能为空")
    private Long commentId;
    
    @NotBlank(message = "回复内容不能为空")
    private String content;
    
    private Long parentId;
}
