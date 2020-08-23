package com.tsystems.logisticsProject.dto;

import com.tsystems.logisticsProject.entity.enums.DriverState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class DriverDtoForDriverPage extends AbstractDto {

    private String name;
    private String surname;
    private String telephoneNumber;
    private int hoursThisMonth;
    private DriverState driverState;
    private String cityName;
    private OrderDtoForDriverPage orderDtoForDriverPage;
    private List<String> partners;

}
