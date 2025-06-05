package io.github.alberes.register.manager.authorization.services.exceptions;

public class DuplicateRecordException extends RuntimeException{

    public DuplicateRecordException(String msg) {
        super(msg);
    }

    public DuplicateRecordException(String msg, Throwable cause) {
        super(msg, cause);
    }
}