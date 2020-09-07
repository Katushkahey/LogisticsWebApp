package com.tsystems.logisticsProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderClientDto extends AbstractDto {

    private String number;
    private String startedCity;
    private String finishedCity;
    private String status;
}
