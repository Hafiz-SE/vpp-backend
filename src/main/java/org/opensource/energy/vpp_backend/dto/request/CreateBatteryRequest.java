package org.opensource.energy.vpp_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "Request object for creating a new battery")
@Builder
public class CreateBatteryRequest {
    @Schema(description = "Battery name", example = "Battery-A1")
    @NotBlank(message = "Name is required")
    @Size(max = 512, message = "Name must not exceed 512 characters")
    private String name;

    @Schema(description = "Postcode where the battery is located", example = "2000")
    @NotNull(message = "Postcode is required")
    @PositiveOrZero(message = "Postcode cannot be negative")
    private Integer postcode;

    @Schema(description = "Battery watt capacity", example = "1500")
    @NotNull(message = "Capacity is required")
    @PositiveOrZero(message = "Battery wattage cannot be negative")
    private Long capacity;
}
