package com.tsystems.logisticsProject.exception;

public abstract class LogisticsWebAppException extends Exception  {

    public LogisticsWebAppException(String message) {
        super(message);
    }

    public LogisticsWebAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogisticsWebAppException(Throwable cause) {
        super(cause);
    }
}
