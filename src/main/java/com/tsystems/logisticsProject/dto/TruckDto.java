package com.tsystems.logisticsProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TruckDto extends AbstractDto {

    private String number;
    private double capacity;
    private int crewSize;
    private String state;
    private boolean isAvailable;
    private String cityName;

}
