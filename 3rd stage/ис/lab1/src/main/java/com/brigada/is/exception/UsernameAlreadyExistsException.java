package com.brigada.is.exception;

public class UsernameAlreadyExistsException extends AlreadyExistsException {
    public UsernameAlreadyExistsException() {
        super();
    }

    public UsernameAlreadyExistsException(String msg) {
        super(msg);
    }
}
