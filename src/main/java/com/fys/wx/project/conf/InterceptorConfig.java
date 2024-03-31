package com.fys.wx.project.conf;

import com.fys.wx.project.interceptors.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fys
 * @date 2024/4/1
 * @description
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                //放行微信相关接口
                .excludePathPatterns("/wx/**")
                //放行登录接口
                .excludePathPatterns("/user/login");
    }
}
