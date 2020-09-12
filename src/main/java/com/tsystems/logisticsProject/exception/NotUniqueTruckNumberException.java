package com.tsystems.logisticsProject.exception;

public class NotUniqueTruckNumberException extends LogisticsWebAppException {

    public NotUniqueTruckNumberException(String message) {
        super("Already exists truck with this number. Check truck number");
    }
}
