package org.example.serveur.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Personnalisation de BCrypt pour désactiver les salts dynamiques
        return new BCryptPasswordEncoder(10) {
            @Override
            public String encode(CharSequence rawPassword) {
                String fixedSalt = "$2a$10$abcdefghijklmnopqrstuv"; // Salt fixe
                return super.encode(rawPassword + fixedSalt);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                String fixedSalt = "$2a$10$abcdefghijklmnopqrstuv"; // Salt fixe
                return super.matches(rawPassword + fixedSalt, encodedPassword);
            }
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("http://localhost:4200")); // URL du frontend Angular
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    return corsConfig;
                }))
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // Permettre toutes les requêtes
                );

        return http.build();
    }
}
