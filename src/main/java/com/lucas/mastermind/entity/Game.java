package com.lucas.mastermind.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Table(name = "games")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

//    @Column(name ="user", updatable = false)
//    private Long user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Transient
    private LocalDateTime startTime;

    @Transient
    private LocalDateTime finishTime;

    @Column(name = "duration", updatable = false)
    private long duration;

    @Transient
    private int round;

    @Column(name = "attempts", updatable = false)
    private int attempts;

    @Column(name = "date", updatable = false)
    private LocalDateTime date;

    @Column(name="points", updatable = false)
    private double points;

    @Column(name = "is_success", updatable = false)
    private boolean isSuccess;

    @Transient
    private int[] sequence = new int[8];

    @Column(name = "sequence", columnDefinition = "TEXT")
    private String sequenceJson;

    @Transient
    private int[][] guesses = new int[12][];

    @Column(name = "guesses", columnDefinition = "TEXT")
    private String guessesJson;

    @Transient
    private int[][] responses = new int[12][];

    @Column(name = "responses", columnDefinition = "TEXT")
    private String responsesJson;

    @JsonIgnore
    @Transient
    ObjectMapper objectMapper = new ObjectMapper();

    public Game(LocalDateTime startTime, int round, int[] sequence, int[][] guesses, int[][] responses) {
        this.startTime = startTime;
        this.round = round;
        this.sequence = sequence;
        this.guesses = guesses;
        this.responses = responses;
    }

    private void parseSequenceJson(){
        try {
            sequence = objectMapper.readValue(sequenceJson, int[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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

    private void parsePreviousResponsesJson() {
        try {
            if (responsesJson != null && !responsesJson.isEmpty()) {
                responses = objectMapper.readValue(responsesJson, int[][].class);
            } else {
                responses = new int[12][];
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @PrePersist
    public void onCreate(){
        try {
            sequenceJson = objectMapper.writeValueAsString(sequence);
            guessesJson = objectMapper.writeValueAsString(guesses);
            responsesJson=objectMapper.writeValueAsString(responses);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Game - error in JSON conversion");
        }
        finishTime = LocalDateTime.now();
        date=LocalDateTime.now();
        if (startTime != null) {
            duration = Duration.between(startTime, finishTime).getSeconds();
        } else {
            duration = 0;
        }
        attempts = round;
        pointsCalculation();
        isSuccessCheck();
    }

    private void pointsCalculation(){
        long time = 1200-duration;
        if (attempts != 0) {
            points = (double) time / attempts;
        } else {
            points=time;
        }
    }

    private void isSuccessCheck(){
        if(duration<1200){
            int x;
            if(round == 0){
               x = 0;
            }else{
                x=1;
            }
            String lastGuess = Arrays.toString(guesses[round - x]);
            String sequenceString = Arrays.toString(sequence);
            isSuccess=lastGuess.equals(sequenceString);
        }
    }
}
