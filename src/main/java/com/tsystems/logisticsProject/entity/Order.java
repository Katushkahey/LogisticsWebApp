package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;;
import java.util.List;

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
    private List<Waypoint> waypoints;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "completion_date")
    private Long completionDate;
}
