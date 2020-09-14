package com.tsystems.logisticsProject.dto;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverAdminDto extends AbstractDto {

    private String name;
    private String surname;
    private String telephoneNumber;
    private int hoursThisMonth;
    private String cityName;
    @Builder.Default
    private boolean isAvailable = true;
    private String userName;


}
