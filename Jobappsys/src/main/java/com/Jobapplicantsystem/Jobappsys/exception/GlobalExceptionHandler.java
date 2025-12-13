package com.Jobapplicantsystem.Jobappsys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }

    // Return 401 for authentication issues instead of 500
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuth(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    // Treat generic runtime exceptions with a safe message
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        return build(HttpStatus.BAD_REQUEST, "Validation failed");
    }

    // Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOther(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");
    }
}
