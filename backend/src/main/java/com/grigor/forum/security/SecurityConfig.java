package com.grigor.forum.security;

import com.grigor.forum.enums.UserRole;
import com.grigor.forum.exceptions.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        //.requestMatchers(HttpMethod.GET,"api/forums/comments/not-approved-comments")
                        //.hasAnyAuthority(UserRole.ADMIN.toString().toLowerCase(), UserRole.MODER.toString().toLowerCase())
                        .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/rooms").authenticated()
                        .requestMatchers(HttpMethod.POST,"/comments").hasAuthority("post")
                        .requestMatchers(HttpMethod.PUT, "/comments").hasAuthority("edit")
                        .requestMatchers(HttpMethod.DELETE, "/comments/*").hasAuthority("delete")
                        .requestMatchers(HttpMethod.GET, "/comments/*").hasAuthority("delete")
                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority(UserRole.ADMIN.toString().toLowerCase())
                        .requestMatchers(HttpMethod.PUT, "/users/verify/*").hasAuthority(UserRole.ADMIN.toString().toLowerCase())
                        .requestMatchers(HttpMethod.PUT, "/users/*").hasAuthority(UserRole.ADMIN.toString().toLowerCase())
                        .anyRequest()
                        .denyAll())
                .sessionManagement((manager) -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(globalExceptionHandler)
                //.accessDeniedHandler(customAccessDeniedHandler)
        );

        return http.build();
    }

}
