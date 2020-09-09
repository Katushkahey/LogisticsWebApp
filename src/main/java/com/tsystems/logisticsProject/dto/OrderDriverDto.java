package com.tsystems.logisticsProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDriverDto extends AbstractDto {

    private String number;
    private String truckNumber;
    private String status;
    private List<WaypointDto> waypoints;
    private List<DriverShortDto> drivers;
    private Long completionDate;

}
