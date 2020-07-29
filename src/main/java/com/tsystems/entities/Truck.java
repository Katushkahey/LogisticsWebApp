package com.tsystems.entities;

import com.tsystems.entities.enums.TruckState;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "trucks")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number") // реализовать валидацию
    private String number;

    @Column(name = "crew_size")
    private Integer crewSize;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private TruckState truckState;

    @ManyToOne
    @JoinColumn(name = "current_city_id")
    private City currentCity;

    @OneToOne(mappedBy = "truck")
    private Order order;

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TruckState getTruckState() {
        return truckState;
    }

    public void setTruckState(TruckState truckState) {
        this.truckState = truckState;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Truck)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        Truck truck = (Truck) o;
        return ((Truck) o).capacity == capacity &&
                ((Truck) o).crewSize == crewSize &&
                ((Truck) o).truckState.equals(truckState) &&
                ((Truck) o).number.equals(number);

    }

    @Override
    public int hashCode() {
        return 421 * number.hashCode() +
                17 * capacity.hashCode() +
                43 * crewSize.hashCode() +
                151 * truckState.hashCode();
    }

    @Override
    public String toString() {
        return "Truck: " +
                "id = " + id +
                ", number = " + number +
                ", crewSize = " + crewSize +
                ", capacity = " + capacity +
                ", truckState = " + truckState;
    }
}
