package com.smarttruck.presentation.exception;

import java.time.Instant;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.core.AuthenticationException;
import com.smarttruck.presentation.dto.ErrorResponse;

/**
 * Manipulador global de exceções para todos os controllers.
 * Traduz exceções em respostas HTTP padronizadas (ErrorResponse).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            final AuthenticationException ex) {
        final ErrorResponse error = new ErrorResponse(
            "Token de autenticação inválido ou expirado",
            Instant.now(),
            new ErrorResponse.ErrorDetails("TOKEN_INVALID", null)
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            final MethodArgumentNotValidException ex) {
        final String field = ex.getBindingResult().getFieldError() != null
            ? ex.getBindingResult().getFieldError().getField()
            : "unknown";
        final String reason = ex.getBindingResult().getFieldError() != null
            ? ex.getBindingResult().getFieldError().getDefaultMessage()
            : "Validation error";

        final ErrorResponse error = new ErrorResponse(
            "Parâmetros de paginação inválidos",
            Instant.now(),
            new ErrorResponse.ErrorDetails("INVALID_PARAMETER", field + ": " + reason)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            final ConstraintViolationException ex) {
        final String violations = ex.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining(", "));

        final ErrorResponse error = new ErrorResponse(
            "Parâmetros de paginação inválidos",
            Instant.now(),
            new ErrorResponse.ErrorDetails("INVALID_PARAMETER", violations)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            final MethodArgumentTypeMismatchException ex) {
        final ErrorResponse error = new ErrorResponse(
            "Parâmetros de paginação inválidos",
            Instant.now(),
            new ErrorResponse.ErrorDetails("INVALID_PARAMETER",
                ex.getName() + " deve ser um número")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex) {
        final ErrorResponse error = new ErrorResponse(
            "Serviço temporariamente indisponível",
            Instant.now(),
            new ErrorResponse.ErrorDetails("DATABASE_ERROR", ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
}
