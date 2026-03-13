package com.course.gateway.config;

import com.course.gateway.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtUtil jwtUtil) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(HttpMethod.GET, "/courses/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/courses/**").hasAnyRole("FORMATEUR", "ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/courses/**").hasAnyRole("FORMATEUR", "ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/courses/**").hasAnyRole("FORMATEUR", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/inscriptions/**").hasAnyRole("APPRENTI", "ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/inscriptions/**").hasAnyRole("APPRENTI", "ADMIN")
                        .pathMatchers(HttpMethod.GET, "/inscriptions/utilisateur/**").hasAnyRole("APPRENTI", "FORMATEUR", "ADMIN")
                        .pathMatchers(HttpMethod.GET, "/inscriptions/formation/**").hasAnyRole("FORMATEUR", "ADMIN")
                        .pathMatchers(HttpMethod.GET, "/inscriptions/**").authenticated()
                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtAuthenticationFilter(jwtUtil), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}