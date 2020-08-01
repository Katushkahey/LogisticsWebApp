package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.TruckState;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "trucks")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "number") // реализовать валидацию
    private String number;

    @NonNull
    @Column(name = "crew_size")
    private Integer crewSize;

    @NonNull
    @Column(name = "capacity")
    private Integer capacity;

    @NonNull
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private TruckState truckState;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "current_city_id")
    private City currentCity;

    @OneToOne(mappedBy = "orderTruck")
    private Order order;

    @OneToMany(mappedBy = "currentTruck")
    List<Driver> drivers;

    @Override
    public String toString() {
        return "Truck{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", crewSize=" + crewSize +
                ", capacity=" + capacity +
                '}';
    }
}
