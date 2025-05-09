package org.opensource.energy.vpp_backend.service;

import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;

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
}
