package com.example.bright_storage.exception;

public class DBException extends RuntimeException{

    public DBException() {
        super();
    }

    public DBException(String message) {
        super(message);
    }
}
