package org.opensource.energy.vpp_backend.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.opensource.energy.vpp_backend.dto.response.error.ErrorResponse;
import org.opensource.energy.vpp_backend.dto.response.error.FieldErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String DEFAULT_VALIDATION_ERROR_MESSAGE = "Validation failed";
    private static final String DEFAULT_VALIDATION_ERROR_CODE = "VAL-000";

    private static final String GENERIC_ERROR_MESSAGE = "An unexpected error occurred. Please try again later.";
    private static final String GENERIC_ERROR_CODE = "GEN-500";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<FieldErrorDetail> errorDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> FieldErrorDetail.builder()
                        .field(Optional.of(error.getField()).orElse("unknown"))
                        .message(Optional.ofNullable(error.getDefaultMessage()).orElse("invalid"))
                        .build())
                .toList();

        log.warn("Validation failed for request {} - {} field error(s)",
                request.getRequestURI(), errorDetails.size());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(DEFAULT_VALIDATION_ERROR_MESSAGE)
                .errorCode(DEFAULT_VALIDATION_ERROR_CODE)
                .path(request.getRequestURI())
                .errors(errorDetails)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception at [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.builder()
                .message(GENERIC_ERROR_MESSAGE)
                .errorCode(GENERIC_ERROR_CODE)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
