package com.mall_server.mallserver.config;

import com.mall_server.mallserver.interceptor.SysInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web项目配置类
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE","OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/swiper/**").addResourceLocations("file:.\\images\\swiperImgs\\");
        registry.addResourceHandler("/image/bigType/**").addResourceLocations("file:.\\images\\bigTypeImgs\\");
        registry.addResourceHandler("/image/product/**").addResourceLocations("file:.\\images\\productImgs\\");
        registry.addResourceHandler("/image/productSwiperImgs/**").addResourceLocations("file:.\\images\\productSwiperImgs\\");
        registry.addResourceHandler("/image/productIntroImgs/**").addResourceLocations("file:.\\images\\productIntroImgs\\");
        registry.addResourceHandler("/image/productParaImgs/**").addResourceLocations("file:.\\images\\productParaImgs\\");
    }
    @Bean
    public SysInterceptor sysInterceptor(){
        return new SysInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] patterns=new String[]{"/adminLogin","/product/**","/bigType/**","/user/wxlogin","/weixinpay/**"};
        registry.addInterceptor(sysInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(patterns);
    }
}
