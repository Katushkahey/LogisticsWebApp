package com.tsystems.logisticsProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class WaypointDto extends AbstractDto {

    private String cargoName;
    private String cargoNumber;
    private Double cargoWeight;
    private String cityName;
    private String action;
    private String status;
    private Long sequence;
}
