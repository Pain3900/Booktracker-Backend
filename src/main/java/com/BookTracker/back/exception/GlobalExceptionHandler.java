package com.BookTracker.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), LocalDateTime.now());

        // Если ошибка связана с доступом, возвращаем 403 Forbidden, иначе 400 Bad Request
        if (ex.getMessage().contains("прав") || ex.getMessage().contains("запрещен")) {
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ex.printStackTrace();
        ErrorResponse error = new ErrorResponse("Внутренняя ошибка сервера", LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}