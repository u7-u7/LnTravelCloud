package com.lntravel.user.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户信息DTO
 */
@Data
public class UserInfoDTO {
    
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String nickname;
    private String avatar;
    private Integer gender;
    private LocalDate birthday;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
