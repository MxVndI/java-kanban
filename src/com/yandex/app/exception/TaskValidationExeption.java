package com.yandex.app.exception;

public class TaskValidationExeption extends RuntimeException {
    public TaskValidationExeption() {
    }

    public TaskValidationExeption(String message) {
        super(message);
    }

    public TaskValidationExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskValidationExeption(Throwable cause) {
        super(cause);
    }

    public TaskValidationExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
