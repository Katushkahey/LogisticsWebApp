package com.tsystems.entities;

import com.tsystems.entities.enums.CargoState;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cargoes")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private CargoState state;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL)
    private List<OrderInfo> routs;

    public Long getId() {
        return id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public CargoState getState() {
        return state;
    }

    public void setState(CargoState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderInfo> getRouts() {
        return routs;
    }

    public void setRouts(List<OrderInfo> routs) {
        this.routs = routs;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cargo))
        return false;
        if (o == this)
            return true;

        Cargo cargo = (Cargo) o;
        return ((Cargo) o).name.equals(name) &&
                ((Cargo) o).weight == weight &&
                ((Cargo) o).id == id;
    }

    @Override
    public int hashCode() {
        return 113 * name.hashCode() +
                137 * weight.hashCode() +
                181 * id.hashCode();
    }

    @Override
    public String toString() {
        return "Cargo: " +
                "name = " + name +
                ", weight = " + weight +
                ", state = " + state;
    }
}
