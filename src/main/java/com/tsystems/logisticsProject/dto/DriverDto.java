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
public class DriverDto extends AbstractDto {

    private String name;
    private String surname;
    private String telephoneNumber;
    private int hoursThisMonth;
    private List<String> partners;
    private String driverState;
    private String orderNumber;
    private Long startWorkingTime;


}
