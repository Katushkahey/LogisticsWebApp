package com.tsystems.logisticsProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CombinationForOrderDto extends AbstractDto{

    private List<DriverShortDto> drivers;
    private String TruckNumber;
    private int totalHours;
    private int totalBillableHours;
    private String city;
}

