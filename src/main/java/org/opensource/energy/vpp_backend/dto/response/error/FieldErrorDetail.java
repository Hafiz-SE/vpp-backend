package org.opensource.energy.vpp_backend.dto.response.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldErrorDetail {
    private String field;
    private String message;
}
