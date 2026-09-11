package com.webarch.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Use allowedOriginPatterns instead of a wildcard "*" when allowCredentials=true
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:[*]",   // any localhost port (covers Swagger UI at :8080 and the app)
            "http://127.0.0.1:[*]",
            "http://frontend:*"
        ));
        config.setAllowedHeaders(Arrays.asList(
            "Origin", "Content-Type", "Accept",
            "Authorization", "X-Requested-With", "X-Internal-Api-Key"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Link", "X-Total-Count"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
