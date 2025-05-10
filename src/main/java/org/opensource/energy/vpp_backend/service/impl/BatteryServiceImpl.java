package org.opensource.energy.vpp_backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensource.energy.vpp_backend.aspect.RetryOnDatabaseFailure;
import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;
import org.opensource.energy.vpp_backend.dto.response.FilteredBatteryStat;
import org.opensource.energy.vpp_backend.entity.Battery;
import org.opensource.energy.vpp_backend.repository.BatteryRepository;
import org.opensource.energy.vpp_backend.service.BatteryService;
import org.opensource.energy.vpp_backend.specification.BatterySpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.opensource.energy.vpp_backend.util.ValidationUtil.validateRange;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatteryServiceImpl implements BatteryService {
    private final BatteryRepository batteryRepository;

    @Transactional
    @Override
    @RetryOnDatabaseFailure
    public List<Long> saveBatteries(Collection<CreateBatteryRequest> batteries) {
        log.info("Saving {} battery record(s)...", batteries.size());

        List<Battery> batteriesToSave = batteries.stream()
                .map(req -> Battery.builder()
                        .name(req.getName())
                        .postcode(req.getPostcode())
                        .wattCapacity(req.getCapacity())
                        .build()
                )
                .collect(Collectors.toList());

        log.debug("Mapped request to battery entities to save: {}", batteriesToSave);

        List<Long> savedBatteryIds = batteryRepository
                .saveAll(batteriesToSave)
                .stream()
                .map(Battery::getId)
                .toList();

        log.info("Successfully saved {} battery record(s). IDs: {}", savedBatteryIds.size(), savedBatteryIds);

        return savedBatteryIds;
    }

    @Override
    public FilteredBatteryStat getFilteredBatteryStat(Integer postcodeFrom, Integer postcodeTo, Long wattageFrom, Long wattageTo) {
        log.debug("Retrieving filtered battery stats for postcode range [{} - {}] and wattage range [{} - {}]",
                postcodeFrom, postcodeTo, wattageFrom, wattageTo);

        validateRange("postcode", postcodeFrom.longValue(), postcodeTo.longValue());
        if (wattageFrom != null) {
            validateRange("wattage", wattageFrom, wattageTo);
        }

        Specification<Battery> spec = BatterySpecification
                .byPostcodeAndWattageRange(postcodeFrom, postcodeTo, wattageFrom, wattageTo);
        List<Battery> batteries = batteryRepository.findAll(spec);
        log.info("Found {} battery record(s) matching the criteria.", batteries.size());

        List<String> batteryNames = batteries.stream()
                .map(Battery::getName)
                .sorted()
                .toList();

        long totalCount = batteries.size();
        long totalCapacity = batteries.stream().mapToLong(Battery::getWattCapacity).sum();
        long averageCapacity = totalCount == 0 ? 0 : totalCapacity / totalCount;

        String maxCapacityBattery = batteries.stream()
                .max(Comparator.comparingLong(Battery::getWattCapacity))
                .map(Battery::getName)
                .orElse(null);

        String minCapacityBattery = batteries.stream()
                .min(Comparator.comparingLong(Battery::getWattCapacity))
                .map(Battery::getName)
                .orElse(null);

        log.debug("Battery names (sorted): {}", batteryNames);
        log.debug("Total capacity: {}, Average capacity: {}", totalCapacity, averageCapacity);
        log.debug("Battery with highest capacity: {}", maxCapacityBattery);
        log.debug("Battery with lowest capacity: {}", minCapacityBattery);

        return FilteredBatteryStat.builder()
                .batteryNames(batteryNames)
                .totalCount(totalCount)
                .totalCapacity(totalCapacity)
                .averageCapacity(averageCapacity)
                .highestCapacityBatteryName(maxCapacityBattery)
                .lowestCapacityBatteryName(minCapacityBattery)
                .build();
    }
}
