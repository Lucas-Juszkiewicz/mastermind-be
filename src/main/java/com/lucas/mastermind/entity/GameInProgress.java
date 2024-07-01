package com.lucas.mastermind.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "game_in_progress")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameInProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name ="user_id", updatable = false)
    private Long userId;

    @Column(name = "start_time", updatable = false)
    private LocalDateTime startTime;

    @Column(name = "finish_time", updatable = false)
    private LocalDateTime finishTime;

    @Column(name = "round")
    private int round;

    @Transient
    private int[] sequence = new int[8];

    @Column(name = "sequence", columnDefinition = "TEXT")
    private String sequenceJson;

    @Transient
    private int[][] guesses = new int[12][];

    @Column(name = "guesses", columnDefinition = "TEXT")
    private String guessesJson;

    @Transient
    ObjectMapper objectMapper = new ObjectMapper();

    public GameInProgress(Long userId) {
        this.userId = userId;
    }

    public void addGuess(int round, int[] guess) {
        if (round < 0 || round >= guesses.length) {
            throw new IllegalArgumentException("Round must be between 0 and 11");
        }
        if (guess.length != 8) {
            throw new IllegalArgumentException("Guess must have a length of 8");
        }
        guesses[round] = guess;
        updateGuessesJson();
    }

    public int[] getGuess(int round) {
        if (round < 0 || round >= guesses.length) {
            throw new IllegalArgumentException("Round must be between 0 and 11");
        }
        return guesses[round];
    }

    private void updateGuessesJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            guessesJson = objectMapper.writeValueAsString(guesses);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    private void parseGuessesJson() {
        try {
            guesses = objectMapper.readValue(guessesJson, int[][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    private void parseSequenceJson(){
        try {
            sequence = objectMapper.readValue(sequenceJson, int[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }



    @PrePersist
    public void onCreate(){
        try {
            Random random = new Random();
            for (int i = 0; i < sequence.length; i++) {
                sequence[i] = random.nextInt(9) + 1;
                sequenceJson = objectMapper.writeValueAsString(sequence);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        updateGuessesJson();
        startTime = LocalDateTime.now();
        round = 0;
    }
    @PostLoad
    public void onLoad() {
        parseGuessesJson();
        parseSequenceJson();
    }
}
