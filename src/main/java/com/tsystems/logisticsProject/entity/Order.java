package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;;
import java.util.List;
import java.util.Random;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "orders")
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumn(name = "truck_id")
    private Truck orderTruck;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "order", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Cargo> cargoes;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "completion_date")
    private Long completionDate;

    @Column(name = "number")
    private String number;

}
