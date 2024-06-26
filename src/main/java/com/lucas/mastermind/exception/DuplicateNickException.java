package com.lucas.mastermind.exception;

public class DuplicateNickException extends RuntimeException{
    public DuplicateNickException(String message) {
        super(message);
    }
}
