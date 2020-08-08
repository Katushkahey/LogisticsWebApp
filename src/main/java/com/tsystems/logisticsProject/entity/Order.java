package com.tsystems.logisticsProject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "orders")
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @OneToOne
    @JoinColumn(name = "truck_id")
    private Truck orderTruck;

//    @NonNull
//    @OneToMany(mappedBy = "order")
//    List<Cargo> cargoes;

    @OneToMany(mappedBy = "currentOrder")
    private List<Driver> drivers;

    @OneToMany(mappedBy = "order")
    List<Waypoint> waypoints;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", isCompleted=" + isCompleted +
                '}';
    }

    //зачем я это сделала??
    public Long getId() {
        return id;
    }

}
