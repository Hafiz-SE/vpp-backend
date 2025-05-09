package org.opensource.energy.vpp_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Filtered battery statistics based on provided range")
public class FilteredBatteryStat {
    @Schema(description = "List of battery names sorted alphabetically", example = "[\"Battery-A1\", \"Battery-Z9\"]")
    private Collection<String> batteryNames;

    @Schema(description = "Sum of watt capacities of the matched batteries", example = "4500")
    private Long totalCapacity;

    @Schema(description = "Average capacity of matched batteries", example = "900")
    private Long averageCapacity;

    @Schema(description = "Total number of matched batteries", example = "5")
    private Long totalCount;

    @Schema(description = "Name of the battery with the highest capacity", example = "Battery-Z9")
    private String highestCapacityBatteryName;

    @Schema(description = "Name of the battery with the lowest capacity", example = "Battery-A1")
    private String lowestCapacityBatteryName;
}
