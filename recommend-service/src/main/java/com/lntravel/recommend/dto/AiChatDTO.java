package com.lntravel.recommend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * AI聊天DTO
 */
@Data
public class AiChatDTO {
    
    @NotBlank(message = "消息内容不能为空")
    private String message;
    
    private String sessionId;
}
