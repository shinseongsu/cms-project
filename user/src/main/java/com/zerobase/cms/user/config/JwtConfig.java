package com.zerobase.cms.user.config;

import com.zerobase.domain.config.JwtAuthenticationprovider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtAuthenticationprovider jwtAuthenticationprovider() {
        return new JwtAuthenticationprovider();
    }

}
