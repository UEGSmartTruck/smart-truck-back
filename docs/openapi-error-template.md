# SmartTruck Backend - API Error Response Standard

## Overview

This document defines the standard JSON structure for error responses across all SmartTruck API endpoints. Consistent error formatting improves developer experience and enables predictable error handling in client applications.

---

## Base Error Response Structure

```json
{
  "message": "Human-readable error description",
  "timestamp": "ISO 8601 timestamp",
  "details": {
    "code": "ERROR_CODE_CONSTANT",
    "field": "fieldName",
    "rejectedValue": "invalidValue",
    "traceId": "correlation-id-for-tracing"
  }
}
```

### Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `message` | string | ✅ | User-friendly error message in Portuguese |
| `timestamp` | string | ✅ | ISO 8601 format: `2025-12-03T10:15:30.123Z` |
| `details` | object | ✅ | Additional error context |
| `details.code` | string | ✅ | Machine-readable error code (UPPER_SNAKE_CASE) |
| `details.field` | string | ❌ | Field name for validation errors |
| `details.rejectedValue` | any | ❌ | Invalid value that was rejected |
| `details.traceId` | string | ❌ | Correlation ID for log aggregation |

---

## Error Codes by HTTP Status

### 400 Bad Request

**VALIDATION_ERROR** - Input validation failed
```json
{
  "message": "Dados inválidos na requisição",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "VALIDATION_ERROR",
    "errors": [
      {
        "field": "email",
        "message": "Email inválido",
        "rejectedValue": "not-an-email"
      },
      {
        "field": "password",
        "message": "Senha muito curta (mínimo 8 caracteres)",
        "rejectedValue": "123"
      }
    ]
  }
}
```

**MISSING_REQUIRED_FIELD** - Required field not provided
```json
{
  "message": "Campo obrigatório ausente",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "MISSING_REQUIRED_FIELD",
    "field": "email",
    "message": "O campo 'email' é obrigatório"
  }
}
```

### 401 Unauthorized

**INVALID_CREDENTIALS** - Authentication failed
```json
{
  "message": "Usuário ou senha inválidos",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "INVALID_CREDENTIALS"
  }
}
```

**TOKEN_EXPIRED** - JWT token expired
```json
{
  "message": "Token de autenticação expirado",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "TOKEN_EXPIRED",
    "expiresAt": "2025-12-03T09:00:00.000Z"
  }
}
```

**TOKEN_INVALID** - Malformed or tampered token
```json
{
  "message": "Token de autenticação inválido",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "TOKEN_INVALID",
    "reason": "Signature verification failed"
  }
}
```

**TOKEN_BLACKLISTED** - Token revoked (user logged out)
```json
{
  "message": "Sessão encerrada. Faça login novamente.",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "TOKEN_BLACKLISTED"
  }
}
```

### 403 Forbidden

**INSUFFICIENT_PERMISSIONS** - User lacks required role/permission
```json
{
  "message": "Permissão insuficiente para acessar este recurso",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "INSUFFICIENT_PERMISSIONS",
    "requiredRole": "ADMIN",
    "userRole": "USER"
  }
}
```

### 404 Not Found

**RESOURCE_NOT_FOUND** - Requested resource doesn't exist
```json
{
  "message": "Recurso não encontrado",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "RESOURCE_NOT_FOUND",
    "resource": "Ticket",
    "identifier": "ticket-123"
  }
}
```

**USER_NOT_FOUND** - User lookup failed
```json
{
  "message": "Usuário não encontrado",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "USER_NOT_FOUND",
    "userId": "user-456"
  }
}
```

### 409 Conflict

**DUPLICATE_RESOURCE** - Resource already exists
```json
{
  "message": "Recurso já existe",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "DUPLICATE_RESOURCE",
    "resource": "User",
    "field": "email",
    "existingValue": "existing@example.com"
  }
}
```

**CONCURRENT_MODIFICATION** - Optimistic locking conflict
```json
{
  "message": "Recurso foi modificado por outro usuário. Recarregue e tente novamente.",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "CONCURRENT_MODIFICATION",
    "resource": "Ticket",
    "identifier": "ticket-789",
    "expectedVersion": 5,
    "actualVersion": 6
  }
}
```

### 422 Unprocessable Entity

**BUSINESS_RULE_VIOLATION** - Domain logic constraint failed
```json
{
  "message": "Ticket já está resolvido e não pode ser reaberto",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "BUSINESS_RULE_VIOLATION",
    "rule": "RESOLVED_TICKET_CANNOT_BE_REOPENED",
    "currentStatus": "RESOLVED"
  }
}
```

### 429 Too Many Requests

**RATE_LIMIT_EXCEEDED** - Client exceeded rate limit
```json
{
  "message": "Limite de requisições excedido. Tente novamente em 60 segundos.",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "RATE_LIMIT_EXCEEDED",
    "limit": 100,
    "window": "1 minute",
    "retryAfter": 60
  }
}
```

### 500 Internal Server Error

**INTERNAL_ERROR** - Unexpected server error
```json
{
  "message": "Erro interno do servidor. Nosso time foi notificado.",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "INTERNAL_ERROR",
    "traceId": "3f2a1b4c-5d6e-7f8g-9h0i-1j2k3l4m5n6o"
  }
}
```

**DATABASE_ERROR** - Database connection/query failed
```json
{
  "message": "Erro ao acessar banco de dados. Tente novamente.",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "DATABASE_ERROR",
    "traceId": "7h8i9j0k-1l2m-3n4o-5p6q-7r8s9t0u1v2w"
  }
}
```

### 503 Service Unavailable

