package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "waypoints")
public class Waypoint extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NonNull
    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name="order_id")
    private Order order;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NonNull
    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name="cargo_id")
    private Cargo cargo;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NonNull
    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;

    @NonNull
    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private Action action;

    @NonNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private WaypointStatus status;

}
