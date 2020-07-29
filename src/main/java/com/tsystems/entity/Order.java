package com.tsystems.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "is_completed")
    private boolean isCompleted;

    @OneToOne
    @JoinColumn(name = "truck_id")
    private Truck orderTruck;

    @NonNull
    @OneToMany(mappedBy = "order")
    List<Cargo> cargoes;

    @OneToMany(mappedBy = "currentOrder")
    private List<Driver> drivers;

}
