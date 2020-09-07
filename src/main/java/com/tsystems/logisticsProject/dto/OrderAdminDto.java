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
public class OrderAdminDto extends AbstractDto {

    private String number;
    private String truckNumber;
    private List<String> drivers;
    private double maxWeight;
    private List<String> cargoes;
    private String cityFrom;
    private String cityTo;
    private List<WaypointDto> waypoints;
}
