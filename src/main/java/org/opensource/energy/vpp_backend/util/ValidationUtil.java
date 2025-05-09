package org.opensource.energy.vpp_backend.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class ValidationUtil {
    /**
     * Validates that a pair of related values (e.g., from/to range) are either both present or both null.
     * <p>
     * This is typically used for optional filtering fields (e.g., wattageFrom and wattageTo), where
     * specifying only one side of the range would lead to inconsistent or ambiguous logic.
     * </p>
     *
     * @param fieldName the logical name of the field being validated (e.g., "wattage", "capacity");
     *                  used for error message clarity
     * @param from      the lower bound of the range
     * @param to        the upper bound of the range
     * @throws IllegalArgumentException if only one of {@code from} or {@code to} is provided (i.e., non-null)
     */
    public static void validateRangePair(String fieldName, Object from, Object to) {
        log.debug("Validating range pair for {}: from={}, to={}", fieldName, from, to);
        if ((from == null) ^ (to == null)) {
            log.error("Validation failed: Only one side of {} range is provided (from={}, to={})", fieldName, from, to);
            throw new IllegalArgumentException("Both " + fieldName + "From and " + fieldName + "To must be provided together.");
        }
    }

    /**
     * Validates that a given numeric (long) range is valid.
     * <p>
     * Ensures that both {@code from} and {@code to} values are non-negative and that {@code from} is less than or equal to {@code to}.
     * </p>
     *
     * @param rangeType a string used to describe the range type (e.g., "postcode", "capacity") for error messages
     * @param from      the lower bound of the range (inclusive)
     * @param to        the upper bound of the range (inclusive)
     * @throws IllegalArgumentException if {@code from} or {@code to} is negative, or if {@code from} is greater than {@code to}
     */
    public static void validateRange(String rangeType, Long from, Long to) {
        log.debug("Validating {} range: from={}, to={}", rangeType, from, to);
        if (from < 0 || to < 0 || from > to) {
            log.error("Validation failed for {}: from={}, to={}", rangeType, from, to);
            throw new IllegalArgumentException("Invalid " + rangeType + " range");
        }
    }
}
