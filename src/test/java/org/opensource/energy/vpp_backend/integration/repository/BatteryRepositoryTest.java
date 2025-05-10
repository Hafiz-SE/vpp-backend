package org.opensource.energy.vpp_backend.integration.repository;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.opensource.energy.vpp_backend.constant.FixedDBConstant;
import org.opensource.energy.vpp_backend.entity.Battery;
import org.opensource.energy.vpp_backend.repository.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.opensource.energy.vpp_backend.constant.FixedDBConstant.DB_VERSION;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BatteryRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DB_VERSION)
            .withDatabaseName(FixedDBConstant.DB_NAME)
            .withUsername(FixedDBConstant.DB_USER)
            .withPassword(FixedDBConstant.DB_PASS);

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private BatteryRepository batteryRepository;

    @Test
    void given_valid_battery_when_saved_then_it_can_be_retrieved_successfully() {
        Battery battery = Battery.builder()
                .name("Test Battery")
                .postcode(12345)
                .wattCapacity(500L)
                .build();

        Battery saved = batteryRepository.save(battery);

        Optional<Battery> found = batteryRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Battery");
    }

    @Test
    void given_valid_battery_requests_when_saving_all_batteries_then_all_batteries_are_saved_successfully() {
        List<Battery> batteries = List.of(
                Battery.builder().name("Battery A").postcode(1010).wattCapacity(100L).build(),
                Battery.builder().name("Battery B").postcode(1020).wattCapacity(200L).build()
        );

        List<Battery> saved = batteryRepository.saveAll(batteries);

        assertThat(saved).hasSize(2);
        assertThat(saved).allSatisfy(b -> assertThat(b.getId()).isNotNull());
        assertThat(saved).extracting(Battery::getName)
                .containsExactlyInAnyOrder("Battery A", "Battery B");
    }

    @Test
    void given_duplicate_battery_names_when_saving_then_throw_unique_constraint_violation() {
        // Given
        Battery b1 = Battery.builder()
                .name("UniqueBattery")
                .postcode(5000)
                .wattCapacity(1200L)
                .build();

        Battery b2 = Battery.builder()
                .name("UniqueBattery")
                .postcode(5100)
                .wattCapacity(1300L)
                .build();

        batteryRepository.save(b1);

        // When / Then
        assertThrows(Exception.class, () -> {
            batteryRepository.save(b2);
            batteryRepository.flush(); // force insert to catch constraint
        });
    }
}

