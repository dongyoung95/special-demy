package com.specialdemy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/img/**", "/video/**", "/favicon.ico")
                        .permitAll()
                        .requestMatchers("/admin/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/inquiries", true)
                        .failureUrl("/admin/login?error")
                        .permitAll())
                .logout(log -> log
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());
        return http.build();
    }
}
