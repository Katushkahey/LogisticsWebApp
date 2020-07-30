package com.tsystems.logisticsProject.entity;

import com.sun.istack.NotNull;
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
@Table(name = "cities")
public class City {

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

    @OneToMany(mappedBy = "currentCity")
    List<Driver> drivers;

}
