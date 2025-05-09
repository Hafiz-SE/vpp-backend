package org.opensource.energy.vpp_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.opensource.energy.vpp_backend.aspect.RetryOnDatabaseFailure;
import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;
import org.opensource.energy.vpp_backend.entity.Battery;
import org.opensource.energy.vpp_backend.repository.BatteryRepository;
import org.opensource.energy.vpp_backend.service.BatteryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatteryServiceImpl implements BatteryService {
    private final BatteryRepository batteryRepository;

    @Transactional
    @Override
    @RetryOnDatabaseFailure
    public List<Long> saveBatteries(Collection<CreateBatteryRequest> batteries) {
        List<Battery> batteriesToSave = batteries.stream()
                .map(req -> Battery.builder()
                        .name(req.getName())
                        .postcode(req.getPostcode())
                        .wattCapacity(req.getCapacity())
                        .build()
                )
                .toList();

        return batteryRepository
                .saveAll(batteriesToSave)
                .stream()
                .map(Battery::getId)
                .collect(Collectors.toList());
    }
}
