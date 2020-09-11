package com.tsystems.logisticsProject.dto;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TruckDto extends AbstractDto {

    private String number;
    private double capacity;
    private int crewSize;
    private String state;
    @Builder.Default
    private boolean isAvailable = true;
    private String cityName;

}
