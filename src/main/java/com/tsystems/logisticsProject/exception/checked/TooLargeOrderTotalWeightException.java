package com.tsystems.logisticsProject.exception.checked;

public class TooLargeOrderTotalWeightException extends Exception {

    public TooLargeOrderTotalWeightException(Double weight, double capacity) {
        super(String.format("Total weight of this order is %s. Capacity of the biggest truck is %d " +
                "No one truck could complete this order", weight, capacity));
    }
}
