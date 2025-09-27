package com.lntravel.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lntravel.user.dto.LoginForm;
import com.lntravel.user.dto.LoginResponseDTO;
import com.lntravel.user.dto.RegisterForm;
import com.lntravel.user.dto.UserInfoDTO;
import com.lntravel.user.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     */
    LoginResponseDTO login(LoginForm loginForm);
    
    /**
     * 用户注册
     */
    void register(RegisterForm registerForm);
    
    /**
     * 根据用户名获取用户信息
     */
    User getByUsername(String username);
    
    /**
     * 根据用户ID获取用户信息
     */
    UserInfoDTO getUserInfo(Long userId);
    
    /**
     * 更新用户信息
     */
    void updateUserInfo(Long userId, UserInfoDTO userInfoDTO);
    
    /**
     * 验证用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 验证邮箱是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 验证手机号是否存在
     */
    boolean existsByPhone(String phone);
}
