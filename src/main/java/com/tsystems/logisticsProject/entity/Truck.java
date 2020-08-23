package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.TruckState;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Table(name = "trucks")
public class Truck extends AbstractEntity {

    @NonNull
    @Column(name = "number")
    private String number;

    @NonNull
    @Column(name = "crew_size")
    private Integer crewSize;

    @NonNull
    @Column(name = "capacity")
    private Double capacity;

    @NonNull
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private TruckState truckState;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NonNull
    @ManyToOne
    @JoinColumn(name = "current_city_id")
    private City currentCity;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "orderTruck")
    private Order order;
}
