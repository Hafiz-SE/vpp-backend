package org.opensource.energy.vpp_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBatteryRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 512, message = "Name must not exceed 512 characters")
    private String name;

    @NotNull(message = "Postcode is required")
    @PositiveOrZero(message = "Postcode cannot be negative")
    private Integer postcode;

    @NotNull(message = "Capacity is required")
    @PositiveOrZero(message = "Battery wattage cannot be negative")
    private Long capacity;
}
