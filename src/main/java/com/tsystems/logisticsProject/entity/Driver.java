package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.DriverState;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "drivers")
public class Driver extends AbstractEntity {

    private static final int MAX_HOURS_IN_MONTH = 176;
    private static LocalDate firstDayOfCurrentMounth = YearMonth.now().atDay( 1 );


    @ToString.Exclude
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

    @ToString.Exclude
    @NonNull
    @Column(name = "telephone_number")
    private String telephoneNumber;

    @ToString.Exclude
    @Column(name = "hours_this_month")
    private Long hoursThisMonth;

    @ToString.Exclude
    @NonNull
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private DriverState driverState;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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

    @Override
    public String toString() {
        return "" + name + " " + surname;
    }

}
