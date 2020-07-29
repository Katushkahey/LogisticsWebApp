package com.tsystems.entity;

import com.tsystems.entity.enums.CargoState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

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

//    @ManyToOne()
//    private List<OrderInfo> routs;
}
