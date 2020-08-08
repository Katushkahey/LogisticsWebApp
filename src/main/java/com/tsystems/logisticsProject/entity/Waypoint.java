package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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

    @NonNull
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @NonNull
    @ManyToOne
    @JoinColumn(name="cargo_id")
    private Cargo cargo;

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

    @Override
    public String toString() {
        return "Waypoint{" +
                "id=" + id +
                ", action=" + action +
                '}';
    }
}
