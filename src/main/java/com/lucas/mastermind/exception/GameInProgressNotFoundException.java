package com.lucas.mastermind.exception;

public class GameInProgressNotFoundException extends RuntimeException{
    public GameInProgressNotFoundException(Long gameInProgressId){
        super("The game in progress with id: '" + gameInProgressId + " does not exist in our records");
    }
}
