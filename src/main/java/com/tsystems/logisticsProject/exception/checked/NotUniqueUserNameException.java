package com.tsystems.logisticsProject.exception.checked;

public class NotUniqueUserNameException extends LogisticsWebAppException {

    public NotUniqueUserNameException(String username) {
        super(String.format("Already exists User with username %s. Choose another username.", username));
    }
}
