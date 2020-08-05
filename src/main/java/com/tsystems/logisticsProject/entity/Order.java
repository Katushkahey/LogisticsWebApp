package com.tsystems.logisticsProject.entity;

import lombok.*;

import javax.persistence.*;
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
