package com.lucas.mastermind.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GameInProgressDTO {

    private Long id;
    private int[] guess;
    private String startTime;
    private int[] response;
    private int[][] previousResponses;
    private int[][] previousGuesses;
    private int round;
    private String finalMessage;


    public GameInProgressDTO(Long id, int[] guess, int[] response, int[][] previousResponses, int[][] previousGuesses, int round, String finalMessage) {
        this.id = id;
        this.guess = guess;
        this.response = response;
        this.previousResponses = previousResponses;
        this.previousGuesses = previousGuesses;
        this.round = round;
        this.finalMessage = finalMessage;
    }

    public GameInProgressDTO(Long id, int[] response, int round) {
        this.id = id;
        this.response = response;
        this.round = round;

    }

    public GameInProgressDTO(Long id, int[] guess) {
        this.id = id;
        this.guess = guess;
    }

    public GameInProgressDTO(Long id, int[][] previousResponses, int[][] previousGuesses, int round, LocalDateTime startTime) {
        this.id = id;
        this.previousResponses = previousResponses;
        this.previousGuesses = previousGuesses;
        this.round = round;
        this.startTime = String.valueOf(startTime);
    }
}