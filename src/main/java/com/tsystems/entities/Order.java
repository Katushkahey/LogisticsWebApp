package com.tsystems.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @OneToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @OneToMany()
    @JoinTable(
            name = "orders_cargoes",
            joinColumns = @JoinColumn(name = "order_id" ),
            inverseJoinColumns = @JoinColumn(name = "cargo_id")
    )
    private List<Cargo> cargoes;

    @ManyToMany
    @JoinTable(name="orders_drivers", joinColumns=@JoinColumn(name="order_id"),
            inverseJoinColumns=@JoinColumn(name="driver_id"))
    private Set<Driver> drivers;

    public Long getId() {
        return id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public List<Cargo> getCargoes() {
        return cargoes;
    }

    public void setCargoes(List<Cargo> cargoes) {
        this.cargoes = cargoes;
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        Order order = (Order) o;
        return isCompleted == order.isCompleted &&
                ((Order) o).id == id;
    }

    @Override
    public int hashCode() {
        return 433 * id.hashCode();
    }

    @Override
    public String toString() {
        return "Order: " +
                "id = " + id +
                ", isCompleted = " + isCompleted;
    }
}
