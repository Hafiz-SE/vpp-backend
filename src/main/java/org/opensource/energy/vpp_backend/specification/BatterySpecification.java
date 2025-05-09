package org.opensource.energy.vpp_backend.specification;

import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.opensource.energy.vpp_backend.entity.Battery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Slf4j
public class BatterySpecification {
    public static Specification<Battery> byPostcodeAndWattageRange(
            int postcodeFrom,
            int postcodeTo,
            Long wattageFrom,
            Long wattageTo
    ) {
        log.debug("Creating specification with postcodeFrom={}, postcodeTo={}, wattageFrom={}, wattageTo={}",
                postcodeFrom, postcodeTo, wattageFrom, wattageTo);
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.between(root.get("postcode"), postcodeFrom, postcodeTo));
            log.debug("Added postcode predicate: BETWEEN {} AND {}", postcodeFrom, postcodeTo);
            if (wattageFrom != null) {
                predicates.add(cb.between(root.get("wattCapacity"), wattageFrom, wattageTo));
                log.debug("Added wattCapacity predicate: BETWEEN {} AND {}", wattageFrom, wattageTo);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
