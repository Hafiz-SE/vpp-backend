package org.opensource.energy.vpp_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;
import org.opensource.energy.vpp_backend.dto.response.FilteredBatteryStat;
import org.opensource.energy.vpp_backend.service.BatteryService;
import org.opensource.energy.vpp_backend.util.ValidationUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Batteries", description = "Operations for managing battery data and retrieving statistics")
@RequestMapping(
        value = "batteries",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class BatteryController {

    private final BatteryService batteryService;

    @Operation(
            summary = "Save a list of batteries",
            description = "Takes a list of batteries with name, postcode, and watt capacity, and saves them in the system."
    )
    @PostMapping
    public List<Long> saveBatteries(@RequestBody
                                    @Valid
                                    @NotEmpty(message = "List cannot be empty")
                                    Collection<CreateBatteryRequest> createBatteryRequests) {

        return batteryService.saveBatteries(createBatteryRequests);
    }

    @GetMapping
    @Operation(
            summary = "Get battery statistics by filter",
            description = "Returns battery statistics including names, total, average, and min/max capacity based on given postcode and optional wattage range."
    )
    public FilteredBatteryStat getBatteries(@Parameter(description = "Postcode range start", required = true)
                                            @RequestParam
                                            Integer postcodeFrom,
                                            @Parameter(description = "Postcode range end", required = true)
                                            @RequestParam
                                            Integer postcodeTo,
                                            @Parameter(description = "Optional wattage range start")
                                            @RequestParam(required = false)
                                            Long wattageFrom,
                                            @Parameter(description = "Optional wattage range end")
                                            @RequestParam(required = false)
                                            Long wattageTo) {

        ValidationUtil.validateRangePair("postcode", postcodeFrom, postcodeTo);
        ValidationUtil.validateRangePair("wattage", wattageFrom, wattageTo);
        return batteryService.getFilteredBatteryStat(postcodeFrom, postcodeTo, wattageFrom, wattageTo);
    }

}
