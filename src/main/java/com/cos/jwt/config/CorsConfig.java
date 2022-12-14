package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);   // 서버가 응답할때 json을 자바스크립트에서 처리 할 수 있게를 설정
        config.addAllowedOrigin("*");   // 모든 도메인 허용
        config.addAllowedHeader("*");   // 모든 header 허용
        config.addAllowedMethod("*");   // 모든 메소드 허용
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
