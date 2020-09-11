package com.tsystems.logisticsProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DriverShortDto extends AbstractDto {

    String name;
    String surname;
    String state;
    int hoursThisMonth;
    String orderNumber;

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
