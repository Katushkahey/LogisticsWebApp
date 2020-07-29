package com.tsystems.entities;

import com.sun.istack.NotNull;
import com.tsystems.entities.enums.Action;

import javax.persistence.*;
// to do!!!!!!!!!
@Entity
@Table(name = "order_info")
public class OrderInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private Action action;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

}
