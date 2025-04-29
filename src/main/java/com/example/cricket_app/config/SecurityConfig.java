package com.example.cricket_app.config;

import com.example.cricket_app.security.AuthEntryPointJwt;
import com.example.cricket_app.security.AuthTokenFilter;
import com.example.cricket_app.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private AuthTokenFilter authTokenFilter;
    private JwtUtils jwtUtils;
    private AuthEntryPointJwt authEntryPointJwt;

    @Autowired
    public SecurityConfig(AuthTokenFilter authTokenFilter, JwtUtils jwtUtils, AuthEntryPointJwt authEntryPointJwt) {
        this.authTokenFilter = authTokenFilter;
        this.jwtUtils = jwtUtils;
        this.authEntryPointJwt = authEntryPointJwt;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(
                        "/api/auth/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt));
        http.csrf(csrf -> csrf.disable());
        http.addFilterBefore(authTokenFilter,
                UsernamePasswordAuthenticationFilter.class);//run authTokenFilter before running the username password.
        return http.build();
    }
}
