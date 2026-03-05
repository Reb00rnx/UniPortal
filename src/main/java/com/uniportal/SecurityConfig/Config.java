package com.uniportal.SecurityConfig;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class Config {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    public Config(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req -> req
            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users/students", "/api/users/teachers").permitAll()
            .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
            .requestMatchers("/api/schedule/**").permitAll()
            .requestMatchers(
                "/api/courses/all",
                "/api/courses/{courseId}/enroll/{studentId}",
                "/api/courses/{courseId}",
                "/api/grade/**"
            ).hasAnyRole("STUDENT", "TEACHER","ADMIN")
            .anyRequest().authenticated())
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}


//@Bean
//SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//        .csrf(AbstractHttpConfigurer::disable)
//        .authorizeHttpRequests(req -> req
//            .anyRequest().permitAll() // To otworzy absolutnie wszystkie endpointy
//        )
//        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
//        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        // Możesz tymczasowo zakomentować authenticationProvider i filtr JWT,
//        // żeby nie przeszkadzały, jeśli nie przesyłasz tokenów.
//        // .authenticationProvider(authenticationProvider)
//        // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//    return http.build();
//}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}


