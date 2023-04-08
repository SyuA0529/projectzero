package com.oaoproject.projectzero.exception;

public class NoSuchMessageException extends RuntimeException{
    public NoSuchMessageException() {
        super("존재하지 않는 메세지입니다.");
    };

    public NoSuchMessageException(String message) {
        super(message);
    }
}
