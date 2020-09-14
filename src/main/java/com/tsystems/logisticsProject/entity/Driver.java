package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.DriverState;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "drivers")
public class Driver extends AbstractEntity {

    public static final int MAX_HOURS_IN_MONTH = 176;

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
    private int hoursThisMonth;

    @NonNull
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private DriverState driverState;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NonNull
    @ManyToOne
    @JoinColumn(name = "current_city_id")
    private City currentCity;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "current_order_id")
    private Order currentOrder;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "start_working_time")
    private Long startWorkingTime;

}
