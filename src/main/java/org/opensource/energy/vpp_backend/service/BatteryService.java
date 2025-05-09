package org.opensource.energy.vpp_backend.service;

import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;

import java.util.Collection;
import java.util.List;

public interface BatteryService {
    List<Long> saveBatteries(Collection<CreateBatteryRequest> batteries);
}
