package com.tsystems.logisticsProject.entity;

import lombok.*;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "cargoes")
public class Cargo extends AbstractEntity {

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "weight")
    private Double weight;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "number")
    private String number;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL)
    List<Waypoint> waypoints;
}
