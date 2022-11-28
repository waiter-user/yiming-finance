package com.java.ymjr.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;


@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //允许携带Cookie
        config.addAllowedOrigin("*"); //设置允许哪些url访问跨域资源,*表示全部允许
        config.addAllowedHeader("*"); //允许访问的头信息,*表示全部
        config.addAllowedMethod("*");//允许所有请求方法(GET,POST等)访问跨域资源
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间内，对于相同的跨域请求不会再预检了

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
