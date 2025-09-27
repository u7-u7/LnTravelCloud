package com.lntravel.user.controller;

import com.lntravel.common.result.Result;
import com.lntravel.user.dto.LoginForm;
import com.lntravel.user.dto.LoginResponseDTO;
import com.lntravel.user.dto.RegisterForm;
import com.lntravel.user.dto.UserInfoDTO;
import com.lntravel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@Valid @RequestBody LoginForm loginForm) {
        log.info("用户登录: {}", loginForm.getUsername());
        LoginResponseDTO response = userService.login(loginForm);
        return Result.success("登录成功", response);
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterForm registerForm) {
        log.info("用户注册: {}", registerForm.getUsername());
        userService.register(registerForm);
        return Result.success("注册成功");
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoDTO> getUserInfo(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        UserInfoDTO userInfo = userService.getUserInfo(userId);
        return Result.success(userInfo);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<String> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO,
                                       HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        userService.updateUserInfo(userId, userInfoDTO);
        return Result.success("更新成功");
    }
    
    /**
     * 验证用户名是否存在
     */
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return Result.success(exists);
    }
    
    /**
     * 验证邮箱是否存在
     */
    @GetMapping("/check-email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return Result.success(exists);
    }
    
    /**
     * 验证手机号是否存在
     */
    @GetMapping("/check-phone")
    public Result<Boolean> checkPhone(@RequestParam String phone) {
        boolean exists = userService.existsByPhone(phone);
        return Result.success(exists);
    }
    
    /**
     * 从请求中获取当前用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return com.lntravel.common.utils.JwtUtils.getUserIdFromToken(token);
        }
        throw new com.lntravel.common.exception.BusinessException("未登录");
    }
}
