package com.yandex.app.exception;

public class RequestFailedExeption extends RuntimeException {
    public RequestFailedExeption() {
    }

    public RequestFailedExeption(String message) {
        super(message);
    }

    public RequestFailedExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestFailedExeption(Throwable cause) {
        super(cause);
    }
}
