package com.esg.bbollock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()  // 🔹 CORS 활성화
            .csrf().disable()  // 🔹 CSRF 비활성화
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());  // 🔹 모든 요청 허용

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // 🔹 인증 정보 포함 허용
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://paulzzak.vercel.app"));  // 🔹 React 프론트엔드 도메인 + Vercel 도메인 추가
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // 🔹 허용할 HTTP 메서드
        config.setAllowedHeaders(Arrays.asList("*"));  // 🔹 모든 헤더 허용
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}