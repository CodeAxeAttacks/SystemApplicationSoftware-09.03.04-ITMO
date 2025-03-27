package com.brigada.is.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException() {
        super();
    }

    public AlreadyExistsException(String msg) {
        super(msg);
    }
}
