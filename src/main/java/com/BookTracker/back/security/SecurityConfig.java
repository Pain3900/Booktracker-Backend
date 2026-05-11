package com.BookTracker.back.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF, так как мы используем JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Настраиваем доступы к эндпоинтам
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем доступ без токена к регистрации и логину
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Все остальные запросы (создание книг, карточек) требуют токена
                        .anyRequest().authenticated()
                )

                // Делаем сессии Stateless (Spring не будет запоминать юзера между запросами, проверяем токен каждый раз)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Указываем провайдер аутентификации (откуда брать юзеров из БД)
                .authenticationProvider(authenticationProvider)

                // Ставим наш JWT фильтр ДО стандартного фильтра проверки логина/пароля
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
