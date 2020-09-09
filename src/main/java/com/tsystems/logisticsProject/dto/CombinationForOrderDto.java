package com.tsystems.logisticsProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CombinationForOrderDto {

    Long id;
    List<DriverShortDto> drivers;
    String TruckNumber;
    private int totalHours;
    private int totalBillableHours;
}

