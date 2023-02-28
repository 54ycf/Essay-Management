package com.ecnu.config;

import com.ecnu.handler.JwtInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/essay/**") // 拦截的请求  表示拦截essay下所有
                .addPathPatterns("/comment/**")
                .addPathPatterns("/manage/**")
                .addPathPatterns("/file/**")
                .addPathPatterns("/user/updatePwd")
                .excludePathPatterns("/file/resource/**")
                .excludePathPatterns("/user/login") // 不拦截的请求  如登录接口
                .excludePathPatterns("/user/register");
    }
}