**SERVICE_UNAVAILABLE** - Service temporarily down (maintenance, overload)
```json
{
  "message": "Serviço temporariamente indisponível. Tente novamente em alguns minutos.",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "SERVICE_UNAVAILABLE",
    "retryAfter": 300
  }
}
```

---

## Implementation in Spring Boot

### ErrorResponse DTO

```java
package com.smarttruck.presentation.dto;

import java.time.Instant;
import java.util.Map;

/**
 * Standardized error response for all API endpoints.
 */
public record ErrorResponse(
    String message,
    String timestamp,
    Map<String, Object> details
) {
    public ErrorResponse(String message, String code) {
        this(message, Instant.now().toString(), Map.of("code", code));
    }

    public ErrorResponse(String message, String code, Map<String, Object> additionalDetails) {
        this(message, Instant.now().toString(),
            mergeMaps(Map.of("code", code), additionalDetails));
    }

    private static Map<String, Object> mergeMaps(Map<String, Object> map1, Map<String, Object> map2) {
        Map<String, Object> merged = new java.util.HashMap<>(map1);
        merged.putAll(map2);
        return merged;
    }
}
```

### GlobalExceptionHandler

```java
package com.smarttruck.presentation.exception;

import com.smarttruck.presentation.dto.ErrorResponse;
import com.smarttruck.shared.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("Usuário ou senha inválidos", "INVALID_CREDENTIALS"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        logger.warn("User not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("Usuário não encontrado", "USER_NOT_FOUND"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> Map.of(
                "field", error.getField(),
                "message", error.getDefaultMessage(),
                "rejectedValue", String.valueOf(error.getRejectedValue())
            ))
            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                "Dados inválidos na requisição",
                "VALIDATION_ERROR",
                Map.of("errors", errors)
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        String traceId = UUID.randomUUID().toString();
        logger.error("Unexpected error [traceId={}]", traceId, ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                "Erro interno do servidor. Nosso time foi notificado.",
                "INTERNAL_ERROR",
                Map.of("traceId", traceId)
            ));
    }
}
```

---

## Client-Side Error Handling

### TypeScript Example

```typescript
interface ErrorResponse {
  message: string;
  timestamp: string;
  details: {
    code: string;
    [key: string]: any;
  };
}

async function loginUser(email: string, password: string) {
  try {
    const response = await fetch('/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
      const error: ErrorResponse = await response.json();

      switch (error.details.code) {
        case 'INVALID_CREDENTIALS':
          showNotification('Usuário ou senha incorretos', 'error');
          break;
        case 'VALIDATION_ERROR':
          showValidationErrors(error.details.errors);
          break;
        case 'RATE_LIMIT_EXCEEDED':
          showNotification(`Muitas tentativas. Aguarde ${error.details.retryAfter}s`, 'warning');
          break;
        default:
          showNotification('Erro ao fazer login', 'error');
      }

      return null;
    }

    return await response.json();
  } catch (networkError) {
    showNotification('Erro de conexão. Verifique sua internet.', 'error');
    return null;
  }
}
```

---

## Testing Error Responses

### Contract Test Example

```java
@WebMvcTest(LoginController.class)
class LoginControllerErrorContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticateUserUseCase useCase;

    @Test
    void invalidCredentials_shouldReturnStandardErrorFormat() throws Exception {
        when(useCase.execute(any(), any()))
            .thenThrow(new InvalidCredentialsException("test@test.com"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"password\":\"wrong\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Usuário ou senha inválidos"))
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.details.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void validationError_shouldIncludeFieldDetails() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"invalid\",\"password\":\"\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.details.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.details.errors[0].field").exists())
            .andExpect(jsonPath("$.details.errors[0].message").exists());
    }
}
```

---

## Best Practices

### ✅ DO

- Use consistent error codes (UPPER_SNAKE_CASE)
- Include correlation IDs for 5xx errors
- Log all errors with context (userId, requestId, etc.)
- Return 401 for authentication errors, 403 for authorization errors
- Keep error messages user-friendly (Portuguese)

### ❌ DON'T

- Expose stack traces in production
- Include sensitive data in error messages (passwords, tokens)
- Use HTTP 200 for error responses
- Return different structures for different endpoints
- Log sensitive data (use placeholders: `user logged in: {}` instead of logging password)

---

## OpenAPI Specification

```yaml
components:
  schemas:
    ErrorResponse:
      type: object
      required:
        - message
        - timestamp
        - details
      properties:
        message:
          type: string
          description: Human-readable error message
          example: "Usuário ou senha inválidos"
        timestamp:
          type: string
          format: date-time
          description: ISO 8601 timestamp
          example: "2025-12-03T10:15:30.123Z"
        details:
          type: object
          required:
            - code
          properties:
            code:
              type: string
              description: Machine-readable error code
              example: "INVALID_CREDENTIALS"
          additionalProperties: true

  responses:
    BadRequest:
      description: Invalid request
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/ErrorResponse'
          example:
            message: "Dados inválidos na requisição"
            timestamp: "2025-12-03T10:15:30.123Z"
            details:
              code: "VALIDATION_ERROR"
              errors:
                - field: "email"
                  message: "Email inválido"

    Unauthorized:
      description: Authentication required
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/ErrorResponse'
          example:
            message: "Usuário ou senha inválidos"
            timestamp: "2025-12-03T10:15:30.123Z"
            details:
              code: "INVALID_CREDENTIALS"

    NotFound:
      description: Resource not found
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/ErrorResponse'
          example:
            message: "Recurso não encontrado"
            timestamp: "2025-12-03T10:15:30.123Z"
            details:
              code: "RESOURCE_NOT_FOUND"
              resource: "Ticket"
              identifier: "ticket-123"
```

---

**Version**: 1.0.0
**Last Updated**: 2025-12-03
**Maintained by**: SmartTruck Backend Team
