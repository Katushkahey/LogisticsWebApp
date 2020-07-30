package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.CargoState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "cargoes")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "weight")
    private Double weight;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private CargoState state;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
