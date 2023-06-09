package com.developers.live.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 모든 경로에 대해 적용
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("*") // 모든 HTTP 메서드 허용
            .allowCredentials(true) // credentials 허용 설정 추가
            .maxAge(3600); // 캐시 유효 시간 설정
  }
}
