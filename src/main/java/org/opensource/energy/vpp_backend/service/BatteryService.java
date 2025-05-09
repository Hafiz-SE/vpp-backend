package org.opensource.energy.vpp_backend.service;

import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;
import org.opensource.energy.vpp_backend.dto.response.FilteredBatteryStat;

import java.util.Collection;
import java.util.List;

public interface BatteryService {
    /**
     * Persists a collection of battery creation requests into the database.
     * <p>
     * The operation is transactional and will automatically retry up to 3 times
     * in case of transient database errors such as deadlocks, connection timeouts, or lock acquisition failures.
     *
     * @param batteries the collection of battery creation data to be saved
     * @return a list of generated battery IDs after successful persistence
     */
    List<Long> saveBatteries(Collection<CreateBatteryRequest> batteries);

    /**
     * Retrieves a statistical summary of batteries filtered by a given postcode range
     * and optional watt capacity range. The method returns:
     * <ul>
     *   <li>A list of battery names within the postcode range, sorted alphabetically</li>
     *   <li>Statistical metrics such as total, average, minimum, and maximum watt capacity</li>
     *   <li>Battery count matching the filter</li>
     * </ul>
     *
     * <p><strong>Note:</strong> If {@code postcodeFrom} is provided, {@code postcodeTo} must also be provided, and vice versa.
     * The same rule applies for {@code wattageFrom} and {@code wattageTo} — ranges must be specified in pairs.</p>
     *
     * @param postcodeFrom the lower bound of the postcode range (inclusive), or null if unfiltered
     * @param postcodeTo   the upper bound of the postcode range (inclusive), or null if unfiltered
     * @param wattageFrom  (optional) the minimum watt capacity to include; may be null
     * @param wattageTo    (optional) the maximum watt capacity to include; may be null
     * @return a {@link FilteredBatteryStat} containing the sorted battery names and statistical summary
     * @throws IllegalArgumentException if only one bound of a range is provided
     */

    FilteredBatteryStat getFilteredBatteryStat(Integer postcodeFrom, Integer postcodeTo, Long wattageFrom, Long wattageTo);
}
