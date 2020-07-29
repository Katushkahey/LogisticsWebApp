package com.tsystems.entities;

import com.tsystems.entities.enums.DriverState;

import javax.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

    public static final int MAX_HOURS_IN_MONTH = 176;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "hours_this_month")
    private Integer hoursThisMonth;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private DriverState driverState;

    @ManyToOne
    @JoinColumn(name = "current_city_id")
    private City currentCity;

    @ManyToOne
    @JoinColumn(name = "current_truck_id")
    private Truck currentTruck;

    @ManyToOne
    @JoinColumn(name = "current_order_id")
    private Order currentOrder;

    @OneToOne
    @Column(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public Integer getHoursThisMonth() {
        return hoursThisMonth;
    }

    public void setHoursThisMonth(Integer hoursThisMonth) {
        this.hoursThisMonth = hoursThisMonth;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public Truck getCurrentTruck() {
        return currentTruck;
    }

    public void setCurrentTruck(Truck currentTruck) {
        this.currentTruck = currentTruck;
    }

    public DriverState getDriverState() {
        return driverState;
    }

    public void setDriverState(DriverState driverState) {
        this.driverState = driverState;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Driver))
            return false;
        if (o == this)
            return true;

        Driver rhs = (Driver) o;
        return ((Driver) o).name.equals(name) &&
                ((Driver) o).surname.equals(surname) &&
                ((Driver) o).telephoneNumber.equals(telephoneNumber);
    }

    @Override
    public int hashCode() {
        return 11 * id.hashCode() +
                13 * name.hashCode() +
                17 * surname.hashCode() +
                19 * telephoneNumber.hashCode() +
                31 * hoursThisMonth.hashCode() +
                37 * driverState. hashCode();
    }

    @Override
    public String toString() {
        return "Driver: " +
                "name = " + name +
                ", surname = " + surname +
                ", telephoneNumber = " + telephoneNumber +
                ", hoursThisMonth = " + hoursThisMonth +
                ", state = " + driverState;
    }
}
