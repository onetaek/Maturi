package com.maturi.config;

import com.maturi.interceptor.EncodingInterceptor;
import com.maturi.interceptor.LoginCheckInterceptor;
import com.maturi.util.argumentresolver.LoginMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //Interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //로그인한 유저인지 확인하기위한 Interceptor
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/member/join","/member/login","/test/**","/oauth/**",
                        "/https://**",
                        "/css/**","/js/**","/img/**","/*.ico","/html/**","/error");
        //인코딩을 하기위한 Interceptor
        registry.addInterceptor(new EncodingInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/js/**","/img/**","/*.ico","/html/**","/oauth");
    }

    //@Login어노테이션으로 세션에 있는 값을 편리하게 가져오기 위한 작업
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
