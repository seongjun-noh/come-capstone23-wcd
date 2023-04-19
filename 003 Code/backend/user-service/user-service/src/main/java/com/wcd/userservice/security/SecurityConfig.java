package com.wcd.userservice.security;

import com.wcd.userservice.security.jwt.JwtTokenProvider;
import com.wcd.userservice.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    // 권한과 관련한 메서드
    // HttpSecurity: HTTP 요청에 대한 보안 구성 지정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 사용자 인증 매니저, 인증 매니저는 사용자의 인증 정보를 확인
        AuthenticationManager authenticationManager = getAuthenticationManager(http);

        http.csrf().disable();

        http.authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
                .and()
                .authenticationManager(authenticationManager)
                .addFilter(getAuthenticationFilter(authenticationManager));

        http.headers().frameOptions().disable();

        return http.build();
    }

    private AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
        // 인증 매니저를 구성하는 빌더 클래스
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // userDetailService: 사용자 인증 정보를 검색할 때 사용하는 서비스 (userService)
        // passwordEncoder: 패스워드 인코딩을 위해 사용
        authenticationManagerBuilder.userDetailsService(myUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, myUserDetailsService, env, redisTemplate, jwtTokenProvider);
    }
}
