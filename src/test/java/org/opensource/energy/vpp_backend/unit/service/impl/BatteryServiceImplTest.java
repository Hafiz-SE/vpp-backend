package org.opensource.energy.vpp_backend.unit.service.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;
import org.opensource.energy.vpp_backend.dto.response.FilteredBatteryStat;
import org.opensource.energy.vpp_backend.entity.Battery;
import org.opensource.energy.vpp_backend.repository.BatteryRepository;
import org.opensource.energy.vpp_backend.service.impl.BatteryServiceImpl;
import org.opensource.energy.vpp_backend.specification.BatterySpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BatteryServiceImplTest {

    @Mock
    private BatteryRepository batteryRepository;

    @InjectMocks
    private BatteryServiceImpl batteryService;

    private MockedStatic<BatterySpecification> batterySpecMock;

    @BeforeEach
    void setUp() {
        batterySpecMock = mockStatic(BatterySpecification.class);

        batterySpecMock.when(() ->
                BatterySpecification.byPostcodeAndWattageRange(
                        anyInt(),
                        anyInt(),
                        any(),
                        any()
                )).thenReturn((Specification<Battery>) (root, query, cb) -> null);
    }

    @AfterEach
    void tearDown() {
        batterySpecMock.close();
    }

    // region saveBatteries

    @Test
    void given_valid_batteries_when_saveBatteries_then_returns_saved_ids() {
        CreateBatteryRequest r1 = CreateBatteryRequest.builder().name("Alpha").postcode(1000).capacity(200L).build();
        CreateBatteryRequest r2 = CreateBatteryRequest.builder().name("Beta").postcode(1001).capacity(300L).build();

        Battery b1 = Battery.builder().id(1L).name("Alpha").postcode(1000).wattCapacity(200L).build();
        Battery b2 = Battery.builder().id(2L).name("Beta").postcode(1001).wattCapacity(300L).build();

        when(batteryRepository.saveAll(any())).thenReturn(List.of(b1, b2));

        List<Long> result = batteryService.saveBatteries(List.of(r1, r2));

        assertThat(result).containsExactly(1L, 2L);
    }

    //endregion

    // region getFilteredBatteryStat

    @Test
    void given_valid_data_when_getFilteredBatteryStat_then_returns_stats() {
        Battery b1 = Battery.builder().name("Zeta").postcode(1000).wattCapacity(100L).build();
        Battery b2 = Battery.builder().name("Alpha").postcode(1001).wattCapacity(300L).build();

        when(batteryRepository.findAll(Mockito.<Specification<Battery>>any()))
                .thenReturn(List.of(b1, b2));

        FilteredBatteryStat stat = batteryService.getFilteredBatteryStat(1000, 1001, 100L, 300L);

        assertThat(stat.getBatteryNames()).containsExactly("Alpha", "Zeta");
        assertThat(stat.getTotalCount()).isEqualTo(2);
        assertThat(stat.getTotalCapacity()).isEqualTo(400L);
        assertThat(stat.getAverageCapacity()).isEqualTo(200L);
        assertThat(stat.getHighestCapacityBatteryName()).isEqualTo("Alpha");
        assertThat(stat.getLowestCapacityBatteryName()).isEqualTo("Zeta");
    }

    @Test
    void given_no_matching_batteries_when_getFilteredBatteryStat_then_returns_zero_stats() {
        when(batteryRepository.findAll(Mockito.<Specification<Battery>>any()))
                .thenReturn(List.of());

        FilteredBatteryStat stat = batteryService.getFilteredBatteryStat(2000, 2010, null, null);

        assertThat(stat.getBatteryNames()).isEmpty();
        assertThat(stat.getTotalCount()).isZero();
        assertThat(stat.getTotalCapacity()).isZero();
        assertThat(stat.getAverageCapacity()).isZero();
        assertThat(stat.getHighestCapacityBatteryName()).isNull();
        assertThat(stat.getLowestCapacityBatteryName()).isNull();
    }

    @Test
    void given_invalid_postcode_range_when_getFilteredBatteryStat_then_throws_exception() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> batteryService.getFilteredBatteryStat(2005, 2000, null, null));

        assertThat(ex.getMessage()).isEqualTo("Invalid postcode range");
    }

    @Test
    void given_invalid_wattage_range_when_getFilteredBatteryStat_then_throws_exception() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> batteryService.getFilteredBatteryStat(1000, 1005, 500L, 200L));

        assertThat(ex.getMessage()).isEqualTo("Invalid wattage range");
    }

    //endregion
}
