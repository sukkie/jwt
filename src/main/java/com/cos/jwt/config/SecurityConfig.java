package com.cos.jwt.config;

import com.cos.jwt.config.auth.PrincipalDetailService;
import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터(현재 클래스가 필터임)가 스프링 필터체인에 등록이 됩니다.
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;

    private final PrincipalDetailService principalDetailService;

    // 해당 메소드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodPwd() {
        return new BCryptPasswordEncoder();
    }

    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(principalDetailService).passwordEncoder(encodPwd());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();  // csrf disable
        http.cors().disable();  // cors disable
        http.headers().frameOptions().disable();    // frame허용

        // filter3, filter2, filter1 순으로 실행
        http.addFilterBefore(new MyFilter3(), SecurityContextHolderFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 미사용
        .and()
        .addFilter(corsFilter)  // @CorssOrigin은 인증이 없을때만 가능
        .formLogin().disable()  // 로그인창 미사용
        .httpBasic().disable()  // 기본적인 http요청 미사용 (허더의 Authorization에 id,pw 담아서 보내는 방식은 미사용)
        .addFilter(new JwtAuthenticationFilter())

        .authorizeRequests()
        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')")
        .anyRequest().permitAll();
        return http.build();
    }

}
