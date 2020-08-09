package com.tsystems.logisticsProject.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "cities")
public class City extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "lat")
    @NotNull
    private Double lat;

    @NonNull
    @Column(name = "long")
    @NotNull
    private Double lng;

    @NonNull
    @Column(name = "name")
    @NotNull
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "currentCity")
    List<Driver> drivers;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "city")
    List<Waypoint> waypoints;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "currentCity")
    List<Truck> trucksInThisCity;

}
