package com.lucas.mastermind.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameInProgressDTO {

    private Long id;
    private int[] guess;

    private int[] response;
    private int[][] previousResponses;
    private int[][] previousGuesses;
    private int round;
    private String finalMessage;

    public GameInProgressDTO(Long id, int[] response, int round) {
        this.id = id;
        this.response = response;
        this.round = round;

    }

    public GameInProgressDTO(Long id, int[] guess) {
        this.id = id;
        this.guess = guess;
    }

    public GameInProgressDTO(Long id, int[] response, int[][] previousResponses, int[][] previousGuesses, int round, String finalMessage) {
        this.id = id;
        this.response = response;
        this.previousResponses = previousResponses;
        this.previousGuesses = previousGuesses;
        this.round = round;
        this.finalMessage = finalMessage;
    }
}