package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Truck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinationForOrder {

    private Long id;

    private Truck truck;

    private List<Driver> listOfDrivers;

    private int totalHours;

    private int totalBillableHours;
}
