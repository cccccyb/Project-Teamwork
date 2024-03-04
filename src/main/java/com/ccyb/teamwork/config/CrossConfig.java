package com.ccyb.teamwork.config;


import com.ccyb.teamwork.config.handler.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CrossConfig implements WebMvcConfigurer {


    /**
     * 开启跨域
     *
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                //.allowedOrigins("http://localhost:8721")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders()
                .exposedHeaders("*")
//                .exposedHeaders("access-control-allow-headers","access-control-allow-methods","access-control-allow-origin","access-control-max-age","X-Frame-Options")
        ;

    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //排除拦截，除了登录(此时还没token)，其他都拦截
        registry.addInterceptor(jwtInterceptor())
                .excludePathPatterns("/", "/user/login")
                .addPathPatterns("/**");  // 拦截所有请求，通过判断token是否合法来决定是否需要登录

    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}