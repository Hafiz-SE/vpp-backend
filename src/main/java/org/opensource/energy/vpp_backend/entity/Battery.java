package org.opensource.energy.vpp_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@ToString(callSuper = true)
@Table(
        name = "battery",
        indexes = {
                @Index(name = "idx_battery_postcode", columnList = "postcode")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Battery extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "battery_seq_gen")
    @SequenceGenerator(name = "battery_seq_gen", sequenceName = "battery_id_seq", allocationSize = 50)
    private Long id;


    @Column(nullable = false, unique = true, length = 512)
    private String name;

    @Column(nullable = false)
    private Integer postcode;

    @Column(name = "watt_capacity", nullable = false)
    private Long wattCapacity;
}
