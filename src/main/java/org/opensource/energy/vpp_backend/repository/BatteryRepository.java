package org.opensource.energy.vpp_backend.repository;

import org.opensource.energy.vpp_backend.entity.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {
}
