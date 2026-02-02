package com.demo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Disable CSRF (JWT)
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // Preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 🔓 PUBLIC APIs
                .requestMatchers(
                    "/api/auth/**",
                    "/api/users/**",
                    "/api/states/**",
                    "/api/divisions/**",
                    "/productImages/**",
                    "/api/payments/**",
                    "/api/reviews/**"
                ).permitAll()

                // 🟢 PUBLIC PRODUCT READ
                .requestMatchers(HttpMethod.GET, "/api/products/**")
                .permitAll()

                // 🟡 BUYER: REGION PRODUCTS
                .requestMatchers("/api/products/region/**")
                .hasRole("BUYER")

                // 🛒 BUYER: CART
                .requestMatchers("/api/cart/**")
                .hasRole("BUYER")

                // 🧑‍🌾 SELLER / ADMIN: PRODUCT WRITE
                .requestMatchers(HttpMethod.POST, "/api/products/**")
                .hasAnyRole("SELLER", "ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/products/**")
                .hasAnyRole("SELLER", "ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/products/**")
                .hasAnyRole("SELLER", "ADMIN")

                // 📦 SELLER ORDERS
                .requestMatchers("/api/orders/seller/**")
                .authenticated()

                // 🔐 EVERYTHING ELSE
                .anyRequest().authenticated()
            )

            // Stateless JWT
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        // JWT filter
        http.addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🌍 CORS CONFIG
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
            "http://localhost:*",
            "http://127.0.0.1:*"
        ));

        config.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        config.setAllowedHeaders(List.of("*"));

        config.setAllowCredentials(false); // ✅ IMPORTANT

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
