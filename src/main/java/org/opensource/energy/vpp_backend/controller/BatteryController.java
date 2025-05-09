package org.opensource.energy.vpp_backend.controller;

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
@RequestMapping(
        value = "batteries",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class BatteryController {

    private final BatteryService batteryService;

    @PostMapping
    public List<Long> saveBatteries(@RequestBody
                                    @Valid
                                    @NotEmpty(message = "List cannot be empty")
                                    Collection<CreateBatteryRequest> createBatteryRequests) {
        
        return batteryService.saveBatteries(createBatteryRequests);
    }

    @GetMapping
    public FilteredBatteryStat getBatteries(@RequestParam Integer postcodeFrom,
                                            @RequestParam Integer postcodeTo,
                                            @RequestParam(required = false) Long wattageFrom,
                                            @RequestParam(required = false) Long wattageTo) {

        ValidationUtil.validateRangePair("postcode", postcodeFrom, postcodeTo);
        ValidationUtil.validateRangePair("wattage", wattageFrom, wattageTo);
        return batteryService.getFilteredBatteryStat(postcodeFrom, postcodeTo, wattageFrom, wattageTo);
    }

}
