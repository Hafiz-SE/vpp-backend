package org.opensource.energy.vpp_backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;
import org.opensource.energy.vpp_backend.service.BatteryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "battery",
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

}
