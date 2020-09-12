package com.tsystems.logisticsProject.exception.checked;

import com.tsystems.logisticsProject.exception.checked.LogisticsWebAppException;

public class NotUniqueUserNameException extends LogisticsWebAppException {

    public NotUniqueUserNameException(String message) {
        super("Already exists User with this username. Choose another username.");
    }
}
