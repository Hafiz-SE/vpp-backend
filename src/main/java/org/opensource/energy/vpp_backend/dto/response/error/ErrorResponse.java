package org.opensource.energy.vpp_backend.dto.response.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String message;
    private String errorCode;
    private String path;
    @Builder.Default
    private Instant timestamp = Instant.now();
    private List<FieldErrorDetail> errors;
}