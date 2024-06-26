package com.lucas.mastermind.exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message){
        super(message);
    }
}
