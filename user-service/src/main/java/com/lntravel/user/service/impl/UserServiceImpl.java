package com.lntravel.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lntravel.common.exception.BusinessException;
import com.lntravel.common.utils.JwtUtils;
import com.lntravel.user.dto.LoginForm;
import com.lntravel.user.dto.LoginResponseDTO;
import com.lntravel.user.dto.RegisterForm;
import com.lntravel.user.dto.UserInfoDTO;
import com.lntravel.user.entity.User;
import com.lntravel.user.mapper.UserMapper;
import com.lntravel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public LoginResponseDTO login(LoginForm loginForm) {
        // 根据用户名查询用户
        User user = getByUsername(loginForm.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账户已被禁用");
        }
        
        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtils.generateToken(claims);
        
        // 转换为DTO
        UserInfoDTO userInfoDTO = BeanUtil.copyProperties(user, UserInfoDTO.class);
        
        return new LoginResponseDTO(token, userInfoDTO);
    }
    
    @Override
    public void register(RegisterForm registerForm) {
        // 验证用户名是否已存在
        if (existsByUsername(registerForm.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 验证邮箱是否已存在
        if (existsByEmail(registerForm.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }
        
        // 验证手机号是否已存在
        if (existsByPhone(registerForm.getPhone())) {
            throw new BusinessException("手机号已被注册");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerForm.getUsername());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setPhone(registerForm.getPhone());
        user.setNickname(StrUtil.isNotBlank(registerForm.getNickname()) ? 
            registerForm.getNickname() : registerForm.getUsername());
        user.setStatus(1); // 默认启用
        
        save(user);
    }
    
    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }
    
    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return BeanUtil.copyProperties(user, UserInfoDTO.class);
    }
    
    @Override
    public void updateUserInfo(Long userId, UserInfoDTO userInfoDTO) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 更新用户信息
        if (StrUtil.isNotBlank(userInfoDTO.getNickname())) {
            user.setNickname(userInfoDTO.getNickname());
        }
        if (StrUtil.isNotBlank(userInfoDTO.getEmail())) {
            user.setEmail(userInfoDTO.getEmail());
        }
        if (StrUtil.isNotBlank(userInfoDTO.getPhone())) {
            user.setPhone(userInfoDTO.getPhone());
        }
        if (StrUtil.isNotBlank(userInfoDTO.getAvatar())) {
            user.setAvatar(userInfoDTO.getAvatar());
        }
        if (userInfoDTO.getGender() != null) {
            user.setGender(userInfoDTO.getGender());
        }
        if (userInfoDTO.getBirthday() != null) {
            user.setBirthday(userInfoDTO.getBirthday());
        }
        
        updateById(user);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return count(wrapper) > 0;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        return count(wrapper) > 0;
    }
    
    @Override
    public boolean existsByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        return count(wrapper) > 0;
    }
}
