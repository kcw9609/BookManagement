package com.example.study.config;

import com.example.study.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity // 스프링 시큐리티 설정을 담당한다는 의미 기본적으로 csrf활성화
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{ // HttpSecurity 객체를 통해 보안 관련 설정을 함(제공해줌)
        try{
            // http 시큐리터 빌더
            http.cors()
                    .and()
                    .csrf().disable() // csrf는 현재 사용하지 않으므로 disable

                    .httpBasic().disable()// token를 사용하므로 basic인증 disable

                    .sessionManagement() // session기반이 아님을 선언 = 각 요청마다 확인
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()

                    .authorizeRequests()// /와 /auth/** 경로는 인증 안해도 됨
                        .antMatchers("/", "/auth/**").permitAll()

                    .anyRequest() // 나머지 경로는 인증해야 함
                        .authenticated();
            
            
            
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        // filter등록, 매 요청마다 CorsFilter실행한 후에 jwtAuthenticationFilter실행한다.
        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );
        
    }
    
}































