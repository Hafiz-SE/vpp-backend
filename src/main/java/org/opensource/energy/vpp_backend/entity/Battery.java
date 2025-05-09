package org.opensource.energy.vpp_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        name = "battery",
        indexes = {
                @Index(name = "idx_battery_postcode", columnList = "postcode")
        }
)
public class Battery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "battery_id_seq_gen")
    @SequenceGenerator(
            name = "battery_id_seq_gen",
            sequenceName = "battery_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String name;

    @Column(nullable = false)
    private Integer postcode;

    @Column(name = "watt_capacity", nullable = false)
    private Long wattCapacity;
}
