package org.opensource.energy.vpp_backend.integration.service.impl;

import org.junit.jupiter.api.*;
import org.opensource.energy.vpp_backend.constant.FixedDBConstant;
import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;
import org.opensource.energy.vpp_backend.dto.response.FilteredBatteryStat;
import org.opensource.energy.vpp_backend.entity.Battery;
import org.opensource.energy.vpp_backend.repository.BatteryRepository;
import org.opensource.energy.vpp_backend.service.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Tag("integration")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@EnableJpaAuditing
@EnableRetry
class BatteryServiceImplTest {

    @Autowired
    private BatteryService batteryService;

    @Autowired
    private BatteryRepository batteryRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName(FixedDBConstant.FIXED_DB)
            .withUsername(FixedDBConstant.FIXED_DB_USER)
            .withPassword(FixedDBConstant.FIXED_DB_PASS);

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void cleanDatabase() {
        batteryRepository.deleteAll();
    }

    @Test
    void given_valid_battery_requests_when_save_batteries_then_return_saved_ids_and_persisted_records() {
        List<CreateBatteryRequest> batteries = List.of(
                CreateBatteryRequest.builder().name("Battery A").postcode(1001).capacity(150L).build(),
                CreateBatteryRequest.builder().name("Battery B").postcode(1002).capacity(250L).build()
        );

        List<Long> savedIds = batteryService.saveBatteries(batteries);

        assertThat(savedIds).hasSize(2);
        assertThat(batteryRepository.findAll()).hasSize(2);
    }

    @Test
    void given_filter_parameters_when_get_battery_stats_then_return_filtered_battery_stats_correctly() {
        Battery b1 = Battery.builder().name("Alpha").postcode(1000).wattCapacity(100L)
                .modifiedAt(Instant.now()).createdAt(Instant.now()).build();
        Battery b2 = Battery.builder().name("Beta").postcode(1001).wattCapacity(300L)
                .modifiedAt(Instant.now()).createdAt(Instant.now()).build();
        Battery b3 = Battery.builder().name("Gamma").postcode(1100).wattCapacity(200L)
                .modifiedAt(Instant.now()).createdAt(Instant.now()).build();


        batteryRepository.saveAll(List.of(b1, b2, b3));

        FilteredBatteryStat stat = batteryService.getFilteredBatteryStat(1000, 1100, null, null);

        assertThat(stat.getBatteryNames()).containsExactly("Alpha", "Beta", "Gamma");
        assertThat(stat.getTotalCapacity()).isEqualTo(600);
        assertThat(stat.getAverageCapacity()).isEqualTo(200);
        assertThat(stat.getHighestCapacityBatteryName()).isEqualTo("Beta");
        assertThat(stat.getLowestCapacityBatteryName()).isEqualTo("Alpha");
        assertThat(stat.getTotalCount()).isEqualTo(3);
    }
}
