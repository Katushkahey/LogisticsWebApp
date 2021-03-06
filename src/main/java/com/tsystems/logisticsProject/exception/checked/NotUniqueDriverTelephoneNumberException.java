package com.tsystems.logisticsProject.exception.checked;

public class NotUniqueDriverTelephoneNumberException extends LogisticsWebAppException {

    public NotUniqueDriverTelephoneNumberException(String telephoneNumber) {
        super(String.format("Driver with telephone number %s already exists. Check telephone number", telephoneNumber));
    }
}
