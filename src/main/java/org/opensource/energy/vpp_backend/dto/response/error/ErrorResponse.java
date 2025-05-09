package org.opensource.energy.vpp_backend.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String errorCode;
    private String path;
    @Builder.Default
    private Instant timestamp = Instant.now();
    private List<FieldErrorDetail> errors;
}