package com.vsd.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttp ->
                        authorizeHttp
                                .requestMatchers(
                                        "/",
                                        "/home",
                                        "/login",
                                        "/register",
                                        "/articles/**",
                                        "/article/**",
                                        "/categories/**",
                                        "/explore/**",
                                        "/create-article",
                                        "/edit-article/**",
                                        "/dashboard",
                                        "/my-articles",
                                        "/admin",
                                        "/admin/**",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/favicon.ico"
                                ).permitAll()
                                .requestMatchers("/api/v1/auth/login", "/api/v1/auth/user").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/article/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/category/**").permitAll()
                                .anyRequest()
                                .authenticated());

        http.sessionManagement(session->session.sessionCreationPolicy
                (SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
