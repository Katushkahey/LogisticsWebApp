package com.tsystems.logisticsProject.dto;

import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class WaypointDto extends AbstractDto {

    private String cityName;
    private String cargoName;
    private String cargoNumber;
    private Double cargoWeight;
    private Action action;
    private WaypointStatus status;
}
