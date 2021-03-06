package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "waypoints")
public class Waypoint extends AbstractEntity {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NonNull
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @NonNull
    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private Action action;

    @NonNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private WaypointStatus status;

    @NonNull
    @Column(name = "sequence")
    private Long sequence;

}
