package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.DriverState;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "drivers")
public class Driver extends AbstractEntity {

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
    @JoinColumn(name = "current_order_id")
    private Order currentOrder;

    @NonNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", hoursThisMonth=" + hoursThisMonth +
                '}';
    }
}
