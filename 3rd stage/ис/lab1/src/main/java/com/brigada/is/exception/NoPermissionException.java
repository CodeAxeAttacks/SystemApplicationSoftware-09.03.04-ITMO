package com.brigada.is.exception;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException() {
        super();
    }

    public NoPermissionException(String msg) {
        super(msg);
    }
}
