package org.opensource.energy.vpp_backend.unit.util;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensource.energy.vpp_backend.util.ValidationUtil;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ValidationUtilTest {
    // region Tests for validateRangePair

    @Test
    void given_both_null_when_validateRangePair_then_passes() {
        assertDoesNotThrow(() -> ValidationUtil.validateRangePair("wattage", null, null));
    }

    @Test
    void given_both_non_null_when_validateRangePair_then_passes() {
        assertDoesNotThrow(() -> ValidationUtil.validateRangePair("wattage", 10L, 20L));
    }

    @Test
    void given_only_from_when_validateRangePair_then_throws_exception() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateRangePair("wattage", 10L, null));
        assertTrue(exception.getMessage().contains("wattageFrom and wattageTo must be provided together."));
    }

    @Test
    void given_only_to_when_validateRangePair_then_throws_exception() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateRangePair("wattage", null, 20L));
        assertTrue(exception.getMessage().contains("wattageFrom and wattageTo must be provided together."));
    }

    //endregion

    // region Tests for validateRange

    @Test
    void given_valid_range_when_validateRange_then_passes() {
        assertDoesNotThrow(() -> ValidationUtil.validateRange("capacity", 0L, 0L));
        assertDoesNotThrow(() -> ValidationUtil.validateRange("capacity", 5L, 10L));
    }

    @Test
    void given_negative_from_when_validateRange_then_throws_exception() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateRange("capacity", -1L, 10L));
        assertEquals("Invalid capacity range", exception.getMessage());
    }

    @Test
    void given_negative_to_when_validateRange_then_throws_exception() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateRange("capacity", 5L, -10L));
        assertEquals("Invalid capacity range", exception.getMessage());
    }

    @Test
    void given_from_greater_than_to_when_validateRange_then_throws_exception() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateRange("capacity", 20L, 10L));
        assertEquals("Invalid capacity range", exception.getMessage());
    }

    //endregion
}
