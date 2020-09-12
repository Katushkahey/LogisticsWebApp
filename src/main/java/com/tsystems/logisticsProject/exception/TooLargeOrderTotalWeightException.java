package com.tsystems.logisticsProject.exception;

public class TooLargeOrderTotalWeightException extends Exception {

    public TooLargeOrderTotalWeightException(String message) {
        super("Total weight of this order bigger than capacity of the biges`t truck. No one truck could complete this order");
    }
}
