package com.tsystems.logisticsProject.dto;

import com.tsystems.logisticsProject.entity.enums.TruckState;
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
    private int crewSize;
    private double capacity;
    private TruckState truckState;
    private String currentCity;
    private String orderNumber;

}
