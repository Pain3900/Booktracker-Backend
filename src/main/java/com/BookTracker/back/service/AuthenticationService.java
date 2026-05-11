package com.BookTracker.back.service;

import com.BookTracker.back.dto.AuthRequest;
import com.BookTracker.back.dto.AuthResponse;
import com.BookTracker.back.model.entity.User;
import com.BookTracker.back.repository.UserRepository;
import com.BookTracker.back.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        // Проверяем, нет ли уже такого пользователя
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        // Создаем пользователя и хешируем пароль
        var user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        repository.save(user);

        // Генерируем токен
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        // Spring Security сам проверит совпадение логина и пароля
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Если дошли сюда, значит пароль верный. Достаем юзера из БД
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        // Генерируем новый токен
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}