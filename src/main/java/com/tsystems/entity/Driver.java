package com.tsystems.entity;

import com.tsystems.entity.enums.DriverState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "drivers")
public class Driver {

    public static final int MAX_HOURS_IN_MONTH = 176;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "surname")
    private String surname;

    @NonNull
    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "hours_this_month")
    private Integer hoursThisMonth;

    @NonNull
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private DriverState driverState;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "current_city_id")
    private City currentCity;

    @ManyToOne
    @JoinColumn(name = "current_truck_id")
    private Truck currentTruck;

    @ManyToOne
    @JoinColumn(name = "current_order_id")
    private Order currentOrder;

    @NonNull
    @OneToOne
    @Column(name = "user_id")
    private User user;

}
