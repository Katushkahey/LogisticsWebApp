package com.tsystems.logisticsProject.dto;

import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDtoForDriverPage extends AbstractDto {

    private String number;
    private String truckNumber;
    private OrderStatus status;
    private List<WaypointDto> waypointsDto;
    private Long completionDate;

}
