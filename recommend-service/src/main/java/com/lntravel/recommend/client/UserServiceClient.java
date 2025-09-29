package com.lntravel.recommend.client;

import com.lntravel.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "user-service", path = "/user")
public interface UserServiceClient {
    
    /**
     * 获取用户信息
     */
    @GetMapping("/info/{id}")
    Result<Object> getUserInfo(@PathVariable("id") Long id);
    
    /**
     * 获取用户足迹
     */
    @GetMapping("/footprints/{userId}")
    Result<List<Object>> getUserFootprints(@PathVariable("userId") Long userId);
    
    /**
     * 获取用户收藏
     */
    @GetMapping("/favorites/{userId}")
    Result<List<Object>> getUserFavorites(@PathVariable("userId") Long userId);
}
