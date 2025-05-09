package org.opensource.energy.vpp_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilteredBatteryStat {
    private Collection<String> batteryNames;
    private Long totalCapacity;
    private Long averageCapacity;
    private Long totalCount;
    private String highestCapacityBatteryName;
    private String lowestCapacityBatteryName;
}
