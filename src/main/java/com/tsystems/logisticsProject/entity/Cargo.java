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

    @Id
    @ToString.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "weight")
    private Double weight;

    @ToString.Exclude
    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL)
    List<Waypoint> waypoints;

}
