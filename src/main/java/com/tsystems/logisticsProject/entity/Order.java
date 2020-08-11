package com.tsystems.logisticsProject.entity;

import lombok.*;
import org.hibernate.collection.internal.PersistentList;

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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumn(name = "truck_id")
    private Truck orderTruck;

//    @NonNull
//    @OneToMany(mappedBy = "order")
//    List<Cargo> cargoes;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @OneToMany(mappedBy = "currentOrder")
//    private List<Driver> drivers;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch=FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
    List<Waypoint> waypoints;
}
