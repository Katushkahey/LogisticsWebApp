package com.tsystems.logisticsProject.exception.checked;

public class NotUniqueTruckNumberException extends LogisticsWebAppException {

    public NotUniqueTruckNumberException(String truckNumber) {
        super(String.format("Already exists truck with number %s. Check truck number", truckNumber));
    }
}
