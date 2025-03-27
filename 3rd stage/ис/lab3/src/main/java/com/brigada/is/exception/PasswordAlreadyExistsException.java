package com.brigada.is.exception;

public class PasswordAlreadyExistsException extends AlreadyExistsException {
    public PasswordAlreadyExistsException() {
        super();
    }

    public PasswordAlreadyExistsException(String msg) {
        super(msg);
    }
}
