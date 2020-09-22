package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.config;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.interceptor.LoginForAdminInterceptor;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.interceptor.LoginForSuperAdminInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedList;

/**
 * 拦截器配置
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //1.加入的顺序就是拦截器执行的顺序，
        //2.按顺序执行所有拦截器的preHandle
        //3.所有的preHandle 执行完再执行全部postHandle 最后是postHandle
        //普通用户登陆的拦截器
       registry.addInterceptor(loginForAdminInterceptor())
                .addPathPatterns("/api/backstage/**")//要拦截的路径
                .excludePathPatterns(new LinkedList<String>(){{//不拦截的路径（一般指addPathPatterns中包含，但是不进行拦截的特例）
                    add("/api/backstage/login");
                    add("/api/backstage/logout");
                }});
       //管理员登陆拦截器
        registry.addInterceptor(loginForSuperAdminInterceptor())
                .addPathPatterns("/api/backstage/adminmanage/**")//要拦截的路径
                .excludePathPatterns(new LinkedList<String>(){{//不拦截的路径（一般指addPathPatterns中包含，但是不进行拦截的特例）
                    add("/api/backstage/login");
                    add("/api/backstage/logout");
                }});
    }

    //创建拦截器对象：用户登陆状态验证
    @Bean
    public LoginForAdminInterceptor loginForAdminInterceptor() {
        return new LoginForAdminInterceptor();
    }

    //创建拦截器对象：超级用户权限验证
    @Bean
    public LoginForSuperAdminInterceptor loginForSuperAdminInterceptor() {
        return new LoginForSuperAdminInterceptor();
    }
}
