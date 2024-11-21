package com.springboot.yogijogii.config;


import com.springboot.yogijogii.handler.CustomAccessDeniedHandler;
import com.springboot.yogijogii.handler.CustomAuthenticationEntryPoint;
import com.springboot.yogijogii.jwt.JwtAuthenticationFilter;
import com.springboot.yogijogii.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends  WebSecurityConfigurerAdapter{

    private final JwtProvider jwtProvider;

    @Autowired
    public SecurityConfig(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable() // REST API는 UI를 사용하지 않으므로 기본설정을 비활성화
                .csrf().disable() // REST API는 csrf 보안이 필요 없으므로 비활성화

                .sessionManagement()
                .sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS) // JWT Token 인증방식으로 세션은 필요 없으므로 비활성화

                .and()
                .authorizeRequests() // 리퀘스트에 대한 사용권한 체크
                .antMatchers("/api/sign/**",
                        "/sign/exception","/main-api/**","/auth/**").permitAll() // 가입 및 로그인 주소는 허용
                .antMatchers("/api/team/**","/api/search/**","/api/team-strategy/**").authenticated()
                .antMatchers("/api/announcement/manager/**").hasRole("MANAGER")
                .antMatchers("/test/**").permitAll()
                .antMatchers("**exception**").permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class); // JWT Token 필터를 id/password 인증 필터 이전에 추가
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**", "/sign-api/exception");
    }


}
