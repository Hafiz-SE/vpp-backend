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

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BatteryRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3-alpine")
            .withDatabaseName(FixedDBConstant.FIXED_DB)
            .withUsername(FixedDBConstant.FIXED_DB_USER)
            .withPassword(FixedDBConstant.FIXED_DB_PASS);

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private BatteryRepository batteryRepository;

    @Test
    void testSaveAndRetrieveBattery() {
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
    void should_save_all_batteries_successfully() {
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
    void should_enforce_unique_name_constraint() {
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
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            batteryRepository.save(b2);
            batteryRepository.flush(); // force insert to catch constraint
        });
    }
}

