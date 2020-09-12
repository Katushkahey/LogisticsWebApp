package com.tsystems.logisticsProject.exception;

public class NotUniqueUserNameException extends LogisticsWebAppException {

    public NotUniqueUserNameException(String message) {
        super("Already exists User with this username. Choose another username.");
    }
}
