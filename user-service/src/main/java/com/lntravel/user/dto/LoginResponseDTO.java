package com.lntravel.user.dto;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponseDTO {
    
    private String token;
    private UserInfoDTO userInfo;
    
    public LoginResponseDTO(String token, UserInfoDTO userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }
}
